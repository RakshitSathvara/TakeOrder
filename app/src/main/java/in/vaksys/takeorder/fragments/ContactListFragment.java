package in.vaksys.takeorder.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.takeorder.R;
import in.vaksys.takeorder.adapters.ContactListAdapter;
import in.vaksys.takeorder.dbPojo.AddContact;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by dell980 on 6/27/2016.
 */
public class ContactListFragment extends Fragment {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    private ContactListAdapter contactListAdapter;
    private Realm mRealm;
    private RealmResults<AddContact> addContactsList;

    public static ContactListFragment newInstance(int index) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ButterKnife.bind(this, rootView);

        mRealm = Realm.getDefaultInstance();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);

        addContactsList = mRealm.where(AddContact.class).findAll();
        contactListAdapter = new ContactListAdapter(getActivity(), addContactsList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(contactListAdapter);

        addContactsList.addChangeListener(new RealmChangeListener<RealmResults<AddContact>>() {
            @Override
            public void onChange(RealmResults<AddContact> element) {
                contactListAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

}
