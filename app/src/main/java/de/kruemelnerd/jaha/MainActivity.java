package de.kruemelnerd.jaha;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import de.kruemelnerd.jaha.addNewPayment.NewPaymentActivity;
import de.kruemelnerd.jaha.data.room.PaymentEntry;
import de.kruemelnerd.jaha.detail.DetailActivity;
import de.kruemelnerd.jaha.overview.OverviewAdapter;
import de.kruemelnerd.jaha.viewModel.PaymentViewModel;

public class MainActivity extends AppCompatActivity {

    private PaymentViewModel mPaymentViewModel;
    public static final int NEW_PAYMENT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        OverviewAdapter.OnItemClickListener listener = new OverviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, PaymentEntry item) {
                launchDetailActivity(position, item);
            }
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerview_list_payments);
        final OverviewAdapter adapter = new OverviewAdapter(this, listener);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPaymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
        mPaymentViewModel.getAllPayments().observe(this, new Observer<List<PaymentEntry>>() {
            @Override
            public void onChanged(@Nullable List<PaymentEntry> paymentEntries) {
                adapter.setPayments(paymentEntries);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_new_payment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewPaymentActivity.class);
                startActivityForResult(intent, NEW_PAYMENT_ACTIVITY_REQUEST_CODE);
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_PAYMENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            PaymentEntry payment = (PaymentEntry) data.getSerializableExtra(NewPaymentActivity.EXTRA_REPLY);
            mPaymentViewModel.insert(payment);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchDetailActivity(int position, PaymentEntry entry){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.EXTRA_PAYMENT, entry);
        MainActivity.this.startActivity(intent);
    }
}
