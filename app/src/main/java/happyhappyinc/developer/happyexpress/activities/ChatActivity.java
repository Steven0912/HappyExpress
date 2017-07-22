package happyhappyinc.developer.happyexpress.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.adapters.ChatAdapter;
import happyhappyinc.developer.happyexpress.models.OrderModel;

public class ChatActivity extends AppCompatActivity {

    public static OrderModel CURRENT_ORDER;
    private Context mContext;
    private ChatAdapter mAdapter;
    private RecyclerView mListRecyclerView;
    public static List<String> list = new ArrayList<>();
    private TextView mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mContext = this;
        initiComponents();
        setUpToolbar();

        setTitle(CURRENT_ORDER.getFull_name_client());
    }

    private void initiComponents() {
        mMessage = (TextView) findViewById(R.id.et_message_chat);

        mListRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_list);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ChatAdapter(mContext);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }*/
    }

    public void sendMessage(View view){

    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
