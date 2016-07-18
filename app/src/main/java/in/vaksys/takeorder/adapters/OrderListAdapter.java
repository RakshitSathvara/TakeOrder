package in.vaksys.takeorder.adapters;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import in.vaksys.takeorder.R;
import in.vaksys.takeorder.dbPojo.AddContact;
import in.vaksys.takeorder.dbPojo.AddOrder;
import in.vaksys.takeorder.dbPojo.Temp;
import in.vaksys.takeorder.extras.MyApplication;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/30/2016.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    private Context mContext;
    private RealmResults<AddOrder> addOrderRealmResults = null;
    private Realm mRealm;
    private AddOrder addOrder;
    private RealmResults<AddContact> results;
    private EditText barcode;
    private EditText quantity;
    private EditText price;
    private EditText description;
    private Dialog dialog;
    private String sp, barcode1, quantity1, price1, description1;
    private String id;
    private Spinner spinner;
    String buyerIdName;
    private MyApplication myApplication;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderId, barcodeName, quantity, price, description, orderIdHidden;
        private CheckBox checkBox;
        private ImageView edit, delete;
        private LinearLayout linearOrder;

        public MyViewHolder(View view) {
            super(view);
            orderId = (TextView) view.findViewById(R.id.tv_orderId_orderList);
            barcodeName = (TextView) view.findViewById(R.id.tv_barcodeName_orderList);
            quantity = (TextView) view.findViewById(R.id.tv_quantity_orderList);
            price = (TextView) view.findViewById(R.id.tv_price_orderList);
            description = (TextView) view.findViewById(R.id.tv_description_orderList);

            checkBox = (CheckBox) view.findViewById(R.id.checkboxOrder);

            edit = (ImageView) view.findViewById(R.id.edit_order);
            delete = (ImageView) view.findViewById(R.id.delete_order);

            orderIdHidden = (TextView) view.findViewById(R.id.orderIdHidden);
            linearOrder = (LinearLayout) view.findViewById(R.id.linearOrder);
        }
    }


    public OrderListAdapter(Context mContext, RealmResults<AddOrder> addOrderRealmResults) {
        this.mContext = mContext;
        this.addOrderRealmResults = addOrderRealmResults;
        mRealm = Realm.getDefaultInstance();
        myApplication = MyApplication.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_view_raw, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        addOrder = addOrderRealmResults.get(position);

        final int pos = position;

        holder.checkBox.setChecked(addOrder.isFlag());
        holder.checkBox.setTag(addOrderRealmResults.get(position));

//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox checkBox = (CheckBox) view;
//
//                mRealm = Realm.getDefaultInstance();
//
//                mRealm.beginTransaction();
//                AddOrder student = (AddOrder) checkBox.getTag();
//                AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
//                student.setFlag(checkBox.isChecked());
//                addOrderRealmResults.get(pos).setFlag(checkBox.isChecked());
//
//                //id = holder.orderIdHidden.getText().toString();
//                //Log.e("iddddd", id);
//                //mRealm.beginTransaction();
//
//
//                //addOrder.setFlag(true);
//                //mRealm.commitTransaction();
//                holder.linearOrder.setBackgroundResource(R.color.colorBackground);
//                // holder.linearOrder.setEnabled(false);
//                holder.edit.setEnabled(false);
//                holder.delete.setEnabled(false);
//
//                Toast.makeText(
//                        view.getContext(),
//                        "Clicked on Checkbox: " + checkBox.getText() + " is "
//                                + checkBox.isChecked(), Toast.LENGTH_LONG).show();
//
//                mRealm.commitTransaction();
//            }
//        });

        //  AddContact results = mRealm.where(AddContact.class).findFirst();
        holder.orderId.setText(String.valueOf(position + 1));
        holder.orderIdHidden.setText(addOrder.getOrderId());
        holder.barcodeName.setText(addOrder.getBarcode());
        holder.quantity.setText(addOrder.getQuality());
        holder.price.setText(addOrder.getPrice());
        holder.description.setText(addOrder.getDescription());
        holder.checkBox.setId(position);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_contact_edit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                id = holder.orderIdHidden.getText().toString();
                Log.e("iddddd", id);

                spinner = (Spinner) dialog.findViewById(R.id.sp_cus_name_edit);
                barcode = (EditText) dialog.findViewById(R.id.et_name_addOrder_edit);
                quantity = (EditText) dialog.findViewById(R.id.et_quantity_addOrder_edit);
                price = (EditText) dialog.findViewById(R.id.et_price_addOrder_edit);
                description = (EditText) dialog.findViewById(R.id.et_description_addOrder_edit);

                mRealm = Realm.getDefaultInstance();

                results = mRealm.where(AddContact.class).findAll();

                Log.e("sizeeeeee", "" + results.size());

                SpinnerTextAdapter spinnerTextAdapter = new SpinnerTextAdapter(mContext, results);

                // attaching data adapter to spinner
                spinner.setAdapter(spinnerTextAdapter);

                Button btnSave = (Button) dialog.findViewById(R.id.btn_save_addOrder_edit);
                Button btnFinish = (Button) dialog.findViewById(R.id.btn_finish_addOrder_edit);

                AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
                final String spValue = addOrder.getBuyerName();
                String barcodeName = addOrder.getBarcode();
                String quantityS = addOrder.getQuality();
                String priceS = addOrder.getPrice();
                String descriptionS = addOrder.getDescription();

                barcode.setText(barcodeName);
                quantity.setText(quantityS);
                price.setText(priceS);
                description.setText(descriptionS);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                buyerIdName = ((TextView) view.findViewById(R.id.spin_text)).getText().toString();
//                                System.out.println("On Edit Save-----" + buyerIdName);
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                            }
//                        });

                        id = holder.orderIdHidden.getText().toString();
                        Log.e("iddddd", id);

                        //sp = spinner.getSelectedItem().toString();
                        barcode1 = barcode.getText().toString();
                        quantity1 = quantity.getText().toString();
                        price1 = price.getText().toString();
                        description1 = description.getText().toString();

                        updatedatabase(id, spValue, barcode1, quantity1, price1, description1);
                        myApplication.hideKeyboard((Activity) mContext);
                        dialog.dismiss();


                    }
                });

                btnFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sp = spinner.getSelectedItem().toString();
                        barcode1 = barcode.getText().toString();
                        quantity1 = quantity.getText().toString();
                        price1 = price.getText().toString();
                        description1 = description.getText().toString();

                        updatedatabase(id, spValue, barcode1, quantity1, price1, description1);

                        myApplication.hideKeyboard((Activity) mContext);
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
                alertDialogBuilder.setTitle("Sure want to Delete this order ??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                final String deleteId = holder.orderIdHidden.getText().toString();
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

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;

                mRealm = Realm.getDefaultInstance();

                mRealm.beginTransaction();
                AddOrder student = (AddOrder) checkBox.getTag();
                AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
                student.setFlag(true);
                //addOrderRealmResults.get(pos).setFlag(checkBox.isChecked());
                mRealm.commitTransaction();

//                AddOrder aa = addOrderRealmResults.get(pos);
//                mRealm.beginTransaction();
//                Temp aaaa = mRealm.createObject(Temp.class);
//                aaaa.setOrderId(aa.getOrderId());
//                aaaa.setBuyerName(aa.getBuyerName());
//                aaaa.setBarcode(aa.getBarcode());
//                aaaa.setQuality(aa.getQuality());
//                aaaa.setPrice(aa.getPrice());
//                aaaa.setDescription(aa.getDescription());
//                aaaa.setStartDate(aa.getStartDate());
//                aaaa.setFlag(aa.isFlag());
//
//                addOrderRealmResults.deleteFromRealm(pos);
//                mRealm.commitTransaction();

                addOrderRealmResults.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
                    @Override
                    public void onChange(RealmResults<AddOrder> element) {
                        notifyDataSetChanged();
                    }
                });
                //notifyDataSetChanged();
                //id = holder.orderIdHidden.getText().toString();
                //Log.e("iddddd", id);
                //mRealm.beginTransaction();


                //addOrder.setFlag(true);
                //mRealm.commitTransaction();

                //holder.linearOrder.setVisibility(View.GONE);
                //holder.checkBox.setEnabled(false);

