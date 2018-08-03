package com.example.himanshuaggarwal.contactlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by himanshuaggarwal on 31/03/18.
 */

public class DetailActivity_A2_MT17015 extends SingleFragmentActivity_A2_MT17015 {

    public static final String EXTRA_CONTACT_ID = "com.example.himanshu.com.contact_id";

    public static Intent newIntent(Context context, String id){
        Intent intent = new Intent(context, DetailActivity_A2_MT17015.class);
        intent.putExtra(EXTRA_CONTACT_ID, id);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return new DetailFragment_A2_MT17015();
    }
}
