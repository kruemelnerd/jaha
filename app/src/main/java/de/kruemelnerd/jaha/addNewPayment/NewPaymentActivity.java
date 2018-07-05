package de.kruemelnerd.jaha.addNewPayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.data.room.PaymentEntry;

public class NewPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "de.kruemelnerd.android.payment.listsql.REPLY";

    @BindView(R.id.edit_name)
    EditText mEditNameView;
    @BindView(R.id.edit_price)
    EditText mEditPriceView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        ButterKnife.bind(this);


        Toolbar toolbar = findViewById(R.id.toolbarNewPayment);
        setSupportActionBar(toolbar); // FIXME: Not working at the moment. Nevermind. Wait for the big implementation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.button_save)
    public void savePayment() {
        Intent replyIntent = new Intent();
        boolean nameFilled = isMandatoryFieldFilled(mEditNameView);
        boolean priceFilled = isMandatoryFieldFilled(mEditPriceView);

        if (nameFilled && priceFilled) {
            PaymentEntry paymentEntry = new PaymentEntry();
            paymentEntry.setName(mEditNameView.getText().toString());
            paymentEntry.setPrice(Float.valueOf(mEditPriceView.getText().toString()));


            replyIntent.putExtra(EXTRA_REPLY, paymentEntry);
            setResult(RESULT_OK, replyIntent);
            finish();
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }

    }


    private boolean isMandatoryFieldFilled(EditText editText) {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            editText.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }
}