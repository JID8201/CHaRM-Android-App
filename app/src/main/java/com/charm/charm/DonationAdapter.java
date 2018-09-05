package com.charm.charm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DonationAdapter extends ArrayAdapter<DonationCategory> {

    public DonationAdapter(Context context, ArrayList<DonationCategory> categories ) {
        super( context, 0, categories );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        // Get the data item for this position.
        DonationCategory category = getItem( position );

        // Check if the existing view is being reused, otherwise inflate the view
        if( convertView == null ) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.donation_item, parent, false );
        }

        TextView categoryName = convertView.findViewById( R.id.item_txt_category );
        TextView categoryAmount = convertView.findViewById( R.id.item_txt_amount);
        TextView categoryUnit = convertView.findViewById( R.id.item_txt_unit );

        // Populate data into the template.
        categoryName.setText( category.getDonation_name());
        categoryAmount.setText( NumberFormat.getIntegerInstance().format( category.getDonation_amount() ) );
        categoryUnit.setText( category.get_unit() );

        return convertView;
    }
}
