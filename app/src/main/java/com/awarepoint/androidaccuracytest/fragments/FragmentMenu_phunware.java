package com.awarepoint.androidaccuracytest.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awarepoint.androidaccuracytest.MainActivity;
import com.awarepoint.androidaccuracytest.R;

/**
 * Created by dkashipara on 5/19/2017.
 */

public class FragmentMenu_phunware extends Fragment{
    public View mfragmentView = null;

    public View fragmentRoomTransitionView;

    public MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mfragmentView = inflater.inflate(R.layout.layout_main_menuphnwr, null);

        mainActivity = ((MainActivity) getActivity());

        return mfragmentView;
    }

    public void setFragmentContent(FragmentContent fragmentContent) {
        fragmentRoomTransitionView = fragmentContent.fragmentRoomTransition.mfragmentView;
    }
    public void processComplete(boolean isFailed) {
        try {
            mainActivity.progressDialog.dismiss();
            if(isFailed)
                Toast.makeText(mainActivity, "Process failed, please try again", Toast.LENGTH_SHORT).show
                        ();
        } catch (Exception err) {
            Log.e(mainActivity.TAG, err.getMessage());
        }
    }
}
