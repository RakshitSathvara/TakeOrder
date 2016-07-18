package in.vaksys.takeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.takeorder.R;
import in.vaksys.takeorder.adapters.CheckListAdapter;
import in.vaksys.takeorder.dbPojo.AddOrder;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/27/2016.
 */
public class CheckListFragment extends Fragment {

    @Bind(R.id.recyclerCheckList)
    RecyclerView recyclerCheckList;
    private Realm mRealm;
    //private RealmResults<Temp> tempRealmResults;
    private RealmResults<AddOrder> tempRealmResults;
    private CheckListAdapter checkListAdapter;

    public static CheckListFragment newInstance(int index) {
        CheckListFragment fragment = new CheckListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_list, container, false);
        ButterKnife.bind(this, rootView);
        mRealm = Realm.getDefaultInstance();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerCheckList.setLayoutManager(manager);

        //tempRealmResults = mRealm.where(Temp.class).equalTo("flag", true).findAll();
        tempRealmResults = mRealm.where(AddOrder.class).equalTo("flag", true).findAll();
        Log.e("CHECKLIS", "onCreateView: " + tempRealmResults.size());

        checkListAdapter = new CheckListAdapter(getActivity(), tempRealmResults);
        recyclerCheckList.setHasFixedSize(true);
        recyclerCheckList.setItemAnimator(new DefaultItemAnimator());
        recyclerCheckList.setNestedScrollingEnabled(false);
        recyclerCheckList.setAdapter(checkListAdapter);

//        tempRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Temp>>() {
//            @Override
//            public void onChange(RealmResults<Temp> element) {
//                checkListAdapter.notifyDataSetChanged();
//            }
//        });

        tempRealmResults.addChangeListener(new RealmChangeListener<RealmResults<AddOrder>>() {
            @Override
            public void onChange(RealmResults<AddOrder> element) {
                checkListAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
