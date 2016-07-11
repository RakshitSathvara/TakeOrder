package in.vaksys.takeorder.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.takeorder.R;
import in.vaksys.takeorder.activities.UploadFile;
import in.vaksys.takeorder.adapters.SummaryListAdapter;
import in.vaksys.takeorder.dbPojo.AddOrder;
import in.vaksys.takeorder.extras.Constants;
import in.vaksys.takeorder.extras.Utils;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/27/2016.
 */
public class SummaryOfOrderFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.btn_dropbox_summary)
    Button btnDropboxSummary;
    private Button btnGenerate;
    private LinearLayout ll1;
    private View clickSummaryGenerate;
    private EditText startDate, endDate;
    private Realm mRealm;
    private RealmResults<AddOrder> addOrderRealmResults;
    Date oneDate, twoDate;
    private RecyclerView summaryRecycler;
    private SummaryListAdapter summaryListAdapter;
    private SimpleDateFormat sdf, dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
    Date d = null;
    Date startDate1;

    private static final String IMAGE_DIRECTORY_NAME = "MyData";
    public static String timeStamp;
    public static final int MEDIA_TYPE_IMAGE = 1;
    File mediaFile;

    private List<String[]> data = new ArrayList<>();

    private DropboxAPI<AndroidAuthSession> mApi;
    private final String DIR = "/";
    private boolean mLoggedIn, onResume;


    public static SummaryOfOrderFragment newInstance(int index) {
        SummaryOfOrderFragment fragment = new SummaryOfOrderFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fagment_summary_of_order, container, false);
        ButterKnife.bind(this, rootView);
        mRealm = Realm.getDefaultInstance();

        summaryRecycler = (RecyclerView) rootView.findViewById(R.id.summaryRecycler);

        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        summaryRecycler.setLayoutManager(manager);

        btnGenerate = (Button) rootView.findViewById(R.id.btn_generate_report);
        ll1 = (LinearLayout) rootView.findViewById(R.id.ll1);
        clickSummaryGenerate = rootView.findViewById(R.id.click_generate_report_summary);

        startDate = (EditText) rootView.findViewById(R.id.et_startDate);
        endDate = (EditText) rootView.findViewById(R.id.et_endDate);

        btnDropboxSummary.setOnClickListener(this);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                selectdate(startDate);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new DatePickerDialog(getActivity(), date1, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                selectdate(endDate);
            }
        });

        clickSummaryGenerate.setVisibility(View.GONE);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateStartDate()) {
                    return;
                }
                if (!validateEndDate()) {
                    return;
                }
                clickSummaryGenerate.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);

                addOrderRealmResults = mRealm.where(AddOrder.class).between("startDate", startDate1, startDate1)
                        .findAll();
                Log.e("RESULT", "onClick: " + addOrderRealmResults.size());
