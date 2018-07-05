package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "paymentEntry")
public class PaymentEntry implements Serializable {
//TODO: Use Parcable instead of Serializable

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "paymentEntryId")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "price")
    private float mPrice;

    @Ignore
    public PaymentEntry(String name, float price) {
        this.mName = name;
        this.mPrice = price;
    }

    public PaymentEntry() {
        this.mName = "";
        this.mPrice = 0f;
    }


    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float mPrice) {
        this.mPrice = mPrice;
    }
}



