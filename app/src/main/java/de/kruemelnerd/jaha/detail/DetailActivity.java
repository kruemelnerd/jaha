package de.kruemelnerd.jaha.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.data.room.PaymentEntry;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_PAYMENT = "extra_payment";


    @BindView(R.id.detailToolbar)
    Toolbar mToolbar;

    @BindView(R.id.detailDescription)
    TextView mDetailName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        Bundle data = intent.getExtras();

        PaymentEntry entry = (PaymentEntry) data.getSerializable(EXTRA_PAYMENT);
        showPayment(entry);
    }

    private void showPayment(PaymentEntry entry) {
        getSupportActionBar().setTitle(entry.getName());
        mDetailName.setText(entry.getName());

    }


}
