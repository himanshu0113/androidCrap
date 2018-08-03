package com.example.himanshuaggarwal.contactlist;


import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by himanshuaggarwal on 30/03/18.
 */

public class ListFragment_A2_MT17015 extends Fragment {

    private TextView mName;
    private TextView mNumber;
    private TextView mEmail;
    private Image mProfileImage;

    private OnTextBoxClickListner mCallback;
    private static final String TAG = "List_Fragment";

    public interface OnTextBoxClickListner{
        public void onTap();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mName = (TextView) v.findViewById(R.id.contact_name);
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onTap();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnTextBoxClickListner) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"Error");
        }
    }
}
