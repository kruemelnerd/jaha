package de.kruemelnerd.jaha.addNewPayment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
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

public class NewPaymentActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    public static final String EXTRA_REPLY = "de.kruemelnerd.android.payment.listsql.REPLY";
    private static final String TAG = "NewPaymentActivity";

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
    @BindView(R.id.addPaymentLocation)
    EditText mEditViewLocation;


    private PaymentViewModel mPaymentViewModel;
    private PaymentEntry mPaymentEntry;
    private DatePickerDialog mDatePickerDialog;
    private Calendar newDate;
    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    private boolean isNewPayment;

    //Location & Maps
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    GoogleMap mGoogleMap;
    MapFragment mapFragment;
    Marker mCurrLocationMarker;


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

        // Location

        initLocationService();
        initMaps();

        isNewPayment = checkIfNewPayment();

        if (isNewPayment) {
            mPaymentEntry = new PaymentEntry();
            initSpinner();
            initDatePicker();
            updateDateUi();

        } else {
            Intent intent = getIntent();
            Bundle data = intent.getExtras();
            if (data != null) {
                mPaymentEntry = data.getParcelable(EXTRA_PAYMENT);
            }
            showPayment();
        }
    }

    private void initSpinner() {
        // Not working in this Version :(
       /* Spinner spinner = findViewById(R.id.currencies_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
    }

    private void initMaps() {


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.addPaymentMaps);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);

    }

    private void initLocationService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mLastLocation = location;

                    // In some rare cases the location returned can be null
                    if (mLastLocation == null) {
                        return;
                    }

                    if (!Geocoder.isPresent()) {
                        Toast.makeText(NewPaymentActivity.this, R.string.location_no_geocoder_available, Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Start service and update UI to reflect new location
                    startIntentService();
                    updateLocationUI();

                    //TODO translate into address and display in TextField

                }
            }
        };
    }

    private void updateLocationUI() {
        if (mAddressOutput != null && StringUtils.isNotBlank(mAddressOutput)) {
            mEditViewLocation.setText(mAddressOutput);
            mLastLocation = mPaymentEntry.getLocation();
        }

        if (mLastLocation != null) {
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            mapFragment.getView().setVisibility(View.VISIBLE);
            //Place current location marker
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }


    @OnClick(R.id.addPaymentLocationButton)
    public void callLocationService() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
                mapFragment.getView().setVisibility(View.VISIBLE);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.location_permission_needed_for_app_title))
                        .setMessage(getString(R.string.location_permission_needed_for_app))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(NewPaymentActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.RECEIVER, mResultReceiver);
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null && mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private boolean checkIfNewPayment() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data == null) {
            return true;
        }
        PaymentEntry entry = data.getParcelable(EXTRA_PAYMENT);
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
        mAddressOutput = mPaymentEntry.getLocationAddress();
        updateLocationUI();
        initDatePicker();
        updateDateUi();
    }

    private void updateDateUi(){
        mEditViewDate.setInputType(InputType.TYPE_NULL);
        if (mPaymentEntry.getCalendarDate() != null) {
            mEditViewDate.setText(DATE_FORMATTER.format(mPaymentEntry.getCalendarDate().getTime()));
        }else {
            Calendar calender = Calendar.getInstance();
            mEditViewDate.setText(DATE_FORMATTER.format(calender.getTime()));
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
                mEditViewDate.setText(DATE_FORMATTER.format(newDate.getTime()));
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
            mPaymentEntry.setLocationAddress(mEditViewLocation.getText().toString());
            mPaymentEntry.setLocation(mLastLocation);

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
                    if (entry != null) {
                        mPaymentEntry = entry;
                        mPaymentEntry.setId(0);
                        showPayment();
                    } else {
                        fillBarcode(code);
                    }

                }
            });

        } else {
            // else continue with any other code you need in the method
            Toast.makeText(this, getString(R.string.barcode_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void fillBarcode(String barcode) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }


    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }
            updateLocationUI();

            // Show a toast message if an address was found.
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                Toast.makeText(NewPaymentActivity.this, getString(R.string.location_address_found), Toast.LENGTH_SHORT).show();
            }

        }
    }
}