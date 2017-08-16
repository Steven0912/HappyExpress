package happyhappyinc.developer.happyexpress.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import happyhappyinc.developer.happyexpress.adapters.ChatAdapter;
import happyhappyinc.developer.happyexpress.models.BitacoraTicketModel;
import happyhappyinc.developer.happyexpress.models.OrderModel;
import happyhappyinc.developer.happyexpress.network.VolleySingleton;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;
import happyhappyinc.developer.happyexpress.utils.Constants;
import happyhappyinc.developer.happyexpress.utils.Utils;

public class ChatActivity extends AppCompatActivity {

    public static final String MENSAJE = "MENSAJE";

    public static OrderModel CURRENT_ORDER;
    public static int DESTINATARIO;
    public static String NAME_DESTINATARIO;
    private Context mContext;
    private ChatAdapter mAdapter;
    private Gson gson = new Gson();
    private RecyclerView mListRecyclerView;
    private PreferencesManager mPref;
    private TextView mMessageChat;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = this;
        mPref = new PreferencesManager(mContext);
        initiComponents();
        setUpToolbar();

        setTitle(NAME_DESTINATARIO);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, new IntentFilter(MENSAJE));
        loadChat("");
    }

    private void loadChat(String msg) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        // Guardamos en un HashMap los 3 parámetros necesarios para hacer el consumo del login
        map.put("id_origen", mPref.checkId());
        map.put("id_destino", String.valueOf(DESTINATARIO));
        map.put("fecha_mensaje", Utils.getCurrentDateTime());
        if (!msg.equals("")) {
            map.put("mensaje", msg);
        } else {
            map.put("mensaje", "happy_no_chat");
        }
        map.put("id_pedido", String.valueOf(CURRENT_ORDER.getId()));

        // Armamos el JSONObject con los datos que serán enviados al servidor
        JSONObject jobject = new JSONObject(map);

        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(

                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constants.CHAT,
                                jobject,

                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        proccesingResponse(response);
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

    // Método donde manipulamos los datos que retorna el servicio
    private void proccesingResponse(JSONObject response) {

        try {
            String state = response.getString("state");

            switch (state) {
                case "1":

                    JSONArray datas = response.getJSONArray("messagelist");

                    // Parsear con Gson
                    BitacoraTicketModel[] chatlist = gson.fromJson(datas.toString(), BitacoraTicketModel[].class);

                    mAdapter = new ChatAdapter(mContext);
                    mAdapter.setChatList(Arrays.asList(chatlist));
                    mListRecyclerView.setAdapter(mAdapter);

                    /*mImageNetwork.setVisibility(View.GONE);
                    mLblNetwork.setVisibility(View.GONE);
                    mListRecyclerView.setVisibility(View.VISIBLE);*/

                    break;
                case "2":

                    Utils.alertError(mContext, response.getString("message")).show();

                    /*mImageNetwork.setImageResource(R.drawable.empty);
                    mImageNetwork.setVisibility(View.VISIBLE);
                    mLblNetwork.setVisibility(View.VISIBLE);
                    mListRecyclerView.setVisibility(View.GONE);
                    mLblNetwork.setText("No tienes pedidos asignados");*/

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiComponents() {
        mListRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        mListRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatAdapter(mContext);

        mMessageChat = (TextView) findViewById(R.id.et_message_chat);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadChat("");
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
    }


    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendMessage(View view) {
        String msg = mMessageChat.getText().toString();
        if (msg.equals("")) {
            mMessageChat.setError("Escribe algo!");
        } else {
            loadChat(msg);
            mMessageChat.setText("");
        }
    }
}
