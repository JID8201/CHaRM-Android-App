package com.charm.charm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecycleSpinnerAdapter extends ArrayAdapter<DonationCategory> {

    public RecycleSpinnerAdapter(Context context, int resource, List categories ) {
        super( context, resource, 0, categories );
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent ) {
        return createItemView( position, convertView, parent );
    }

    public View getView( int position, View convertView, ViewGroup parent ) {
        return createItemView( position, convertView, parent );
    }

    public View createItemView( int position, View convertView, ViewGroup parent ) {
        // Get the data item for this position.
        DonationCategory category = getItem( position );

        View view = LayoutInflater.from( getContext() ).inflate( R.layout.adapter_recycle_spinner, parent, false );

        TextView spinnerItem = view.findViewById( R.id.adapter_recycle_category );
        spinnerItem.setText( category.getDonation_name() );

        return view;
    }
}
