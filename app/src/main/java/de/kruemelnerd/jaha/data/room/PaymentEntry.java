package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

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

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "date")
    private Calendar mCalendarDate;

    @ColumnInfo(name = "category")
    private String mCategory;

    @ColumnInfo(name = "barcode")
    private String mBarcode;


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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Calendar getCalendarDate() {
        return this.mCalendarDate;
    }

    public void setCalendarDate(Calendar date) {
        this.mCalendarDate = date;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public void setBarcode(String barcode) {
        this.mBarcode = barcode;
    }
}



