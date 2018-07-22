package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

@Entity(tableName = "paymentEntry")
public class PaymentEntry implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "paymentEntryId")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "price")
    private int mPrice;

    @ColumnInfo(name = "description")
    private String mDescription;

    @ColumnInfo(name = "date")
    private Calendar mCalendarDate;

    @ColumnInfo(name = "category")
    private String mCategory;

    @ColumnInfo(name = "barcode")
    private String mBarcode;

    @ColumnInfo(name = "location")
    private Location mLocation;

    @ColumnInfo(name = "location_address")
    private String mLocationAddress;


    @Ignore
    public PaymentEntry(String name, int price) {
        this.mName = name;
        this.mPrice = price;
    }

    public PaymentEntry() {
        this.mName = "";
        this.mPrice = 0;
    }


    protected PaymentEntry(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mPrice = in.readInt();
        mDescription = in.readString();
        mCategory = in.readString();
        mBarcode = in.readString();
        mLocation = in.readParcelable(Location.class.getClassLoader());
        mLocationAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mPrice);
        dest.writeString(mDescription);
        dest.writeString(mCategory);
        dest.writeString(mBarcode);
        dest.writeParcelable(mLocation, flags);
        dest.writeString(mLocationAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentEntry> CREATOR = new Creator<PaymentEntry>() {
        @Override
        public PaymentEntry createFromParcel(Parcel in) {
            return new PaymentEntry(in);
        }

        @Override
        public PaymentEntry[] newArray(int size) {
            return new PaymentEntry[size];
        }
    };

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

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int mPrice) {
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


    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public String getLocationAddress() {
        return mLocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.mLocationAddress = locationAddress;
    }
}



