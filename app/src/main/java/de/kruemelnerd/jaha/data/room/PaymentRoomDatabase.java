package de.kruemelnerd.jaha.data.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = {PaymentEntry.class}, version = 2)
public abstract class PaymentRoomDatabase extends RoomDatabase {

    public abstract PaymentDao paymentEntryDao();

    private static PaymentRoomDatabase INSTANCE;

    public static PaymentRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PaymentRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PaymentRoomDatabase.class, "payment_database")
                            //.fallbackToDestructiveMigration()
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