//                for (AddOrder ss : addOrderRealmResults){
//                    addOrder.getOrderId();
//                    addOrder.getBuyerName();
//                    addOrder.getBarcode();
//                    addOrder.getQuality();
//                    addOrder.getPrice();
//                   addOrder.getDescription();
//                }
                summaryListAdapter = new SummaryListAdapter(getActivity(), addOrderRealmResults);
                summaryRecycler.setHasFixedSize(true);
                summaryRecycler.setItemAnimator(new DefaultItemAnimator());
                summaryRecycler.setNestedScrollingEnabled(false);
                summaryRecycler.setAdapter(summaryListAdapter);

                addOrderRealmResults.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
                    @Override
                    public void onChange(RealmResults<AddOrder> element) {
                        summaryListAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


        return rootView;
    }


    private void selectdate(final EditText date) {


        sdf = new SimpleDateFormat("dd-MM-yyyy");

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        final Calendar newCalendar = Calendar.getInstance();

        newCalendar.add(Calendar.DAY_OF_MONTH, 0);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String formattedDate = sdf.format(newCalendar.getTime());
        try {
            d = sdf.parse(formattedDate);
            Log.e("DATE SUMMARY", "selectdate: " + d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
                newCalendar.set(year, monthOfYear, dayOfMonth);
                //  newCalendar.add(Calendar.DATE, -5);
                //startDate1 = newCalendar.getTime();
                String temp = dateFormatter.format(newCalendar.getTime());
                try {
                    startDate1 = dateFormatter.parse(temp);
                    Log.e("date", "onDateSet: " + startDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("DATE-SUMMARY", "onDateSet: " + dateFormatter.format(newCalendar.getTime()));
                date.setText(dateFormatter.format(newCalendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.getDatePicker().setMaxDate(d.getTime());

        fromDatePickerDialog.show();
    }

    private boolean validateStartDate() {
        if (startDate.getText().toString().trim().isEmpty()) {
            startDate.setError(getString(R.string.err_msg_startdate));
            requestFocus(startDate);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEndDate() {
        if (endDate.getText().toString().trim().isEmpty()) {
            endDate.setError(getString(R.string.err_msg_enddate));
            requestFocus(endDate);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {

        if (addOrderRealmResults.size() > 0) {


            try {


                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        IMAGE_DIRECTORY_NAME);

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        // MyApplication.getInstance().showLog("TAG", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");

                    }
                }

                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());


                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "file_" + timeStamp + ".csv");
                CSVWriter writer = new CSVWriter(new FileWriter(mediaFile));


                for (AddOrder s : addOrderRealmResults) {

                    data.add(new String[]{s.getOrderId(), s.getBuyerName(), s.getBarcode(), s.getBuyerName(), s.getQuality(), s.getPrice(), s.getDescription()});
//                   data.add(new String[]{ s.getCity()});
//                   data.add(new String[]{s.getContactId()});
                    writer.writeAll(data);

                }
                //  writer.writeAll(data);
                writer.close();


                System.out.println("*** Also wrote this information to file: " + mediaFile);

                if (mLoggedIn) {
                    logOut();
                }

                if (Utils.isOnline(getActivity())) {
                    mApi.getSession().startAuthentication(getActivity());
                    onResume = true;
                } else {
                    Utils.showNetworkAlert(getActivity());
                }

                mLoggedIn = false;
                if (false) {
                    UploadFile upload = new UploadFile(getActivity(), mApi, DIR, mediaFile);
                    upload.execute();
                    //onResume = false;

                }

//                List<String[]> data = new ArrayList<String[]>();
//                data.add(new String[]{"India", "New Delhi"});
//                data.add(new String[]{"United States", "Washington D.C"});
//                data.add(new String[]{"Germany", "Berlin"});


            } catch (Exception e) {

                e.printStackTrace();
            }


        } else {
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
        }


    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(Constants.DROPBOX_APP_KEY,
                Constants.DROPBOX_APP_SECRET);
        AndroidAuthSession session;

        String[] stored = getKeys();
        if (stored != null) {
            AccessTokenPair accessToken = new AccessTokenPair(stored[0],
                    stored[1]);
            session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE,
                    accessToken);
        } else {
            session = new AndroidAuthSession(appKeyPair, Constants.ACCESS_TYPE);
        }

        return session;
    }

    private String[] getKeys() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(Constants.ACCESS_KEY_NAME, null);
        String secret = prefs.getString(Constants.ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
            String[] ret = new String[2];
            ret[0] = key;
            ret[1] = secret;
            return ret;
        } else {
            return null;
        }
    }


    private void logOut() {
        mApi.getSession().unlink();

        clearKeys();
    }

    private void clearKeys() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    @Override
    public void onResume() {

        AndroidAuthSession session = mApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                storeKeys(tokens.key, tokens.secret);
                setLoggedIn(onResume);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:"
                        + e.getLocalizedMessage());
            }
        }
        super.onResume();
    }

    private void storeKeys(String key, String secret) {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(Constants.ACCESS_KEY_NAME, key);
        edit.putString(Constants.ACCESS_SECRET_NAME, secret);
        edit.commit();
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        error.show();
    }

    public void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
        if (loggedIn) {
            UploadFile upload = new UploadFile(getActivity(), mApi, DIR, mediaFile);
            upload.execute();
            onResume = false;

        }
    }
}
