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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.fragments.ChatFragment;
import happyhappyinc.developer.happyexpress.fragments.DemoFragment;
import happyhappyinc.developer.happyexpress.fragments.OrdersFragment;
import happyhappyinc.developer.happyexpress.models.UserModel;
import happyhappyinc.developer.happyexpress.network.VolleySingleton;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;
import happyhappyinc.developer.happyexpress.utils.Constants;
import happyhappyinc.developer.happyexpress.utils.FontHelper;
import happyhappyinc.developer.happyexpress.utils.Utils;

public class MainActivity extends AppCompatActivity {

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

    private boolean mChecked = true;

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

        //setTitle(getResources().getString(R.string.str_item_orders));
        mFragManager.beginTransaction().replace(R.id.contentFragment, new OrdersFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getState();
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

                    //setTitle(menuItem.getTitle());
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

        mEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChecked) {
                    changeAvailable(1);
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
                            alertDialog.dismiss();
                            changeAvailable(2);

                        /*EditText reasonsDecline = (EditText) viewAlert.findViewById(R.id.et_reasons_disable);
                        String reasons = reasonsDecline.getText().toString();
                        if (!reasons.equals("")) {
                            //consumeWebServiceChangeStateBtns(2, reasons);
                            mEnabled.setBackgroundResource(R.drawable.btn_disable_enable);
                            alertDialog.dismiss();

                            changeAvailable(2);
                        } else {
                            reasonsDecline.setError("Diligencia los motivos!!!");
                        }*/

                        }
                    });

                    Button btnCloseAlert = (Button) viewAlert.findViewById(R.id.btn_close_alert_denied);
                    btnCloseAlert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getState();
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });
        //mEnabled.setOnCheckedChangeListener(this);

        mFragManager = getSupportFragmentManager();
    }

    private void changeAvailable(final int state) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        // Guardamos en un HashMap los 3 parámetros necesarios para hacer el consumo del login
        map.put("id_usuario", mPref.checkId());
        map.put("fecha", Utils.getCurrentDateTime());
        map.put("estado", "" + state);

        // Armamos el JSONObject con los datos que serán enviados al servidor
        JSONObject jobject = new JSONObject(map);
        // Hacemos el consumo respectivo del login checkLogin

        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(

                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constants.DISABLEDELIVERY,
                                jobject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // nothing
                                        processingResponse(response, 1);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Utils.alertError(mContext, "Verifica tu conexión a internet").show();
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorizationh", Utils.md5("H*2017*" + Utils.getDay()));
                                return params;
                            }
                        }
                );
    }

    private void processingResponse(JSONObject response, int flag) {
        try {
            String state = response.getString("state");

            switch (state) {
                case "1":
                    // TODO Here
                    if (flag == 1) {
                        changeFragment(new OrdersFragment());
                        mNavigationDrawer.setCheckedItem(R.id.itemOrders);
                    }

                    changeBtnState(response.getString("estado"));
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeBtnState(String estado) {
        if (Integer.parseInt(estado) == 1) {
            mChecked = true;
            mEnabled.setBackgroundResource(R.drawable.btn_enable_disable);
            mEnabled.setChecked(true);
        } else if (Integer.parseInt(estado) == 2) {
            mChecked = false;
            mEnabled.setBackgroundResource(R.drawable.btn_disable_enable);
            mEnabled.setChecked(false);
        }
    }

    private void getState() {

        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(

                        new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.GETSTATE_DELIVERY + "/" + mPref.checkId(),
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // nothing
                                        processingResponse(response, 2);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Utils.alertError(mContext, "Verifica tu conexión a internet").show();
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorizationh", Utils.md5("H*2017*" + Utils.getDay()));
                                return params;
                            }
                        }
                );
    }
}
