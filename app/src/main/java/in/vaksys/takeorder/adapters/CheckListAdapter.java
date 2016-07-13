package in.vaksys.takeorder.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatSpinner;
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
import in.vaksys.takeorder.dbPojo.AddOrder;
import in.vaksys.takeorder.dbPojo.Temp;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 7/7/2016.
 */
public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyCheckList> {

    private Context mContext;
    private RealmResults<Temp> tempRealmResults = null;
    private RealmResults<AddContact> results;
    private Realm mRealm;
    private Temp tempOrder;
    Realm realm;
    private String id;
    private Dialog dialog;

    private EditText barcode;
    private EditText quantity;
    private EditText price;
    private EditText description;
    private AppCompatSpinner mSpinner;
    private String sp, barcode1, quantity1, price1, description1;


    public CheckListAdapter(Context mContext, RealmResults<Temp> tempRealmResults) {
        this.mContext = mContext;
        this.tempRealmResults = tempRealmResults;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public CheckListAdapter.MyCheckList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_list_raw, parent, false);

        return new MyCheckList(itemView);
    }

    @Override
    public void onBindViewHolder(final CheckListAdapter.MyCheckList holder, final int position) {
        tempOrder = tempRealmResults.get(position);

        id = tempOrder.getOrderId();
        Log.e("iddddd", id);

        final int pos = position;

        holder.orderId.setText(String.valueOf(position + 1));
        //holder.orderIdHidden.setText(addOrder.getOrderId());
        holder.orderIdHidden.setText(tempOrder.getBuyerName());
        holder.barcodeName.setText(tempOrder.getBarcode());
        holder.quantity.setText(tempOrder.getQuality());
        holder.price.setText(tempOrder.getPrice());
        holder.description.setText(tempOrder.getDescription());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.check_list_edit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//                id = holder.orderIdHidden.getText().toString();
//                Log.e("iddddd", id);

                mSpinner = (AppCompatSpinner) dialog.findViewById(R.id.sp_cus_name_checkList_edit);
                barcode = (EditText) dialog.findViewById(R.id.et_name_addOrder_checkList_edit);
                quantity = (EditText) dialog.findViewById(R.id.et_quantity_addOrder_checkList_edit);
                price = (EditText) dialog.findViewById(R.id.et_price_addOrder_checkList_edit);
                description = (EditText) dialog.findViewById(R.id.et_description_addOrder_checkList_edit);

                mRealm = Realm.getDefaultInstance();

                results = mRealm.where(AddContact.class).findAll();

                Log.e("sizeeeeee", "" + results.size());

                SpinnerTextAdapter newSpin = new SpinnerTextAdapter(mContext, results);
                mSpinner.setAdapter(newSpin);

                Button btnSave = (Button) dialog.findViewById(R.id.btn_save_addOrder_checkList_edit);

                Temp tempOrder = mRealm.where(Temp.class).equalTo("orderId", id).findFirst();
                System.out.println("Buyer Name in CheckList" + tempOrder.getBuyerName());
                System.out.println("barcode Name in CheckList" + tempOrder.getBarcode());
                System.out.println("quantity Name in CheckList" + tempOrder.getQuality());
                System.out.println("price Name in CheckList" + tempOrder.getPrice());
                System.out.println("description Name in CheckList" + tempOrder.getDescription());
                final String spValue = tempOrder.getBuyerName();
                String barcodeName = tempOrder.getBarcode();
                String quantityS = tempOrder.getQuality();
                String priceS = tempOrder.getPrice();
                String descriptionS = tempOrder.getDescription();

                barcode.setText(barcodeName);
                quantity.setText(quantityS);
                price.setText(priceS);
                description.setText(descriptionS);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //id = holder.orderIdHidden.getText().toString();
                        //Log.e("iddddd", id);

                        //sp = spinner.getSelectedItem().toString();
                        barcode1 = barcode.getText().toString();
                        quantity1 = quantity.getText().toString();
                        price1 = price.getText().toString();
                        description1 = description.getText().toString();

                        updatedatabase(id, spValue, barcode1, quantity1, price1, description1);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm = Realm.getDefaultInstance();

                mRealm.beginTransaction();
                Temp temp = mRealm.where(Temp.class).equalTo("orderId", id).findFirst();
                temp.setFlag(false);
                mRealm.commitTransaction();

                Temp aa = tempRealmResults.get(pos);
                mRealm.beginTransaction();
                AddOrder aaaa = mRealm.createObject(AddOrder.class);
                aaaa.setOrderId(aa.getOrderId());
                aaaa.setBuyerName(aa.getBuyerName());
                aaaa.setBarcode(aa.getBarcode());
                aaaa.setQuality(aa.getQuality());
                aaaa.setPrice(aa.getPrice());
                aaaa.setDescription(aa.getDescription());
                aaaa.setStartDate(aa.getStartDate());
                aaaa.setFlag(aa.isFlag());

                tempRealmResults.deleteFromRealm(pos);
                mRealm.commitTransaction();

                notifyDataSetChanged();
            }
        });

        RealmResults<AddOrder> temp = mRealm.where(AddOrder.class).equalTo("orderId", id).findAll();
        RealmResults<AddOrder> selectedOrder = temp.where().equalTo("flag", true).findAll();
        Log.e("lenths", String.valueOf(selectedOrder.size()));
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
    }

    @Override
    public int getItemCount() {
        return (null != tempRealmResults ? tempRealmResults.size() : 0);
    }

    public class MyCheckList extends RecyclerView.ViewHolder {

        public TextView orderId, barcodeName, quantity, price, description, orderIdHidden;
        private ImageView edit, delete;

        public MyCheckList(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.tv_orderId_checkList);
            barcodeName = (TextView) itemView.findViewById(R.id.tv_barcodeName_checkList);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity_checkList);
            price = (TextView) itemView.findViewById(R.id.tv_price_checkList);
            description = (TextView) itemView.findViewById(R.id.tv_description_checkList);
            orderIdHidden = (TextView) itemView.findViewById(R.id.orderIdHidden_checkList);
            edit = (ImageView) itemView.findViewById(R.id.edit_order_checkList);
            delete = (ImageView) itemView.findViewById(R.id.delete_order_checkList);
        }
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
        Temp user = mRealm.where(Temp.class).equalTo("orderId", id).findFirst();
        user.setBuyerName(sp);
        user.setBarcode(barcode1);
        user.setQuality(quantity1);
        user.setPrice(price1);
        user.setDescription(description1);

        mRealm.commitTransaction();

        tempRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Temp>>() {
            @Override
            public void onChange(RealmResults<Temp> element) {

                notifyDataSetChanged();
            }
        });

    }
}
