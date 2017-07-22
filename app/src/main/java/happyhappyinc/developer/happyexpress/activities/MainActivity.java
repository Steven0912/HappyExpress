package happyhappyinc.developer.happyexpress.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.fragments.ChatFragment;
import happyhappyinc.developer.happyexpress.fragments.DemoFragment;
import happyhappyinc.developer.happyexpress.fragments.OrdersFragment;
import happyhappyinc.developer.happyexpress.models.UserModel;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;
import happyhappyinc.developer.happyexpress.utils.FontHelper;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static UserModel SESSION_USER;
    private static FragmentManager mFragManager;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationDrawer;
    private TextView name;
    private Context mContext;
    private PreferencesManager mPref;
    private ActionBarDrawerToggle mToggle;
    private ToggleButton mEnabled;

    public static void changeFragment(Fragment f) {
        mFragManager.beginTransaction().replace(R.id.contentFragment, f).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        mContext = this;
        mPref = new PreferencesManager(mContext);
        initComponents();
        setUpToolbar();
        setUpNavDrawer();

        setTitle(getResources().getString(R.string.str_item_orders));
        mFragManager.beginTransaction().replace(R.id.contentFragment, new OrdersFragment()).commit();
    }

    private void setUpNavDrawer() {

        mNavigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment fragment = null;
                Class fragmentClass = null;

                int[] idItems = {R.id.itemOrders, R.id.itemChat, R.id.itemDemo, R.id.itemLogout};
                Class[] classFraments = {OrdersFragment.class, ChatFragment.class, DemoFragment.class, null};

                for (int i = 0; i < idItems.length; i++) {

                    if (idItems[i] == id) {
                        if (i == 3) {
                            mPref.logout();
                            mPref.changeActivity(MainActivity.this, LoginActivity.class);
                        } else {
                            fragmentClass = classFraments[i];
                        }
                    }

                }

                if (fragmentClass != null) {
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    setTitle(menuItem.getTitle());
                    changeFragment(fragment);

                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });

        mToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_draw_open,
                R.string.navigation_draw_close);

        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }

    private void initComponents() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.view_root);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mNavigationDrawer = (NavigationView) findViewById(R.id.navDrawer);
        View header = mNavigationDrawer.getHeaderView(0);
        FontHelper.setCustomTypeface(header.findViewById(R.id.tv_full_name_delivery));
        name = (TextView) header.findViewById(R.id.tv_full_name_delivery);
        name.setText(mPref.checkName());
        mEnabled = (ToggleButton) header.findViewById(R.id.tb_disable_enable);
        mEnabled.setChecked(true);
        mEnabled.setOnCheckedChangeListener(this);

        mFragManager = getSupportFragmentManager();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mEnabled.setBackgroundResource(R.drawable.btn_enable_disable);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            final View viewAlert = inflater.inflate(R.layout.custom_alert_disable, null);
            builder.setView(viewAlert);

            builder.create();
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.show();


            Button btnSendDisable = (Button) viewAlert.findViewById(R.id.btn_send_disable);
            btnSendDisable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText reasonsDecline = (EditText) viewAlert.findViewById(R.id.et_reasons_disable);
                    String reasons = reasonsDecline.getText().toString();
                    if (!reasons.equals("")) {
                        //consumeWebServiceChangeStateBtns(2, reasons);
                        mEnabled.setBackgroundResource(R.drawable.btn_disable_enable);
                        alertDialog.dismiss();
                    } else {
                        reasonsDecline.setError("Diligencia los motivos!!!");
                    }
                }
            });

            Button btnCloseAlert = (Button) viewAlert.findViewById(R.id.btn_close_alert_denied);
            btnCloseAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
    }
}
