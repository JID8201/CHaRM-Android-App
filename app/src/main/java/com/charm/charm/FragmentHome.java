package com.charm.charm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<DonationCategory> categories = createCategories();
        DonationAdapter donationAdapter = new DonationAdapter( getContext(), categories );

        ListView listView = view.findViewById( R.id.home_list_categories );
        listView.setAdapter( donationAdapter );

        return view;
    }

    private ArrayList<DonationCategory> createCategories() {
        ArrayList<DonationCategory> categories = new ArrayList<>();
        categories.add( new DonationCategory( "Paint", 219245, "Gallons" ) );
        categories.add( new DonationCategory( "Illegaly Dumped Tires", 21500, "Tires" ) );
        categories.add( new DonationCategory( "Hazardous Chemicals", 111643, "Gallons" ) );
        categories.add( new DonationCategory( "Electronics", 211, "lbs." ) );

        return categories;
    }

}
