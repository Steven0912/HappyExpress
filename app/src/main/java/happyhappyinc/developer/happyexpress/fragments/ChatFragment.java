package happyhappyinc.developer.happyexpress.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.adapters.ChatAdapter;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private Context mContext;
    private ChatAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    public static List<String> list = new ArrayList<>();
    private TextView mMessage;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mContext = getActivity();
        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        mMessage = (TextView) v.findViewById(R.id.et_message_chat);

        mListRecyclerView = (RecyclerView) v.findViewById(R.id.rv_chat_list);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ChatAdapter(mContext);
    }

}