//                holder.linearOrder.removeViewAt(position);

                //removeAt(pos);
                //holder.linearOrder.setEnabled(false);
                //holder.edit.setEnabled(false);
                //holder.delete.setEnabled(false);

//                Toast.makeText(
//                        view.getContext(),
//                        "Clicked on Checkbox: " + checkBox.getText() + " is "
//                                + checkBox.isChecked(), Toast.LENGTH_SHORT).show();

            }

        });

        final RealmResults<AddOrder> temp = mRealm.where(AddOrder.class).equalTo("orderId", id).findAll();
        RealmResults<AddOrder> selectedOrder = temp.where().equalTo("flag", false).findAll();
        Log.e("lenths in orderlist", String.valueOf(selectedOrder.size()));
        if (selectedOrder.size() > 0) {

//            holder.linearOrder.setBackgroundResource(R.color.colorBackground);
//            // holder.linearOrder.setEnabled(false);
//            holder.edit.setEnabled(false);
//            holder.delete.setEnabled(false);
            selectedOrder.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
                @Override
                public void onChange(RealmResults<AddOrder> element) {
                    notifyDataSetChanged();
                }
            });
        }

//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    CheckBox checkBox = (CheckBox) compoundButton;
//
//                    mRealm = Realm.getDefaultInstance();
//
//                    mRealm.beginTransaction();
//                    AddOrder student = (AddOrder) checkBox.getTag();
//                    AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
//                    student.setFlag(checkBox.isChecked());
//                    addOrderRealmResults.get(pos).setFlag(checkBox.isChecked());
//
//                    //id = holder.orderIdHidden.getText().toString();
//                    //Log.e("iddddd", id);
//                    //mRealm.beginTransaction();
//
//
//                    //addOrder.setFlag(true);
//                    //mRealm.commitTransaction();
//
//                    ViewGroup parent = (ViewGroup) compoundButton.getParent();
//                    if (parent != null) {
//                        parent.removeView(holder.linearOrder);
//                    }
//                    holder.linearOrder.setVisibility(View.GONE);
//                    holder.linearOrder.removeViewAt(position);
//
//                    //removeAt(pos);
//                    //holder.linearOrder.setEnabled(false);
//                    //holder.edit.setEnabled(false);
//                    //holder.delete.setEnabled(false);
//
//                    Toast.makeText(
//                            compoundButton.getContext(),
//                            "Clicked on Checkbox: " + checkBox.getText() + " is "
//                                    + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
//
//                    mRealm.commitTransaction();
//                }
//                } else {
//                    CheckBox checkBox = (CheckBox) compoundButton;
//
//                    mRealm = Realm.getDefaultInstance();
//
//                    mRealm.beginTransaction();
//                    AddOrder student = (AddOrder) checkBox.getTag();
//                    AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
//                    student.setFlag(false);
//                    addOrderRealmResults.get(pos).setFlag(checkBox.isChecked());
//
//                    //id = holder.orderIdHidden.getText().toString();
//                    //Log.e("iddddd", id);
//                    //mRealm.beginTransaction();
//
//
//                    //addOrder.setFlag(true);
//                    //mRealm.commitTransaction();
//                    holder.linearOrder.setBackgroundColor(Color.WHITE);
//                    holder.edit.setEnabled(true);
//                    holder.delete.setEnabled(true);
//
//                    Toast.makeText(
//                            compoundButton.getContext(),
//                            "Clicked on Checkbox: " + checkBox.getText() + " is "
//                                    + checkBox.isChecked(), Toast.LENGTH_SHORT).show();
//
//                    mRealm.commitTransaction();
//                }
//            }
//        });

