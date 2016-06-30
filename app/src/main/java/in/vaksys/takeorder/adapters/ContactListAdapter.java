package in.vaksys.takeorder.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import in.vaksys.takeorder.R;
import in.vaksys.takeorder.dbPojo.AddContact;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/29/2016.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private Context mContext;
    private RealmResults<AddContact> addContactsList = null;
    private Realm mRealm;
    private AddContact addContact;
    EditText buyerName;
    EditText phone;
    EditText city;
    Realm realm;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView contactId, buyerName, phone, city, contactIdHidden;
        public ImageView edit, delete;

        public MyViewHolder(View view) {
            super(view);
            contactId = (TextView) view.findViewById(R.id.tv_contactId_contactList);
            buyerName = (TextView) view.findViewById(R.id.tv_buyerName_contactList);
            phone = (TextView) view.findViewById(R.id.tv_phone_contactList);
            city = (TextView) view.findViewById(R.id.tv_city_contactList);
            contactIdHidden = (TextView) view.findViewById(R.id.contactIdHidden);

            edit = (ImageView) view.findViewById(R.id.edit_contact);
            delete = (ImageView) view.findViewById(R.id.delete_contact);
        }
    }


    public ContactListAdapter(Context mContext, RealmResults<AddContact> addContactsList) {
        this.mContext = mContext;
        this.addContactsList = addContactsList;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_view_raw, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        addContact = addContactsList.get(position);

        //  AddContact results = mRealm.where(AddContact.class).findFirst();
        holder.contactId.setText(String.valueOf(position + 1));
        holder.contactIdHidden.setText(addContact.getContactId());
        holder.buyerName.setText(addContact.getBuyerName());
        holder.phone.setText(addContact.getPhone());
        holder.city.setText(addContact.getCity());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_order_edit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final String id = holder.contactIdHidden.getText().toString();
                Log.e("iddddd", id);

                final EditText buyerName = (EditText) dialog.findViewById(R.id.et_buyerName_addContact_edit);
                final EditText phone = (EditText) dialog.findViewById(R.id.et_phone_addContact_edit);
                final EditText city = (EditText) dialog.findViewById(R.id.et_city_addContact_edit);

                Button btnSave = (Button) dialog.findViewById(R.id.btn_save_addContact_edit);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name, phoneNo, citys;

                        name = buyerName.getText().toString();
                        phoneNo = phone.getText().toString();
                        citys = city.getText().toString();

                        updatedatabase(id, name, phoneNo, citys);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("Sure want to Delete this contact ??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final String deleteId = holder.contactIdHidden.getText().toString();
                                Log.e("iddddd", deleteId);

                                DeleteVehicle(deleteId);
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    private void DeleteVehicle(String contactId) {

        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        AddContact user = realm.where(AddContact.class).equalTo("contactId", contactId).findFirst();

        user.deleteFromRealm();

        realm.commitTransaction();
    }


    private void updatedatabase(String id, String buyerName, String phone, String city) {

        realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        AddContact user = realm.where(AddContact.class).equalTo("contactId", id).findFirst();

        user.setBuyerName(buyerName);
        user.setPhone(phone);
        user.setCity(city);
        realm.commitTransaction();

        addContactsList.addChangeListener(new RealmChangeListener<RealmResults<AddContact>>() {
            @Override
            public void onChange(RealmResults<AddContact> element) {

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != addContactsList ? addContactsList.size() : 0);
    }

    private void Submit(String s) {


    }

}
