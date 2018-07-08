package de.kruemelnerd.jaha.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import de.kruemelnerd.jaha.data.room.PaymentEntry;
import de.kruemelnerd.jaha.data.room.PaymentRepository;


public class PaymentViewModel extends AndroidViewModel {

    private PaymentRepository mRepository;
    private LiveData<List<PaymentEntry>> mAllPayments;

    public PaymentViewModel(Application application){
        super(application);
        mRepository = new PaymentRepository(application);
        mAllPayments = mRepository.getAllPayments();
    }

    public LiveData<List<PaymentEntry>> getAllPayments(){
        return mAllPayments;
    }

    public void insert(PaymentEntry payment){
        mRepository.insert(payment);
    }

    public void update(PaymentEntry payment) { mRepository.update(payment); }

}
