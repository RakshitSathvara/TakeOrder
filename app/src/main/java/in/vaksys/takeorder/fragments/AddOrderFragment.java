package in.vaksys.takeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.takeorder.R;
import in.vaksys.takeorder.adapters.SpinnerTextAdapter;
import in.vaksys.takeorder.dbPojo.AddContact;
import in.vaksys.takeorder.dbPojo.AddOrder;
import in.vaksys.takeorder.extras.MyApplication;
import in.vaksys.takeorder.model.Message;
import in.vaksys.takeorder.model.MessageSec;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/28/2016.
 */
public class AddOrderFragment extends Fragment {

    @Bind(R.id.sp_cus_name)
    Spinner spCusName;
    @Bind(R.id.et_name_addOrder)
    EditText etNameAddOrder;
    @Bind(R.id.et_quantity_addOrder)
    EditText etQuantityAddOrder;
    @Bind(R.id.et_price_addOrder)
    EditText etPriceAddOrder;
    @Bind(R.id.et_description_addOrder)
    EditText etDescriptionAddOrder;
    @Bind(R.id.btn_save_addOrder)
    Button btnSaveAddOrder;
    @Bind(R.id.btn_finish_addOrder)
    Button btnFinishAddOrder;

    private Realm mRealm;
    private RealmResults<AddContact> results;
    private String sp;
    private String buyerIdName = "";

    private SimpleDateFormat sdf, dateFormatter;
    Date d = null;
    private MyApplication myApplication;

    public static AddOrderFragment newInstance(int index) {
        AddOrderFragment fragment = new AddOrderFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_order, container, false);
        ButterKnife.bind(this, rootView);

        myApplication = MyApplication.getInstance();

        mRealm = Realm.getDefaultInstance();

        results = mRealm.where(AddContact.class).findAll();

        SpinnerTextAdapter spinnerTextAdapter = new SpinnerTextAdapter(getActivity(), results);

        // attaching data adapter to spinner
        spCusName.setAdapter(spinnerTextAdapter);

        btnSaveAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                myApplication.hideKeyboard(getActivity());
            }
        });

        spCusName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp = spCusName.getSelectedItem().toString();
                buyerIdName = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
                Log.e("FINISH", "onClick: " + buyerIdName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFinishAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sp = spCusName.getSelectedItem().toString();
//                buyerIdName = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();

                submitForm();
                myApplication.hideKeyboard(getActivity());
                Log.e("FINISH", "onClick: " + sp + buyerIdName);
                EventBus.getDefault().post(new Message(buyerIdName));

                MessageSec messageSec = new MessageSec(buyerIdName);

                EventBus.getDefault().post(messageSec);

            }
        });

        return rootView;
    }

    private void addOrder(String sp, String buyerIDName, String name, String quantity, String price, String description, Date startDate) {

        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        AddOrder addOrder = mRealm.createObject(AddOrder.class);

        addOrder.setOrderId(UUID.randomUUID().toString());
        Log.e("AddOrder", "addOrder: " + buyerIDName);
        addOrder.setBuyerName(buyerIDName);
        addOrder.setBarcode(name);
        addOrder.setQuality(quantity);
        addOrder.setPrice(price);
        addOrder.setDescription(description);
        addOrder.setStartDate(startDate);

        mRealm.commitTransaction();
        mRealm.close();

        Toast.makeText(getActivity(), "Order Saved.", Toast.LENGTH_SHORT).show();

        RealmResults<AddOrder> results = mRealm.where(AddOrder.class).findAll();
        for (AddOrder s : results) {
            s.getOrderId();
            s.getBuyerName();
            s.getBarcode();
            s.getQuality();
            s.getPrice();
            s.getDescription();
            Log.e("MainActivity", "Get Data: " + s);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateBarcodeName() {
        if (etNameAddOrder.getText().toString().trim().isEmpty()) {
            etNameAddOrder.setError(getString(R.string.err_msg_barcoede));
            requestFocus(etNameAddOrder);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateQuatity() {
        if (etQuantityAddOrder.getText().toString().trim().isEmpty()) {
            etQuantityAddOrder.setError(getString(R.string.err_msg_quantity));
            requestFocus(etQuantityAddOrder);
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePrice() {
        if (etPriceAddOrder.getText().toString().trim().isEmpty()) {
            etPriceAddOrder.setText("-");
            requestFocus(etPriceAddOrder);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateDescription() {
        if (etDescriptionAddOrder.getText().toString().trim().isEmpty()) {
            etDescriptionAddOrder.setText("-");
            requestFocus(etDescriptionAddOrder);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateSpinner() {


        if (buyerIdName.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "Please Add Contact First.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }


    }

    private void submitForm() {

        spCusName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                buyerIdName = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (!validateSpinner()) {
            return;
        }
        if (!validateBarcodeName()) {
            return;
        }
        if (!validateQuatity()) {
            return;
        }
        if (!validatePrice()) {

        }
        if (!validateDescription()) {

        }

//        sp = spCusName.getSelectedItem().toString();

        String name = etNameAddOrder.getText().toString();
        String quantity = etQuantityAddOrder.getText().toString();
        String price = etPriceAddOrder.getText().toString();
        String description = etDescriptionAddOrder.getText().toString();

        // sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

//        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//
//        final Calendar newCalendar = Calendar.getInstance();
//
//        newCalendar.add(Calendar.DAY_OF_MONTH, 0);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
//        String formattedDate = sdf.format(newCalendar.getTime());
//        try {
//            d = sdf.parse(formattedDate);
//            Log.e("DATE", "submitForm: " + d);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        //get current date time with Date()
        Date date = new Date();
        String a = dateFormat.format(date);
        try {
            date = dateFormat.parse(a);
            Log.e("CCCCCCCCCC", "submitForm: " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("DATE IS " + dateFormat.format(date));

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        System.out.println("DATE IS 1" + dateFormat.format(cal.getTime()));
        Log.e("ADDORDER", "submitForm: " + dateFormat.format(cal.getTime()));

        addOrder(sp, buyerIdName, name, quantity, price, description, date);

        etNameAddOrder.setText("");
        etQuantityAddOrder.setText("");
        etPriceAddOrder.setText("");
        etDescriptionAddOrder.setText("");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
