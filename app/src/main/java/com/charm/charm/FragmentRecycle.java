package com.charm.charm;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecycle extends Fragment {


    public FragmentRecycle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        String zipcode = sharedPreferences.getString( getString( R.string.pref_zipcode ), null );

        TextView userZip = view.findViewById( R.id.recycle_user_zip);

        userZip.setText( zipcode );

        return view;
    }
}
