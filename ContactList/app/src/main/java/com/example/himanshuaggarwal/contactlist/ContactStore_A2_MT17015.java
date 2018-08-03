package com.example.himanshuaggarwal.contactlist;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by himanshuaggarwal on 31/03/18.
 */

public class ContactStore_A2_MT17015 {
    private static ContactStore_A2_MT17015 sContactStore;
    private List<Contact_A2_MT17015> mContacts;

    public static ContactStore_A2_MT17015 get(Context context) {
        if(sContactStore==null){
            sContactStore = new ContactStore_A2_MT17015(context);
        }
        return sContactStore;
    }

    private ContactStore_A2_MT17015(Context context){
        getSavedContacts(context);
    }

    public List<Contact_A2_MT17015> getContacts(){
        return mContacts;
    }

    public Contact_A2_MT17015 getContact(String id){
        for (Contact_A2_MT17015 contact:mContacts){
            if (contact.getmId().equals(id))
                return contact;
        }

        return null;
    }

    private void getSavedContacts(Context context){
        mContacts = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.getCount()>0){

            while (cursor.moveToNext()){
                Contact_A2_MT17015 contact = new Contact_A2_MT17015();

                contact.setmId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                contact.setmName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{contact.getmId()}, null);

                    while (phoneCursor.moveToNext() && contact.getmNumber()==null){
                        contact.setmNumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }

                    Cursor imageCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{contact.getmId()}, null);

                    while (imageCursor.moveToNext() && contact.getmEmail()==null){
                        contact.setmEmail(imageCursor.getString(imageCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
//                        Log.e("Email", contact.getmEmail());
                    }
                }

                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contact.getmId()));
                Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                Cursor imageCursor = context.getContentResolver().query(photoUri,
                        new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
                if (imageCursor == null) {
                    contact.setmProfileImage(null);
                }
                try {
                    if (imageCursor.moveToFirst()) {
                        byte[] data = imageCursor.getBlob(0);
                        if (data != null) {
                            contact.setmProfileImage(BitmapFactory.decodeByteArray(data, 0, data.length));
                        }
                    }
                } finally {
                    imageCursor.close();
                }

                if (contact.getmName()!=null && contact.getmNumber()!=null)
                    mContacts.add(contact);
            }
        }

    }

}
