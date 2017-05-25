package com.awarepoint.androidaccuracytest.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awarepoint.androidaccuracytest.R;

/**
 * Created by ureyes on 3/17/2016.
 */
public class FragmentContent extends Fragment {

    public MapViewFragment googleFragment;
    public View mfragmentView;
    public FragmentRoomTransition fragmentRoomTransition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mfragmentView = inflater.inflate(R.layout.layout_main_content, null);
        googleFragment = (MapViewFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        fragmentRoomTransition = (FragmentRoomTransition) getChildFragmentManager().findFragmentById(R.id.fragment_room_transition);
        googleFragment.mfragmentView.setVisibility(View.GONE);
        return mfragmentView;
    }


}
