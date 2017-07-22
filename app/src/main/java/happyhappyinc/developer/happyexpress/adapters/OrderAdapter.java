package happyhappyinc.developer.happyexpress.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import happyhappyinc.developer.happyexpress.R;
import happyhappyinc.developer.happyexpress.activities.DetailOrderActivity;
import happyhappyinc.developer.happyexpress.models.OrderModel;
import happyhappyinc.developer.happyexpress.utils.FontHelper;

/**
 * Created by Steven on 04/07/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    // Variables globales para inicializar nuestra grilla de puntos de acceso
    private List<OrderModel> mOrderList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public OrderAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setOrderList(List<OrderModel> list) {
        mOrderList = list;
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.custom_item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final OrderModel currentOrder = mOrderList.get(position);

        //holder.iv_category.setImageBitmap();
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date orderDate = null;
        try {
            orderDate = format.parse(currentOrder.getOrder_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_order_date.setText(orderDate.getHours() + ":" + orderDate.getMinutes());
        holder.tv_product_name.setText(currentOrder.getProduct_name());
        holder.tv_ticket_number.setText("#" + currentOrder.getId());
        holder.tv_address_a.setText(currentOrder.getAddress_a());
        holder.tv_address_c.setText(currentOrder.getAddress_c());
        //api google maps holder.tv_markers_text.setText(currentOrder.get);
        //api google maps holder.tv_clock_text.setText(currentOrder.get);
        holder.view_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailOrderActivity.CURRENT_ORDER = currentOrder;
                mContext.startActivity(new Intent(mContext, DetailOrderActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_category;
        private CardView view_root;
        private TextView tv_order_date, tv_product_name, tv_ticket_number, tv_address_a, tv_address_c, tv_markers_text, tv_clock_text;

        public OrderViewHolder(View itemView) {
            super(itemView);

            FontHelper.setCustomTypeface(itemView.findViewById(R.id.view_root));

            view_root = (CardView) itemView.findViewById(R.id.view_root);
            iv_category = (ImageView) itemView.findViewById(R.id.iv_category);
            tv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_ticket_number = (TextView) itemView.findViewById(R.id.tv_ticket_number);
            tv_address_a = (TextView) itemView.findViewById(R.id.tv_address_a);
            tv_address_c = (TextView) itemView.findViewById(R.id.tv_address_c);
            tv_markers_text = (TextView) itemView.findViewById(R.id.tv_markers_text);
            tv_clock_text = (TextView) itemView.findViewById(R.id.tv_clock_text);
        }
    }
}
