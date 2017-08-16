package happyhappyinc.developer.happyexpress.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.maps.DirectionsJSONParser;
import happyhappyinc.developer.happyexpress.models.OrderModel;
import happyhappyinc.developer.happyexpress.network.VolleySingleton;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;
import happyhappyinc.developer.happyexpress.utils.Constants;
import happyhappyinc.developer.happyexpress.utils.FontHelper;
import happyhappyinc.developer.happyexpress.utils.Utils;

public class DetailOrderActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SlideToActView.OnSlideCompleteListener {

    public static OrderModel CURRENT_ORDER;
    private TextView[] mLblsInformation = new TextView[9];
    private SlideToActView[] mSlidesStates = new SlideToActView[4];
    private LinearLayout mContainerBtns, mContainerSlides;
    private Context mContext;
    private boolean isAsocciateRoute = true;
    private PreferencesManager mPref;
    private FloatingActionsMenu mFab;

    /*
    MAPAS
    */

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int REQUEST_CHECK_SETTINGS = 1000;

    /*
    END MAPAS
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_detail_order);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));


        mContext = this;
        mPref = new PreferencesManager(mContext);
        setTitle("PEDIDO # " + CURRENT_ORDER.getId());

        initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        consumeWebServiceValidateStateOrder();

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        getDeviceLocation();
    }

    /*
        AQUÍ EMPIEZA EL CÓDIGO DIFERENTE A MAPA
     */

    private void changeStateSlides(int indice) {
        switch (indice) {
            case 0:
                mSlidesStates[0].setVisibility(View.VISIBLE);
                mSlidesStates[1].setVisibility(View.GONE);
                mSlidesStates[2].setVisibility(View.GONE);
                mSlidesStates[3].setVisibility(View.GONE);
                break;
            case 1:
                mSlidesStates[0].setVisibility(View.GONE);
                mSlidesStates[1].setVisibility(View.VISIBLE);
                mSlidesStates[2].setVisibility(View.GONE);
                mSlidesStates[3].setVisibility(View.GONE);
                break;
            case 2:
                mSlidesStates[0].setVisibility(View.GONE);
                mSlidesStates[1].setVisibility(View.GONE);
                mSlidesStates[2].setVisibility(View.VISIBLE);
                mSlidesStates[3].setVisibility(View.GONE);
                break;
            case 3:
                mSlidesStates[0].setVisibility(View.GONE);
                mSlidesStates[1].setVisibility(View.GONE);
                mSlidesStates[2].setVisibility(View.GONE);
                mSlidesStates[3].setVisibility(View.VISIBLE);
                break;
            case 4:
                mSlidesStates[0].setVisibility(View.GONE);
                mSlidesStates[1].setVisibility(View.GONE);
                mSlidesStates[2].setVisibility(View.GONE);
                mSlidesStates[3].setVisibility(View.GONE);
                mContainerSlides.setVisibility(View.GONE);
                Utils.alertSuccess(mContext, "Felicidades", "Continúa así...").show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
                break;
        }
    }

