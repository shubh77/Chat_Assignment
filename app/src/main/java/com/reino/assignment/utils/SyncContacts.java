package com.reino.assignment.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.reino.assignment.model.ContactModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

public class SyncContacts {

    private Context context;
    public SyncContacts(Context context) {
        this.context = context;
    }

    /**
     * This method is used to fetch all the available contacts in the Phone book.
     * @return List of Contacts.
     */
    public Single<List<ContactModel>> fetchContacts() {
        return Single.fromCallable(new Callable<List<ContactModel>>() {
            @Override
            public List<ContactModel> call() throws Exception {
                ArrayList<ContactModel> contactList = new ArrayList<>();
                StringBuilder mBuilder = new StringBuilder();
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

                if (mCursor != null && mCursor.getCount() > 0 ) {
                    while (mCursor.moveToNext()) {
                        Integer id = mCursor.getInt(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String photo = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                        ArrayList<String> phoneList = new ArrayList<>();
                        int hasPhoneNumber = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                        if (hasPhoneNumber > 0) {
                            mBuilder.append("\"").append(name).append("\"");
                            Cursor phoneCursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                                    new String[]{""+id}, null);

                            assert phoneCursor != null;
                            while (phoneCursor.moveToNext()) {
                                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                        .replaceAll("\\s", "");
                                if (phoneNumber.startsWith("0"))
                                    phoneNumber = "+91"+phoneNumber.substring(1);
                                else if (!phoneNumber.startsWith("+"))
                                    phoneNumber = "+91"+phoneNumber;

                                if (!phoneList.contains(phoneNumber))
                                    phoneList.add(phoneNumber);
                                Log.d("ContactListFragment", "id: "+id);
                                Log.d("ContactListFragment", "Name: "+name);
                                Log.d("ContactListFragment", "Phone: "+phoneNumber);
                            }
                            phoneCursor.close();
                        }
                        if (name != null && !name.equals("") && phoneList.size() != 0) {
                            ContactModel contact = new ContactModel(id, photo, name, phoneList, System.currentTimeMillis() + "");
                            contactList.add(contact);
                        }
                    }
                }
                mCursor.close();

                return contactList;
            }
        });
    }
}
