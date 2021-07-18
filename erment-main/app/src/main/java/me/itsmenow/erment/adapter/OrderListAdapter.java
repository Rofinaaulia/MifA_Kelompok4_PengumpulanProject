package me.itsmenow.erment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import me.itsmenow.erment.R;
import me.itsmenow.erment.model.OrderList;
import me.itsmenow.erment.model.PayItem;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{

    private Context context;
    private final List<OrderList> mOrderList;
    private final OrderListAdapter.OnOrderItemClickListener mOnOrderItemClickListener;

    public OrderListAdapter(List<OrderList> mOrderList, OrderListAdapter.OnOrderItemClickListener mOnOrderItemClickListener) {
        this.mOrderList = mOrderList;
        this.mOnOrderItemClickListener = mOnOrderItemClickListener;
    }

    public interface OnOrderItemClickListener{
        void onOrderItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textBuyerName;
        TextView textBuyerAddress;
        TextView textBuyerNumber;
        RecyclerView recyclerViewPay;
        TextView textBuyerTotal;
        Button buttonRemove;
        OrderListAdapter.OnOrderItemClickListener onOrderItemClickListener;
        public ViewHolder(View itemView, OrderListAdapter.OnOrderItemClickListener onOrderItemClickListener){
            super(itemView);
            textBuyerName = itemView.findViewById(R.id.text_buyer_name);
            textBuyerAddress = itemView.findViewById(R.id.text_buyer_address);
            textBuyerNumber = itemView.findViewById(R.id.text_buyer_number);
            recyclerViewPay = itemView.findViewById(R.id.recycler_order_list);
            buttonRemove = itemView.findViewById(R.id.button_remove);
            textBuyerTotal = itemView.findViewById(R.id.text_buyer_total);
            this.onOrderItemClickListener = onOrderItemClickListener;
            buttonRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onOrderItemClickListener.onOrderItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.item_history, parent, false);
        // Return a new holder instance
        return new OrderListAdapter.ViewHolder(userView, mOnOrderItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        OrderList orderList = mOrderList.get(position);
        // Set item views based on your views and data model
        TextView textBuyerName = holder.textBuyerName;
        textBuyerName.setText(orderList.getBuyerName());
        TextView textBuyerAddress = holder.textBuyerAddress;
        textBuyerAddress.setText(orderList.getBuyerAddress());
        TextView textBuyerNumber = holder.textBuyerNumber;
        textBuyerNumber.setText(orderList.getBuyerNumber());
        RecyclerView recyclerView = holder.recyclerViewPay;
        TextView textBuyerTotal = holder.textBuyerTotal;
        textBuyerTotal.setText(orderList.getFormattedTotalPrice());
        PayListAdapter payListAdapter = new PayListAdapter(orderList.getPayItemList(), position1 -> {});
        recyclerView.setAdapter(payListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        payListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

}
