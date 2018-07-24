package de.kruemelnerd.jaha.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.GtinRepository;
import de.kruemelnerd.jaha.addNewPayment.onlineBarcodeDatabase.model.GtinResponse;
import de.kruemelnerd.jaha.data.room.PaymentEntry;
import de.kruemelnerd.jaha.data.room.PaymentRepository;


public class PaymentViewModel extends AndroidViewModel {

    private PaymentRepository mRepositoryPayment;
    private GtinRepository mGtinRepository;
    private LiveData<List<PaymentEntry>> mAllPayments;
    private LiveData<PaymentEntry> mPaymentWithBarcode;


    public PaymentViewModel(Application application) {
        super(application);
        mRepositoryPayment = new PaymentRepository(application);
        mGtinRepository = GtinRepository.getInstance();
        mAllPayments = mRepositoryPayment.getAllPayments();
    }

    public LiveData<List<PaymentEntry>> getAllPayments() {
        return mAllPayments;
    }

    public LiveData<PaymentEntry> getPaymentWithBarcode(String barcode) {
        return mRepositoryPayment.getPaymentWithBarcode(barcode);
    }

    public void insert(PaymentEntry payment) {
        mRepositoryPayment.insert(payment);
    }

    public void update(PaymentEntry payment) {
        mRepositoryPayment.update(payment);
    }

    public LiveData<GtinResponse> getProductForGtin(String barcode) {
        return mGtinRepository.getProductForGtin(barcode);
    }


}
