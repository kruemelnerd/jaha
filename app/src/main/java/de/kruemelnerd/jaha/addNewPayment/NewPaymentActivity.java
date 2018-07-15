package de.kruemelnerd.jaha.addNewPayment;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import de.kruemelnerd.jaha.addNewPayment.barcodescanner.IntentIntegrator;
import de.kruemelnerd.jaha.addNewPayment.barcodescanner.IntentResult;
import de.kruemelnerd.jaha.data.room.PaymentEntry;
import de.kruemelnerd.jaha.utils.StringUtils;
import de.kruemelnerd.jaha.viewModel.PaymentViewModel;

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
    @BindView(R.id.addPaymentBarcode)
    EditText mEditViewBarcode;
    @BindView(R.id.addPaymentCategory)
    EditText mEditViewCategory;


    private PaymentViewModel mPaymentViewModel;
    private PaymentEntry mPaymentEntry;
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


        mPaymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
//

        isNewPayment = checkIfNewPayment();

        if (isNewPayment) {
            mPaymentEntry = new PaymentEntry();
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
            if (data != null) {
                mPaymentEntry = (PaymentEntry) data.getSerializable(EXTRA_PAYMENT);
            }
            showPayment();

        }


    }

    private boolean checkIfNewPayment() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data == null) {
            return true;
        }
        PaymentEntry entry = (PaymentEntry) data.getSerializable(EXTRA_PAYMENT);
        return (entry == null);
    }


    private void showPayment() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mPaymentEntry.getName());
        }

        mEditNameView.setText(mPaymentEntry.getName());
        mEditViewDescription.setText(mPaymentEntry.getDescription());
        mEditPriceView.setText(String.valueOf(mPaymentEntry.getPrice()));
        mEditViewBarcode.setText(mPaymentEntry.getBarcode());
        mEditViewCategory.setText(mPaymentEntry.getCategory());
        if (mPaymentEntry.getCalendarDate() != null) {
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

            mEditViewDate.setInputType(InputType.TYPE_NULL);
            mEditViewDate.setText(dateFormatter.format(mPaymentEntry.getCalendarDate().getTime()));
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

            mPaymentEntry.setName(mEditNameView.getText().toString());
            mPaymentEntry.setPrice(Float.valueOf(mEditPriceView.getText().toString()));
            mPaymentEntry.setDescription(mEditViewDescription.getText().toString());
            mPaymentEntry.setBarcode(mEditViewBarcode.getText().toString());
            mPaymentEntry.setCategory(mEditViewCategory.getText().toString());
            mPaymentEntry.setCalendarDate(newDate);

            replyIntent.putExtra(EXTRA_REPLY, mPaymentEntry);
            setResult(RESULT_OK, replyIntent);

            finish();
        } else {
            setResult(RESULT_CANCELED, replyIntent);
        }

    }

    @OnClick(R.id.addPaymentFabBarcode)
    public void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && StringUtils.isNotBlank(scanResult.getContents())) {

            // handle scan result
            final String code = scanResult.getContents().toUpperCase();

            mPaymentEntry.setBarcode(code);
            mEditViewBarcode.setText(mPaymentEntry.getBarcode());

            mPaymentViewModel.getPaymentWithBarcode(code).observe(this, new Observer<PaymentEntry>() {
                @Override
                public void onChanged(@Nullable PaymentEntry entry) {
                    if(entry != null){
                        mPaymentEntry = entry;
                        mPaymentEntry.setId(0);
                        showPayment();
                    }else {
                        fillBarcode(code);
                    }

                }
            });

        } else {
            // else continue with any other code you need in the method
            Toast.makeText(this, getString(R.string.barcode_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void fillBarcode(String barcode){
        Toast.makeText(this, getString(R.string.barcode_not_in_db), Toast.LENGTH_SHORT).show();
        mEditViewBarcode.setText(barcode);
    }

    private boolean isMandatoryFieldFilled(EditText editText) {
        String text = editText.getText().toString();
        if (StringUtils.isBlank(text)) {
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