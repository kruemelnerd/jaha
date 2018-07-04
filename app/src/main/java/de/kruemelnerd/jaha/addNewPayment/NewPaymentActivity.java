package de.kruemelnerd.jaha.addNewPayment;

import android.arch.persistence.room.util.StringUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.kruemelnerd.jaha.R;

public class NewPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "de.kruemelnerd.android.payment.listsql.REPLY";

    private EditText mEditWordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        mEditWordView = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String namePayment = mEditWordView.getText().toString();
                    if(TextUtils.isEmpty(namePayment)){
                        mEditWordView.setError(getString(R.string.error_field_required));
                    }

                    //float pricePayment = mEditWordView.getText().toString();

                    replyIntent.putExtra(EXTRA_REPLY, namePayment);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}