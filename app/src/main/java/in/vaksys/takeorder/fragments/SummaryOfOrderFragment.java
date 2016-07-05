package in.vaksys.takeorder.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.vaksys.takeorder.R;
import in.vaksys.takeorder.adapters.SummaryListAdapter;
import in.vaksys.takeorder.dbPojo.AddOrder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/27/2016.
 */
public class SummaryOfOrderFragment extends Fragment {

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

        mRealm = Realm.getDefaultInstance();

        summaryRecycler = (RecyclerView) rootView.findViewById(R.id.summaryRecycler);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        summaryRecycler.setLayoutManager(manager);

        btnGenerate = (Button) rootView.findViewById(R.id.btn_generate_report);
        ll1 = (LinearLayout) rootView.findViewById(R.id.ll1);
        clickSummaryGenerate = rootView.findViewById(R.id.click_generate_report_summary);

        startDate = (EditText) rootView.findViewById(R.id.et_startDate);
        endDate = (EditText) rootView.findViewById(R.id.et_endDate);

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
        } catch (java.text.ParseException e) {
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
}
