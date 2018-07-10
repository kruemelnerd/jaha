package de.kruemelnerd.jaha.data.room;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class PaymentRepository {

    private PaymentDao mPaymentDao;
    private LiveData<List<PaymentEntry>> mAllWords;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PaymentRepository(Application application) {
        PaymentRoomDatabase db = PaymentRoomDatabase.getDatabase(application);
        mPaymentDao = db.paymentEntryDao();
        mAllWords = mPaymentDao.getAllPayments();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<PaymentEntry>> getAllPayments() {
        return mAllWords;
    }

    public LiveData<PaymentEntry> getPaymentWithBarcode(String barcode){
        return mPaymentDao.getPaymentWithBarcode(barcode);
    }



    public void update(PaymentEntry payment) {
        new AsyncTask<PaymentEntry, Void, Void>() {
            private PaymentDao mAsyncTaskDao;

            @Override
            protected Void doInBackground(PaymentEntry... paymentEntries) {
                mPaymentDao.update(paymentEntries[0]);
                return null;
            }
        }.execute(payment);
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(PaymentEntry word) {
        new insertAsyncTask(mPaymentDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<PaymentEntry, Void, Void> {

        private PaymentDao mAsyncTaskDao;

        insertAsyncTask(PaymentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PaymentEntry... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
