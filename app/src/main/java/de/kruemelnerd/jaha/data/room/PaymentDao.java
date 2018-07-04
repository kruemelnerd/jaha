package de.kruemelnerd.jaha.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PaymentDao {

    @Insert
    void insert(PaymentEntry entry);

    @Query("SELECT * from paymentEntry ORDER BY name ASC")
    LiveData<List<PaymentEntry>> getAllPayments();

    @Delete
    int delete(PaymentEntry entry);

    @Query("DELETE FROM paymentEntry")
    public void deleteAll();


}
