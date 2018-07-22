package de.kruemelnerd.jaha.overview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.data.room.PaymentEntry;


public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.WordViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(int position, PaymentEntry item);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView paymentNameTextView;
        private final TextView paymentPriceTextView;
        private final LinearLayout overviewItemHolder;

        private WordViewHolder(View itemView) {
            super(itemView);
            paymentNameTextView = itemView.findViewById(R.id.textViewName);
            paymentPriceTextView = itemView.findViewById(R.id.textViewPrice);
            overviewItemHolder = itemView.findViewById(R.id.overviewItemHolder);
        }
    }

    private final LayoutInflater mInflater;
    private List<PaymentEntry> mPayments; // Cached copy of words
    private OnItemClickListener listener;

    public OverviewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public OverviewAdapter(Context context, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, final int position) {
        if (mPayments != null) {
            final PaymentEntry current = mPayments.get(position);
            holder.paymentNameTextView.setText(current.getName());
            float priceInEuro = Float.valueOf(current.getPrice()) / 100;
            holder.paymentPriceTextView.setText(String.valueOf(priceInEuro));

            holder.overviewItemHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, current);
                }
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.paymentNameTextView.setText("No Word");
        }
    }

    public void setPayments(List<PaymentEntry> payments) {
        mPayments = payments;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPayments != null)
            return mPayments.size();
        else return 0;
    }
}