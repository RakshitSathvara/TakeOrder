package in.vaksys.takeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.takeorder.R;
import in.vaksys.takeorder.dbPojo.AddContact;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/28/2016.
 */
public class AddContactFragment extends Fragment {

    @Bind(R.id.et_buyerName_addContact)
    EditText etBuyerNameAddContact;
    @Bind(R.id.et_phone_addContact)
    EditText etPhoneAddContact;
    @Bind(R.id.et_city_addContact)
    EditText etCityAddContact;
    @Bind(R.id.btn_save_addContact)
    Button btnSaveAddContact;

    private Realm mRealm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);
        ButterKnife.bind(this, rootView);

        mRealm = Realm.getDefaultInstance();

        btnSaveAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        return rootView;
    }

    private void addContact(String buyerName, String phone, String city) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();

        AddContact addContact = mRealm.createObject(AddContact.class);

        addContact.setContactId(UUID.randomUUID().toString());
        addContact.setBuyerName(buyerName);
        addContact.setPhone(phone);
        addContact.setCity(city);

        mRealm.commitTransaction();
        mRealm.close();


        Toast.makeText(getActivity(), "Contact Saved.", Toast.LENGTH_SHORT).show();
        RealmResults<AddContact> results = mRealm.where(AddContact.class).findAll();
        for (AddContact s : results) {
            s.getContactId();
            s.getBuyerName();
            s.getPhone();
            s.getCity();
            Log.e("MainActivity", "Get Data: " + s);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateBuyerName() {
        if (etBuyerNameAddContact.getText().toString().trim().isEmpty()) {
            etBuyerNameAddContact.setError(getString(R.string.err_msg_buyer_name));
            requestFocus(etBuyerNameAddContact);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateNumber() {
        if (etPhoneAddContact.getText().toString().trim().isEmpty()) {
            etPhoneAddContact.setError(getString(R.string.err_msg_phone_name));
            requestFocus(etPhoneAddContact);
            return false;
        }
        if (etPhoneAddContact.length() != 10) {
            etPhoneAddContact.setError(getString(R.string.err_msg_phone));
            requestFocus(etPhoneAddContact);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateCity() {
        if (etCityAddContact.getText().toString().trim().isEmpty()) {
            etCityAddContact.setError(getString(R.string.err_msg_city));
            requestFocus(etCityAddContact);
            return false;
        } else {
            return true;
        }
    }

    private void submitForm() {
        if (!validateBuyerName()) {
            return;
        }
        if (!validateNumber()) {
            return;
        }
        if (!validateCity()) {
            return;
        }

        String buyerName = etBuyerNameAddContact.getText().toString();
        String phone = etPhoneAddContact.getText().toString();
        String city = etCityAddContact.getText().toString();


        addContact(buyerName, phone, city);

        etBuyerNameAddContact.setText("");
        etPhoneAddContact.setText("");
        etCityAddContact.setText("");
    }
}
