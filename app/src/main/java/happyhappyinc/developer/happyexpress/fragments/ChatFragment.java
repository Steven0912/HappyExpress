package happyhappyinc.developer.happyexpress.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import happyhappyinc.developer.happyexpress.adapters.ChatAdapter;
import happyhappyinc.developer.happyexpress.models.BitacoraTicketModel;
import happyhappyinc.developer.happyexpress.network.VolleySingleton;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;
import happyhappyinc.developer.happyexpress.utils.Constants;
import happyhappyinc.developer.happyexpress.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    public static final String MENSAJE = "MENSAJE";

    private Context mContext;
    private ChatAdapter mAdapter;
    private Gson gson = new Gson();
    private RecyclerView mListRecyclerView;
    private PreferencesManager mPref;
    private TextView mMessageChat;
    private ImageView mBtnSend;
    private BroadcastReceiver mBroadcastReceiver;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mContext = getActivity();
        mPref = new PreferencesManager(mContext);
        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        mListRecyclerView = (RecyclerView) v.findViewById(R.id.rv_chat_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        mListRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new ChatAdapter(mContext);

        mMessageChat = (TextView) v.findViewById(R.id.et_message_chat);
        mBtnSend = (ImageView) v.findViewById(R.id.btn_send_message);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadChat("");
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, new IntentFilter(MENSAJE));
        loadChat("");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
    }

    public void sendMessage() {
        String msg = mMessageChat.getText().toString();
        if (msg.equals("")) {
            mMessageChat.setError("Escribe algo!");
        } else {
            loadChat(msg);
            mMessageChat.setText("");
        }
    }

    private void loadChat(String msg) {

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        // Guardamos en un HashMap los 3 parámetros necesarios para hacer el consumo del login
        map.put("id_destino", mPref.checkId());
        map.put("fecha_mensaje", Utils.getCurrentDateTime());
        if (!msg.equals("")) {
            map.put("mensaje", msg);
        } else {
            map.put("mensaje", "happy_no_chat");
        }

        // Armamos el JSONObject con los datos que serán enviados al servidor
        JSONObject jobject = new JSONObject(map);

        VolleySingleton.
                getInstance(mContext).
                addToRequestQueue(

                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constants.CHATWITHOUTORDER,
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
}
