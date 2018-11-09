package com.charm.charm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RecycledAdapter extends ArrayAdapter<DonationCategory> {

    public RecycledAdapter(Context context, ArrayList<DonationCategory> categories ) {
        super( context, 0, categories );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        // Get the data item for this position.
        DonationCategory category = getItem( position );

        // Check if the existing view is being reused, otherwise inflate the view
        if( convertView == null ) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.recycled_item, parent, false );
        }

        TextView categoryName = convertView.findViewById( R.id.recItem_name );
        TextView categoryAmount = convertView.findViewById( R.id.recItem_amount );
        String categoryUnit = category.get_unit();

        String categoryAmountText = NumberFormat.getIntegerInstance().format( category.getDonation_amount() );
        if( !categoryUnit.equals( "" ) ) {
            categoryAmountText += " " + categoryUnit;
        }

        // Populate data into the template.
        categoryName.setText( category.getDonation_name());
        categoryAmount.setText( categoryAmountText );

        return convertView;
    }
}