    private void initComponents() {

        mFab = (FloatingActionsMenu) findViewById(R.id.menufab);

        /* Este código es para poder moverse por el mapa sin que collapse */
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.abl_container);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);

        mContainerBtns = (LinearLayout) findViewById(R.id.ll_container_btns);
        mContainerSlides = (LinearLayout) findViewById(R.id.ll_container_slides);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        int[] ids = {R.id.tv_order_date_detail, R.id.tv_product_name, R.id.tv_ticket_number, R.id.tv_associate_name,
                R.id.tv_address_a, R.id.tv_client_name, R.id.tv_address_c, R.id.tv_markers_text, R.id.tv_clock_text};

        for (int i = 0; i < mLblsInformation.length; i++) {
            mLblsInformation[i] = (TextView) findViewById(ids[i]);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date orderDate = null;
        try {
            orderDate = format.parse(CURRENT_ORDER.getOrder_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mLblsInformation[0].setText(orderDate.getHours() + ":" + orderDate.getMinutes());
        mLblsInformation[1].setText(CURRENT_ORDER.getProduct_name());
        mLblsInformation[2].setText("#" + CURRENT_ORDER.getId());
        mLblsInformation[3].setText(CURRENT_ORDER.getFull_name_associate());
        mLblsInformation[4].setText(CURRENT_ORDER.getAddress_a());
        mLblsInformation[5].setText(CURRENT_ORDER.getFull_name_client());
        mLblsInformation[6].setText(CURRENT_ORDER.getAddress_c());

        int idsSlides[] = {R.id.sb_slide_my_way_associate, R.id.sb_slide_here, R.id.sb_slide_my_way_client, R.id.sb_slide_finish_order};
        for (int i = 0; i < mSlidesStates.length; i++) {
            mSlidesStates[i] = (SlideToActView) findViewById(idsSlides[i]);
            mSlidesStates[i].setOnSlideCompleteListener(this);
        }
    }

    public void chat(View view) {
        ChatActivity.CURRENT_ORDER = CURRENT_ORDER;
        String tagValue = (String) view.getTag();
        switch (tagValue) {
            case "1":
                ChatActivity.DESTINATARIO = CURRENT_ORDER.getId_client();
                ChatActivity.NAME_DESTINATARIO = CURRENT_ORDER.getFull_name_client();
                break;
            case "2":
                ChatActivity.DESTINATARIO = CURRENT_ORDER.getId_associate();
                ChatActivity.NAME_DESTINATARIO = CURRENT_ORDER.getFull_name_associate();
                break;
            case "3":
                // pendiente modificar el servicio para obtener el id de servicio al cliente
                ChatActivity.DESTINATARIO = CURRENT_ORDER.getId_customer_service();
                ChatActivity.NAME_DESTINATARIO = "Servicio al Cliente";
                break;
        }
        startActivity(new Intent(mContext, ChatActivity.class));
        mFab.collapse();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickOrderAccept(View view) {
        consumeWebServiceChangeStateBtns(1, "");
    }

    public void clickOrderDecline(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrderActivity.this);
        LayoutInflater inflater = DetailOrderActivity.this.getLayoutInflater();
        final View viewAlert = inflater.inflate(R.layout.custom_alert_denied_view, null);
        builder.setView(viewAlert);

        builder.create();
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.show();


        Button btnSendOrderDecline = (Button) viewAlert.findViewById(R.id.btn_send_order_decline);
        btnSendOrderDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reasonsDecline = (EditText) viewAlert.findViewById(R.id.et_reasons_decline);
                String reasons = reasonsDecline.getText().toString();
                if (!reasons.equals("")) {
                    consumeWebServiceChangeStateBtns(2, reasons);
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

    private void consumeWebServiceValidateStateOrder() {
        // Hacemos el consumo respectivo del login checkLogin

        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(

                        new JsonObjectRequest(
                                Request.Method.GET,
                                Constants.VALIDATE_STATE_ORDER + "/" + CURRENT_ORDER.getId(),
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        proccesingResponseStateOrders(response);
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

    // Método donde manipulamos los datos que nos retorna el WebService
    private void proccesingResponseStateOrders(JSONObject response) {

        try {
            String state = response.getString("status");

            switch (state) {
                // En este caso la petición fue exitosa y retorna los datos correspondientes
                case "1":

                    // BOTONES VISIBLES Y SLIDES OCULTOS
                    mContainerBtns.setVisibility(View.VISIBLE);
                    mContainerSlides.setVisibility(View.GONE);


                    break;
                // En este caso la petición se realizó, pero no fue exitosa
                case "2":

                    // SLIDES VISIBLES Y BOTONES OCULTOS
                    mContainerBtns.setVisibility(View.GONE);
                    mContainerSlides.setVisibility(View.VISIBLE);

                    String order_state = response.getString("slide_state");
                    switch (order_state) {
                        case "-1":
                            changeStateSlides(0);
                            break;
                        case "13":
                            changeStateSlides(1);
                            break;
                        case "14":
                            changeStateSlides(2);
                            break;
                        case "15":
                            changeStateSlides(3);
                            break;
                        case "16":
                            changeStateSlides(4);
                            break;
                    }

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void consumeWebServiceChangeStateSlides(final int indice) {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        // Guardamos en un HashMap los 3 parámetros necesarios para hacer el consumo del login
        map.put("id_pedido", String.valueOf(CURRENT_ORDER.getId()));
        String idEstado = "";
        switch (indice) {
            case 0:
                idEstado = "" + 13;
                break;
            case 1:
                idEstado = "" + 14;
                break;
            case 2:
                idEstado = "" + 15;
                break;
            case 3:
                idEstado = "" + 16;
                break;
        }
        map.put("id_estado", idEstado);
        map.put("fecha", Utils.getCurrentDate());
        map.put("hora", Utils.getCurrentTime());

        // Armamos el JSONObject con los datos que serán enviados al servidor
        final JSONObject jobject = new JSONObject(map);

        // Hacemos el consumo respectivo del login checkLogin
        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(

                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constants.CHANGE_ORDER_STATE,
                                jobject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        proccesingResponseSlides(response, indice);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Utils.alertError(mContext, "Verifica tu conexión a internet " + error.getMessage()).show();
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

    // Método donde manipulamos los datos que nos retorna el WebService
    private void proccesingResponseSlides(JSONObject response, int indice) {

        try {
            String state = response.getString("status");

            switch (state) {
                // En este caso la petición fue exitosa y retorna los datos correspondientes
                case "1":

                    // aqui oculto un linerlayout y muestro el otro linearlayout de los slides
                    consumeWebServiceValidateStateOrder();


                    break;
                // En este caso la petición se realizó, pero no fue exitosa
                case "2":

                    // Como no hubo éxito, mostramos al usuario cual fue el error que hubo!!!
                    Utils.alertError(mContext, response.getString("message")).show();
                    mSlidesStates[indice].resetSlider();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void consumeWebServiceChangeStateBtns(int flag, String reasons) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        // Guardamos en un HashMap los 3 parámetros necesarios para hacer el consumo del login
        map.put("id_pedido", String.valueOf(CURRENT_ORDER.getId()));
        map.put("id_domiciliario", mPref.checkId());
        map.put("current_datetime", Utils.getCurrentDateTime());
        if (flag == 2) {
            map.put("id_estado", "2");
            map.put("reasons", reasons);
        }

        // Armamos el JSONObject con los datos que serán enviados al servidor
        final JSONObject jobject = new JSONObject(map);

        // Hacemos el consumo respectivo del login checkLogin
        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.PUT,
                                Constants.CHANGE_ORDER_STATE,
                                jobject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        proccesingResponse(response, jobject.length());
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Utils.alertError(mContext, "Verifica tu conexión a internet ").show();
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

    // Método donde manipulamos los datos que nos retorna el WebService
    private void proccesingResponse(JSONObject response, int flag) {

        try {
            String state = response.getString("status");

            switch (state) {
                // En este caso la petición fue exitosa y retorna los datos correspondientes
                case "1":

                    // aqui oculto un linerlayout y muestro el otro linearlayout de los slides
                    if (flag == 3) {
                        // NEW Toca volver a llamar al servicio para verificar el estado del pedido
                        Utils.alertSuccess(mContext, "GOOD", response.getString("message")).show();
                        consumeWebServiceValidateStateOrder();

                    } else {
                        finish();
                    }

                    break;
                // En este caso la petición se realizó, pero no fue exitosa
                case "2":

                    // Como no hubo éxito, mostramos al usuario cual fue el error que hubo!!!
                    Utils.alertError(mContext, response.getString("message")).show();

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*
        AQUÍ FINALIZA EL CÓDIGO DIFERENTE A MAPA
     */

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!mLocationPermissionGranted) {
            alertPermissionGPS();
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        drawRoute(
                new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                new LatLng(Double.parseDouble(CURRENT_ORDER.getLatitude_a()), Double.parseDouble(CURRENT_ORDER.getLongitude_a())),
                true
        );

        drawRoute(
                new LatLng(Double.parseDouble(CURRENT_ORDER.getLatitude_a()), Double.parseDouble(CURRENT_ORDER.getLongitude_a())),
                new LatLng(Double.parseDouble(CURRENT_ORDER.getLatitude_c()), Double.parseDouble(CURRENT_ORDER.getLongitude_c())),
                false
        );
        /*pintarRutaAsociado();
        pintarRutaCliente();*/
    }

    public void drawRoute(LatLng origen, LatLng destino, boolean flag) {
        LatLng origin = origen;
        LatLng dest = destino;

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(dest);

        if (flag) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple));
        } else {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_orange));
        }

        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        getDeviceLocation();
        updateLocationUI();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        mLastKnownLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("Estás aquí")
                    .position(new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()))
                    .snippet("Comienza tu ruta!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));

            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

        } else {
            /*Log.d("TESMAP", "Entra al else");
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);*/
        }
    }

    private void alertPermissionGPS() {
        /*PARA ALERTA DE PRENDER GPS*/
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        /*SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());*/

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    DetailOrderActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });

        /*TERMINA ALERTA DE PRENDER GPS*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //LOGIC
                        // Turn on the My Location layer and the related control on the map.
                        updateLocationUI();

                        // Get the current location of the device and set the position of the map.
                        getDeviceLocation();

                        drawRoute(
                                new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                                new LatLng(Double.parseDouble(CURRENT_ORDER.getLatitude_a()), Double.parseDouble(CURRENT_ORDER.getLongitude_a())),
                                true
                        );

                        drawRoute(
                                new LatLng(Double.parseDouble(CURRENT_ORDER.getLatitude_a()), Double.parseDouble(CURRENT_ORDER.getLongitude_a())),
                                new LatLng(Double.parseDouble(CURRENT_ORDER.getLatitude_c()), Double.parseDouble(CURRENT_ORDER.getLongitude_c())),
                                false
                        );
                        break;
                    case Activity.RESULT_CANCELED:
                        alertPermissionGPS();
                }
                break;
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onSlideComplete(@NotNull SlideToActView slideToActView) {
        switch (slideToActView.getId()) {
            case R.id.sb_slide_my_way_associate:
                consumeWebServiceChangeStateSlides(0);
                break;
            case R.id.sb_slide_here:
                consumeWebServiceChangeStateSlides(1);
                break;
            case R.id.sb_slide_my_way_client:
                consumeWebServiceChangeStateSlides(2);
                break;
            case R.id.sb_slide_finish_order:
                consumeWebServiceChangeStateSlides(3);
                break;
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... url) {

            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {


            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(5);
                if (isAsocciateRoute) {
                    lineOptions.color(Color.rgb(82, 13, 255));
                    isAsocciateRoute = false;
                } else {
                    lineOptions.color(Color.rgb(255, 60, 0));
                    isAsocciateRoute = true;
                }
                lineOptions.geodesic(true);
            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
