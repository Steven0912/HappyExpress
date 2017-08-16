package happyhappyinc.developer.happyexpress.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.models.BitacoraTicketModel;
import happyhappyinc.developer.happyexpress.preferences.PreferencesManager;

/**
 * Created by Steven on 19/07/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<BitacoraTicketModel> mChatList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private PreferencesManager mPref;

    public ChatAdapter(Context context) {
        mContext = context;
        mPref = new PreferencesManager(mContext);
        mInflater = LayoutInflater.from(mContext);
    }

    public void setChatList(List<BitacoraTicketModel> list) {
        mChatList = list;
        notifyDataSetChanged();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.custom_item_chat, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        BitacoraTicketModel currentMessage = mChatList.get(position);

        if (Integer.parseInt(mPref.checkId()) == currentMessage.getId_usuario()) {
            // aqui se coloca el background azulito que edgar no envió
            holder.mMessage.setBackgroundResource(R.drawable.background_message_domi);
            holder.mMessage.setTextColor(Color.WHITE);
        } else {
            holder.mMessage.setBackgroundResource(R.drawable.background_message_client);
            holder.mMessage.setTextColor(mContext.getResources().getColor(R.color.ak_text));
        }
        holder.mMessage.setText(currentMessage.getMsg());
        holder.mHourMessage.setText(currentMessage.getFecha());

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView mMessage;
        private TextView mHourMessage;

        public ChatViewHolder(View itemView) {
            super(itemView);

            mMessage = (TextView) itemView.findViewById(R.id.tv_message_chat);
            mHourMessage = (TextView) itemView.findViewById(R.id.tv_hour_message);
        }
    }
}
