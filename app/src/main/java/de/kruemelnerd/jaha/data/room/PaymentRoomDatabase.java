package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = {PaymentEntry.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class PaymentRoomDatabase extends RoomDatabase {

    public abstract PaymentDao paymentEntryDao();

    private static PaymentRoomDatabase INSTANCE;

    public static PaymentRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PaymentRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PaymentRoomDatabase.class, "payment_database")
                            .fallbackToDestructiveMigration() // FIXME: Add better version: https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PaymentDao mDao;

        PopulateDbAsync(PaymentRoomDatabase db) {
            mDao = db.paymentEntryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //mDao.deleteAll();
//            PaymentEntry payment = new PaymentEntry("Hello", 12);
//            mDao.insert(payment);
//            payment = new PaymentEntry("World", 22);
//            mDao.insert(payment);
            return null;
        }
    }
}
