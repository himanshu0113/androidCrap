package com.example.himanshuaggarwal.contactlist;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by himanshuaggarwal on 30/03/18.
 */

public class DetailFragment_A2_MT17015 extends Fragment {
    Contact_A2_MT17015 mContact;
    TextView mName;
    TextView mNumber;
    TextView mEmail;
    ImageView mProfileImage;
    FloatingActionButton mEditButton;

    private static final String TAG = "Detail_Fragment";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String contactId = (String) getActivity().getIntent().getSerializableExtra(DetailActivity_A2_MT17015.EXTRA_CONTACT_ID);
        mContact = ContactStore_A2_MT17015.get(getActivity()).getContact(contactId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        if (mContact!=null) {
            mName = (TextView) v.findViewById(R.id.detail_contact_name);
            mName.setText(mContact.getmName());

            mNumber = (TextView) v.findViewById(R.id.detail_contact_number);
            mNumber.setText(mContact.getmNumber());
            mNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+mContact.getmNumber()));
                    if (callIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(callIntent);
                    }
                }
            });

            mEmail = (TextView) v.findViewById(R.id.detail_contact_email);
            mEmail.setText(mContact.getmEmail());
            mEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { mContact.getmEmail() });
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + mContact.getmEmail()));
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(Intent.createChooser(intent, ""));
                    }
                }
            });

            mProfileImage = (ImageView) v.findViewById(R.id.detail_profile_image);
            mProfileImage.setImageBitmap(mContact.getmProfileImage());

            mEditButton = (FloatingActionButton) v.findViewById(R.id.edit_button);
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = mContact.getmId();
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id)));
                    startActivity(intent);
                }
            });
        }

        return v;
    }
}
