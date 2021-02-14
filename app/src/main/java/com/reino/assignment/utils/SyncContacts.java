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
    private String phoneNumber;
    private String phoneType;
    private int type;

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
                        ArrayList<String> phoneTypeList = new ArrayList<>();
                        int hasPhoneNumber = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                        if (hasPhoneNumber > 0) {
                            mBuilder.append("\"").append(name).append("\"");
                            Cursor phoneCursor = mContentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                                    new String[]{""+id}, null);

                            assert phoneCursor != null;
                            while (phoneCursor.moveToNext()) {
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                        .replaceAll("\\s", "");
                                type = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                phoneType = managePhoneNumberType(type);
                                if (phoneNumber.startsWith("0"))
                                    phoneNumber = "+91"+phoneNumber.substring(1);
                                else if (!phoneNumber.startsWith("+"))
                                    phoneNumber = "+91"+phoneNumber;

                                if (!phoneList.contains(phoneNumber))
                                    phoneList.add(phoneNumber);

                                if (!phoneTypeList.contains(phoneType))
                                    phoneTypeList.add(phoneType);
                            }
                            phoneCursor.close();
                        }
                        if (name != null && !name.equals("") && phoneList.size() != 0) {
                            ContactModel contact = new ContactModel(id, photo, name, phoneList, System.currentTimeMillis() + "", phoneTypeList);
                            contactList.add(contact);
                        }
                    }
                }
                mCursor.close();

                return contactList;
            }
        });
    }

    /**
     * This method will return the Phone Number type, based on the View Type Number.
     * @param phoneNumberType
     * @return
     */
    private String managePhoneNumberType(int phoneNumberType) {
        switch (phoneNumberType) {
            case 1:
                return "Home";
            case 2:
                return "Mobile";
            case 3:
                return "Work";
            case 4:
                return "Fax Work";
            case 5:
                return "Fax Home";
            case 6:
                return "Pager";
            case 7:
                return "Other";
            case 8:
                return "Callback";
            case 9:
                return "Car";
            case 10:
                return "Company";
            case 11:
                return "ISDN";
            case 12:
                return "Main";
            case 13:
                return "Other Fax";
            case 14:
                return "Radio";
            case 15:
                return "Telex";
            case 16:
                return "TTY_TDD";
            case 17:
                return "Work Mobile";
            case 18:
                return "Work Pager";
            case 19:
                return "Assistant";
            case 20:
                return "MMS";
            default:
                return "Other";
        }
    }
}
