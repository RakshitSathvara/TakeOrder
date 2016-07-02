package in.vaksys.takeorder.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import in.vaksys.takeorder.R;
import in.vaksys.takeorder.fragments.AddContactFragment;
import in.vaksys.takeorder.fragments.AddOrderFragment;
import in.vaksys.takeorder.fragments.CheckListFragment;
import in.vaksys.takeorder.fragments.ContactListFragment;
import in.vaksys.takeorder.fragments.FragmentDrawer;
import in.vaksys.takeorder.fragments.OrderListFragment;
import in.vaksys.takeorder.fragments.SummaryOfOrderFragment;
import in.vaksys.takeorder.model.Message;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private FragmentManager fragmentManager;
    private ContactListFragment contactListFragment;
    private AddContactFragment addContactFragment;
    private OrderListFragment orderListFragment;
    private AddOrderFragment addOrderFragment;
    private SummaryOfOrderFragment summaryOfOrderFragment;
    private CheckListFragment checkListFragment;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(0);
        contactListFragment = ContactListFragment.newInstance(0);
        addContactFragment = AddContactFragment.newInstance(1);
        orderListFragment = OrderListFragment.newInstance(2);
        addOrderFragment = AddOrderFragment.newInstance(3);
        summaryOfOrderFragment = SummaryOfOrderFragment.newInstance(4);
        checkListFragment = CheckListFragment.newInstance(5);

        if (!value.equalsIgnoreCase("")) {
            Log.e("dhfj", "dfkjd");

        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);

//        contactListFragment = ContactListFragment.newInstance(0);
//        addContactFragment = AddContactFragment.newInstance(1);
//        orderListFragment = OrderListFragment.newInstance(2);
//        addOrderFragment = AddOrderFragment.newInstance(3);
//        summaryOfOrderFragment = SummaryOfOrderFragment.newInstance(4);
//        checkListFragment = CheckListFragment.newInstance(5);

    }

    private void displayView(int position) {
//        Fragment fragment = null;
//        String title = getString(R.string.app_name);
//        switch (position) {
//            case 0:
//                fragment = new ContactListFragment();
//                title = getString(R.string.contact);
//                break;
//            case 1:
//                fragment = new AddContactFragment();
//                title = getString(R.string.add_contact);
//                break;
//            case 2:
//                fragment = new OrderListFragment();
//                title = getString(R.string.order);
//                break;
//            case 3:
//                fragment = new AddOrderFragment();
//                title = getString(R.string.add_order);
//                break;
//            case 4:
//                fragment = new SummaryOfOrderFragment();
//                title = getString(R.string.summary);
//                break;
//            case 5:
//                fragment = new CheckListFragment();
//                title = getString(R.string.check);
//                break;
//            default:
//                break;
//        }
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//
//            // set the toolbar title
//            getSupportActionBar().setTitle(title);
//        }
        contactListFragment = ContactListFragment.newInstance(0);
        addContactFragment = AddContactFragment.newInstance(1);
        orderListFragment = OrderListFragment.newInstance(2);
        addOrderFragment = AddOrderFragment.newInstance(3);
        summaryOfOrderFragment = SummaryOfOrderFragment.newInstance(4);
        checkListFragment = CheckListFragment.newInstance(5);
        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, contactListFragment)
                    .commit();
        }

        if (position == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, addContactFragment)
                    .commit();
        }

        if (position == 2) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, orderListFragment)
                    .commit();
        }

        if (position == 3) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, addOrderFragment)
                    .commit();
        }

        if (position == 4) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, summaryOfOrderFragment)
                    .commit();
        }

        if (position == 5) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, checkListFragment)
                    .commit();
        }


    }

    @Subscribe
    public void onEvent(Message messageCar) {
        Log.e("Main datata", messageCar.getMsg());
        value = messageCar.getMsg();
        Log.e("VALUE", "onEvent: " + value);
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, orderListFragment)
                .commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
