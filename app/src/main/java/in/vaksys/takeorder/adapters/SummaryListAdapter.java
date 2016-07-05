package in.vaksys.takeorder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.vaksys.takeorder.R;
import in.vaksys.takeorder.dbPojo.AddOrder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dell980 on 7/5/2016.
 */
public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.MyViewHolder> {

    private Context mContext;
    private AddOrder addOrder;
    private RealmResults<AddOrder> addOrderRealmResults;
    private Realm mRealm;

    public SummaryListAdapter(Context mContext, RealmResults<AddOrder> addOrderRealmResults) {
        this.mContext = mContext;
        this.addOrderRealmResults = addOrderRealmResults;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_to_dateorder, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SummaryListAdapter.MyViewHolder holder, int position) {
        addOrder = addOrderRealmResults.get(position);

        holder.summaryId.setText(String.valueOf(position + 1));
        holder.buyerName.setText(addOrder.getBuyerName());
        holder.barcodeName.setText(addOrder.getBarcode());
        holder.quantity.setText(addOrder.getQuality());
        holder.price.setText(addOrder.getPrice());
        holder.description.setText(addOrder.getDescription());
        //holder.date.setText((CharSequence) addOrder.getStartDate());
    }

    @Override
    public int getItemCount() {
        return (null != addOrderRealmResults ? addOrderRealmResults.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView summaryId, hiddenSummaryId, barcodeName, buyerName, quantity, price, description, date;

        public MyViewHolder(View itemView) {
            super(itemView);
            summaryId = (TextView) itemView.findViewById(R.id.tv_summaryId_summaryList);
            hiddenSummaryId = (TextView) itemView.findViewById(R.id.summaryIdHidden);
            buyerName = (TextView) itemView.findViewById(R.id.tv_buyerName_summaryList);
            barcodeName = (TextView) itemView.findViewById(R.id.tv_barcodeName_summaryList);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity_summaryList);
            price = (TextView) itemView.findViewById(R.id.tv_price_summaryList);
            description = (TextView) itemView.findViewById(R.id.tv_description_summaryList);
            //date = (TextView) itemView.findViewById(R.id.tv_date_summaryList);
        }
    }
}
