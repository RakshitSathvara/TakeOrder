package in.vaksys.takeorder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.vaksys.takeorder.R;
import in.vaksys.takeorder.dbPojo.AddOrder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dell980 on 7/7/2016.
 */
public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyCheckList> {

    private Context mContext;
    private RealmResults<AddOrder> addOrderRealmResults = null;
    private Realm mRealm;
    private AddOrder addOrder;
    Realm realm;


    public CheckListAdapter(Context mContext, RealmResults<AddOrder> addOrderRealmResults) {
        this.mContext = mContext;
        this.addOrderRealmResults = addOrderRealmResults;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public CheckListAdapter.MyCheckList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_list_raw, parent, false);

        return new MyCheckList(itemView);
    }

    @Override
    public void onBindViewHolder(CheckListAdapter.MyCheckList holder, int position) {
        addOrder = addOrderRealmResults.get(position);

        holder.orderId.setText(String.valueOf(position + 1));
        //holder.orderIdHidden.setText(addOrder.getOrderId());
        holder.orderIdHidden.setText(addOrder.getBuyerName());
        holder.barcodeName.setText(addOrder.getBarcode());
        holder.quantity.setText(addOrder.getQuality());
        holder.price.setText(addOrder.getPrice());
        holder.description.setText(addOrder.getDescription());
    }

    @Override
    public int getItemCount() {
        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
    }

    public class MyCheckList extends RecyclerView.ViewHolder {

        public TextView orderId, barcodeName, quantity, price, description, orderIdHidden;
        private ImageView edit;

        public MyCheckList(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.tv_orderId_checkList);
            barcodeName = (TextView) itemView.findViewById(R.id.tv_barcodeName_checkList);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity_checkList);
            price = (TextView) itemView.findViewById(R.id.tv_price_checkList);
            description = (TextView) itemView.findViewById(R.id.tv_description_checkList);
            orderIdHidden = (TextView) itemView.findViewById(R.id.orderIdHidden_checkList);
            edit = (ImageView) itemView.findViewById(R.id.edit_order_checkList);
        }
    }
}
