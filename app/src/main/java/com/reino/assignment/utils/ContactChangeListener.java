package com.reino.assignment.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactChangeListener {
    private final ContentResolver contentResolver;
    private final Context context;
    private static ContactChangeListener mInstance;
    private static IChangeListener mListener;
    private ContactChangeObserver changeObserver;

    public interface IChangeListener {
        void onContactChanged();
    }

    private ContactChangeListener(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public static ContactChangeListener getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ContactChangeListener(context);
        }
        return mInstance;
    }

    public void startContactObservation(IChangeListener iChangeListener) {
        changeObserver = new ContactChangeObserver(new Handler(Looper.getMainLooper()));
        contentResolver.registerContentObserver(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                true, changeObserver);
        mListener = iChangeListener;
    }

    public void stopContactObservation() {
        contentResolver.unregisterContentObserver(changeObserver);
        mListener = null;
    }

    private void notifyContactChanged() {
        if (mListener != null) {
            mListener.onContactChanged();
        }
    }


    private final static long CHANGE_UPDATE_TIME = 2000;
    private long mLastTimeChangeProcessed = 0l;

    private class ContactChangeObserver extends ContentObserver {
        private final String TAG = getClass().getSimpleName();
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public ContactChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.e(TAG, "onChange: called "+selfChange);

            long mLastTimeChangeOccured = System.currentTimeMillis();
            if (mLastTimeChangeOccured - mLastTimeChangeProcessed > CHANGE_UPDATE_TIME){
                notifyContactChanged();
                Log.e("TAG", "onChange:Contacts Changed , now call sync  ----------->> " );
                mLastTimeChangeProcessed = System.currentTimeMillis();
            }
        }
    }

}
