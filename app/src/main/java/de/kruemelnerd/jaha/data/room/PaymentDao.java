package de.kruemelnerd.jaha.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PaymentDao {

    @Insert
    void insert(PaymentEntry entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(PaymentEntry entry);


    @Query("SELECT * from paymentEntry ORDER BY name ASC")
    LiveData<List<PaymentEntry>> getAllPayments();

    @Delete
    int delete(PaymentEntry entry);

    @Query("DELETE FROM paymentEntry")
    public void deleteAll();

    @Query("SELECT * FROM paymentEntry WHERE barcode= :barcode")
    LiveData<PaymentEntry> getPaymentWithBarcode(String barcode);
}
