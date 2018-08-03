package com.example.himanshuaggarwal.contactlist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by himanshuaggarwal on 31/03/18.
 */

public class RecyclerListFragment_A2_MT17015 extends Fragment {
    private RecyclerView mContactRecyclerView;
    private ContactAdapter mAdapter;
    private FloatingActionButton mAddButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_list, container, false);

        mContactRecyclerView = (RecyclerView) v.findViewById(R.id.contact_recycler_view);

        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddButton = (FloatingActionButton) v.findViewById(R.id.add_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
                    updateUI();
            }
        });

        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class ContactHolder extends RecyclerView.ViewHolder{
        private Contact_A2_MT17015 mContact;
        private TextView mName;
        private ImageView mProfileImage;


        public ContactHolder(View singleView) {
            super(singleView);

            mName = (TextView) singleView.findViewById(R.id.contact_name);
            mProfileImage = (ImageView) singleView.findViewById(R.id.profile_image);


            singleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = DetailActivity_A2_MT17015.newIntent(getActivity(), mContact.getmId());
                    getActivity().startActivity(intent);
                }
            });
        }

        private void bindContact(Contact_A2_MT17015 contact){
            mContact = contact;
            mName.setText(mContact.getmName());
            if (mContact.getmProfileImage()!=null){
                mProfileImage.setImageBitmap(mContact.getmProfileImage());
            }
            else
               mProfileImage.setImageDrawable(Drawable.createFromPath("@drawable/ic_person_black_24dp"));
        }
    }

    private void updateUI(){
        ContactStore_A2_MT17015 contactStore = ContactStore_A2_MT17015.get(getActivity());
        List<Contact_A2_MT17015> contacts = contactStore.getContacts();

        if (mAdapter==null) {
            mAdapter = new ContactAdapter(contacts);
            mContactRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
        private List<Contact_A2_MT17015> mContacts;

        public ContactAdapter(List<Contact_A2_MT17015> contacts){
            mContacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.fragment_list, parent, false);

            return new ContactHolder(v);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            Contact_A2_MT17015 contact = mContacts.get(position);
            holder.bindContact(contact);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }
    }
}
