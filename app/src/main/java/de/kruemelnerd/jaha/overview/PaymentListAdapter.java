package de.kruemelnerd.jaha.overview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.data.room.PaymentEntry;


public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView paymentNameTextView;
        private final TextView paymentPriceTextView;

        private WordViewHolder(View itemView) {
            super(itemView);
            paymentNameTextView = itemView.findViewById(R.id.textViewName);
            paymentPriceTextView = itemView.findViewById(R.id.textViewPrice);
        }
    }

    private final LayoutInflater mInflater;
    private List<PaymentEntry> mWords; // Cached copy of words

    public PaymentListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (mWords != null) {
            PaymentEntry current = mWords.get(position);
            holder.paymentNameTextView.setText(current.getName());
            holder.paymentPriceTextView.setText(String.valueOf(current.getPrice()));
        } else {
            // Covers the case of data not being ready yet.
            holder.paymentNameTextView.setText("No Word");
        }
    }

    public void setPayments(List<PaymentEntry> words){
        mWords = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }
}