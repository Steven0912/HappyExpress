package happyhappyinc.developer.happyexpress.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import happyhappyinc.developer.happyexpress.R;

/**
 * Created by Steven on 19/07/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<String> mChatList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public ChatAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setChatList(List<String> list) {
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

        String currentMessage = mChatList.get(position);

        holder.mMessage.setText(currentMessage);

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
