package in.vaksys.takeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.takeorder.R;
import in.vaksys.takeorder.adapters.OrderListAdapter;
import in.vaksys.takeorder.adapters.SpinnerTextAdapter;
import in.vaksys.takeorder.dbPojo.AddContact;
import in.vaksys.takeorder.dbPojo.AddOrder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/27/2016.
 */
public class OrderListFragment extends Fragment {


    @Bind(R.id.orderRecyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.sp_customer)
    AppCompatSpinner spCustomer;

    private OrderListAdapter orderListAdapter;
    private Realm mRealm;
    private RealmResults<AddOrder> addOrderList;

    public static OrderListFragment newInstance(int index) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_list, container, false);

        ButterKnife.bind(this, rootView);

        mRealm = Realm.getDefaultInstance();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);

        /*addOrderList = mRealm.where(AddOrder.class).findAll();
        orderListAdapter = new OrderListAdapter(getActivity(), addOrderList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(orderListAdapter);

        addOrderList.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
            @Override
            public void onChange(RealmResults<AddOrder> element) {
                orderListAdapter.notifyDataSetChanged();
            }
        });*/


        RealmResults<AddContact> addOrders = mRealm.where(AddContact.class).findAll();

        final SpinnerTextAdapter sp = new SpinnerTextAdapter(getActivity(), addOrders);
        spCustomer.setAdapter(sp);

        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                addOrderList = mRealm.where(AddOrder.class).equalTo("buyerName", adapterView.getSelectedItem().toString()).findAll();
                orderListAdapter = new OrderListAdapter(getActivity(), addOrderList);
                recyclerview.setHasFixedSize(true);
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setNestedScrollingEnabled(false);
                recyclerview.setAdapter(orderListAdapter);

                addOrderList.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
                    @Override
                    public void onChange(RealmResults<AddOrder> element) {
                        orderListAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

//    @Override
//    public void onDestroyView() {
//
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
////
//    @Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//
//    }
//
//    @Subscribe
//    public void onEventBackgroundThread(MessageSec messageCar) {
//        Log.e("Sp datata", messageCar.getMsg());
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onPause() {
//        EventBus.getDefault().unregister(this);
//
//        super.onPause();
//
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
}
