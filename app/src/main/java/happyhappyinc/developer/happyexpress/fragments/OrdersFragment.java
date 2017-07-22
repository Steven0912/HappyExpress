package happyhappyinc.developer.happyexpress.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.adapters.OrderAdapter;
import happyhappyinc.developer.happyexpress.models.OrderModel;
import happyhappyinc.developer.happyexpress.network.VolleySingleton;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;
import happyhappyinc.developer.happyexpress.utils.Constants;
import happyhappyinc.developer.happyexpress.utils.FontHelper;
import happyhappyinc.developer.happyexpress.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private OrderAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    private Gson gson = new Gson();
    private PreferencesManager mPref;
    private ImageView mImageNetwork;
    private TextView mLblNetwork;
    private SwipeRefreshLayout mSwipe;


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        FontHelper.setCustomTypeface(v.findViewById(R.id.view_root));

        // Inicializamos los componentes
        mContext = getActivity();
        mPref = new PreferencesManager(mContext);
        Log.d("TESMAP", "token: " + mPref.getToken());

        initComponents(v);

        mSwipe.setColorSchemeResources(R.color.colorPrimary);
        mSwipe.setOnRefreshListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrdersList();
    }

    // Inicializando los componentes de la vista
    private void initComponents(View v) {
        mListRecyclerView = (RecyclerView) v.findViewById(R.id.rv_orders_list);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mImageNetwork = (ImageView) v.findViewById(R.id.iv_networkoff);
        mLblNetwork = (TextView) v.findViewById(R.id.tv_networkoff);
        mSwipe = (SwipeRefreshLayout) v.findViewById(R.id.sr_webservice);
    }

    // Método que hace el consumo del servicio y trae los pedidos asignados al domiciliario
    private void loadOrdersList() {

        String ruta = Constants.ORDERS_LIST + "/" + mPref.checkId();

        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ruta,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        proccesingResponse(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Utils.alertError(mContext, "Verifica tu conexión a internet").show();
                                        mImageNetwork.setImageResource(R.drawable.networkoff);
                                        mLblNetwork.setText("No hay conexión a Internet");
                                        mImageNetwork.setVisibility(View.VISIBLE);
                                        mLblNetwork.setVisibility(View.VISIBLE);
                                        mListRecyclerView.setVisibility(View.GONE);
                                        if (mSwipe.isRefreshing()) {
                                            mSwipe.setRefreshing(false);
                                        }
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

    // Método donde manipulamos los datos que retorna el servicio
    private void proccesingResponse(JSONObject response) {

        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }

        try {
            String state = response.getString("state");

            switch (state) {
                case "1":

                    JSONArray datas = response.getJSONArray("orderslist");

                    // Parsear con Gson
                    OrderModel[] orderslist = gson.fromJson(datas.toString(), OrderModel[].class);

                    mAdapter = new OrderAdapter(mContext);
                    mAdapter.setOrderList(Arrays.asList(orderslist));
                    mListRecyclerView.setAdapter(mAdapter);

                    mImageNetwork.setVisibility(View.GONE);
                    mLblNetwork.setVisibility(View.GONE);
                    mListRecyclerView.setVisibility(View.VISIBLE);

                    break;
                case "2":

                    mImageNetwork.setImageResource(R.drawable.empty);
                    mImageNetwork.setVisibility(View.VISIBLE);
                    mLblNetwork.setVisibility(View.VISIBLE);
                    mListRecyclerView.setVisibility(View.GONE);
                    mLblNetwork.setText("No tienes pedidos asignados");

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        loadOrdersList();
    }
}
