package me.itsmenow.erment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import me.itsmenow.erment.R;
import me.itsmenow.erment.model.PayItem;

public class PayListAdapter extends RecyclerView.Adapter<PayListAdapter.ViewHolder>{

    private final List<PayItem> mPayList;
    private final PayListAdapter.OnPayItemClickListener mOnPayItemClickListener;

    public PayListAdapter(List<PayItem> mPayList, PayListAdapter.OnPayItemClickListener onPayItemClickListener) {
        this.mPayList = mPayList;
        this.mOnPayItemClickListener = onPayItemClickListener;
    }

    public interface OnPayItemClickListener{
        void onPayItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textPayName;
        TextView textQuantityPrice;
        TextView textTotalPrice;
        PayListAdapter.OnPayItemClickListener onPayItemClickListener;
        public ViewHolder(View itemView, PayListAdapter.OnPayItemClickListener onPayItemClickListener){
            super(itemView);
            textPayName = itemView.findViewById(R.id.pay_item_name);
            textQuantityPrice = itemView.findViewById(R.id.pay_item_quantityprice);
            textTotalPrice = itemView.findViewById(R.id.pay_item_total);
            this.onPayItemClickListener = onPayItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPayItemClickListener.onPayItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public PayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View userView = inflater.inflate(R.layout.item_pay_list, parent, false);
        // Return a new holder instance
        return new PayListAdapter.ViewHolder(userView, mOnPayItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PayListAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        PayItem payItem = mPayList.get(position);
        // Set item views based on your views and data model
        TextView payName = holder.textPayName;
        payName.setText(payItem.getName());
        TextView payQuantityPrice = holder.textQuantityPrice;
        payQuantityPrice.setText(String.format(Locale.getDefault(),"%s x %s", payItem.getQuantity(), payItem.getFormattedPrice()));
        TextView payTotalPrice = holder.textTotalPrice;
        payTotalPrice.setText(payItem.getFormattedTotalPrice());
    }

    @Override
    public int getItemCount() {
        return mPayList.size();
    }
}