//        holder.checkBox.setTag(addOrderRealmResults.get(position));
//
//        holder.checkBox.setOnCheckedChangeListener(null);
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                int pp = holder.checkBox.getId();
////                holder.checkBox.setTag(position);
//                holder.checkBox.setSelected(b);
//                if (b) {
//                    id = holder.orderIdHidden.getText().toString();
//                    Log.e("iddddd", id);
//                    mRealm.beginTransaction();
//
//                    AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
//
//                    addOrder.setFlag(true);
//                    mRealm.commitTransaction();
//                    holder.linearOrder.setBackgroundResource(R.color.colorBackground);
//                    // holder.linearOrder.setEnabled(false);
//                    holder.edit.setEnabled(false);
//                    holder.delete.setEnabled(false);
//
//                    //AddOrder addOrder1 = mRealm.where(AddOrder.class).equalTo("orderId",pos).findFirst();
//
//                } else {
//
//                    id = holder.orderIdHidden.getText().toString();
//                    Log.e("iddddd", id);
//
//                    mRealm.beginTransaction();
//
//                    AddOrder addOrder = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
//                    addOrder.setFlag(false);
//
//                    mRealm.commitTransaction();
//                    holder.linearOrder.setBackgroundColor(Color.WHITE);
//                    holder.edit.setEnabled(true);
//                    holder.delete.setEnabled(true);
//                    //holder.linearOrder.setClickable(false);
//
//                }
//
//            }
//        });

    }

    private void saveData() {

//        if (!validateBarcodeName()) {
//            return;
//        }
//        if (!validateQuatity()) {
//            return;
//        }
//        if (!validatePrice()) {
//
//        }
//        if (!validateDescription()) {
//
//        }


    }


    @Override
    public int getItemCount() {
        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
    }

    private void updatedatabase(String id, String sp, String barcode1, String quantity1, String price1, String description1) {

        System.out.println("id" + id);
        System.out.println("buyer name" + sp);
        System.out.println("barcode" + barcode1);
        System.out.println("quantity" + quantity1);
        System.out.println("price" + price1);
        System.out.println("dec" + description1);

        mRealm = Realm.getDefaultInstance();

        mRealm.beginTransaction();
        AddOrder user = mRealm.where(AddOrder.class).equalTo("orderId", id).findFirst();
        user.setBuyerName(sp);
        user.setBarcode(barcode1);
        user.setQuality(quantity1);
        user.setPrice(price1);
        user.setDescription(description1);

        mRealm.commitTransaction();

        addOrderRealmResults.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
            @Override
            public void onChange(RealmResults<AddOrder> element) {

                notifyDataSetChanged();
            }
        });

    }

    private void DeleteVehicle(String contactId) {

        mRealm = Realm.getDefaultInstance();

        mRealm.beginTransaction();
        AddOrder user = mRealm.where(AddOrder.class).equalTo("orderId", contactId).findFirst();

        user.deleteFromRealm();

        mRealm.commitTransaction();
    }

//    private boolean validateBarcodeName() {
//        if (etNameAddOrder.getText().toString().trim().isEmpty()) {
//            etNameAddOrder.setError(getString(R.string.err_msg_barcoede));
//            requestFocus(etNameAddOrder);
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private boolean validateQuatity() {
//        if (etQuantityAddOrder.getText().toString().trim().isEmpty()) {
//            etQuantityAddOrder.setError(getString(R.string.err_msg_quantity));
//            requestFocus(etQuantityAddOrder);
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private boolean validatePrice() {
//        if (etPriceAddOrder.getText().toString().trim().isEmpty()) {
//            etPriceAddOrder.setText("-");
//            requestFocus(etPriceAddOrder);
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private boolean validateDescription() {
//        if (etDescriptionAddOrder.getText().toString().trim().isEmpty()) {
//            etDescriptionAddOrder.setText("-");
//            requestFocus(etDescriptionAddOrder);
//            return false;
//        } else {
//            return true;
//        }
//    }
}

