package de.kruemelnerd.jaha.addNewPayment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.data.room.PaymentEntry;

import static de.kruemelnerd.jaha.detail.DetailActivity.EXTRA_PAYMENT;

public class NewPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_REPLY = "de.kruemelnerd.android.payment.listsql.REPLY";

    @BindView(R.id.addPaymentName)
    EditText mEditNameView;
    @BindView(R.id.addPaymentPrice)
    EditText mEditPriceView;
    @BindView(R.id.addPaymentDescription)
    EditText mEditViewDescription;
    @BindView(R.id.addPaymentDate)
    EditText mEditViewDate;

    private DatePickerDialog mDatePickerDialog;
    private Calendar newDate;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    private boolean isNewPayment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbarNewPayment);
        setSupportActionBar(toolbar); // FIXME: Not working at the moment. Nevermind. Wait for the big implementation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        isNewPayment = checkIfNewPayment();

        if (isNewPayment) {
            Spinner spinner = findViewById(R.id.currencies_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.currencies_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            initDatePicker();
            Calendar calender = Calendar.getInstance();
            mEditViewDate.setText(dateFormatter.format(calender.getTime()));

        } else {
            Intent intent = getIntent();
            Bundle data = intent.getExtras();
            PaymentEntry entry = (PaymentEntry) data.getSerializable(EXTRA_PAYMENT);
            showPayment(entry);

        }


    }

    private boolean checkIfNewPayment() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data == null) {
            return true;
        }
        PaymentEntry entry = (PaymentEntry) data.getSerializable(EXTRA_PAYMENT);
        if (entry == null) {
            return true;
        }
        return false;
    }


    private void showPayment(PaymentEntry entry) {
        getSupportActionBar().setTitle(entry.getName());
        mEditNameView.setText(entry.getName());
        mEditViewDescription.setText(entry.getDescription());
        mEditPriceView.setText(String.valueOf(entry.getPrice()));
        if (entry.getCalendarDate() != null) {
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

            mEditViewDate.setInputType(InputType.TYPE_NULL);
            mEditViewDate.setText(dateFormatter.format(entry.getCalendarDate().getTime()));
        } else {
            Toast.makeText(this, "Date is missing", Toast.LENGTH_SHORT).show();
        }

    }

    private void initDatePicker() {
        Calendar calender = Calendar.getInstance();
        newDate = calender;
        mEditViewDate.setInputType(InputType.TYPE_NULL);
        mEditViewDate.setOnClickListener(this);

        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                mEditViewDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.button_save)
    public void savePayment() {
        Intent replyIntent = new Intent();
        //These two are mandatory
        boolean nameFilled = isMandatoryFieldFilled(mEditNameView);
        boolean priceFilled = isMandatoryFieldFilled(mEditPriceView);

        if (nameFilled && priceFilled) {
            PaymentEntry paymentEntry = new PaymentEntry();
            paymentEntry.setName(mEditNameView.getText().toString());
            paymentEntry.setPrice(Float.valueOf(mEditPriceView.getText().toString()));
            paymentEntry.setDescription(mEditViewDescription.getText().toString());

            paymentEntry.setCalendarDate(newDate);

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

    @Override
    public void onClick(View v) {
        if (v == mEditViewDate) {
            mDatePickerDialog.show();
        }
    }
}