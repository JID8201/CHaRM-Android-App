package com.charm.charm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecycle extends Fragment implements ActivityHome.ActivityInterface  {

    private View recycleView;
    private Spinner spinner;

    private ArrayList<DonationCategory> recycled_items;
    private RecycledAdapter recycledAdapter;

    public FragmentRecycle() {
        // Required empty public constructor
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        recycleView = inflater.inflate(R.layout.fragment_recycle, container, false);

        recycled_items = new ArrayList<>();
        recycledAdapter = new RecycledAdapter( getContext(), recycled_items ) ;

        setUpListView();

        spinner = recycleView.findViewById( R.id.recycle_spinner_category );

        ArrayList<DonationCategory> categories = createCategories();
        final RecycleSpinnerAdapter recycleSpinnerAdapter = new RecycleSpinnerAdapter( getActivity(), R.layout.adapter_recycle_spinner, categories );

        spinner.setAdapter( recycleSpinnerAdapter );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                DonationCategory category = (DonationCategory) adapterView.getItemAtPosition( position );
                TextView description_view = recycleView.findViewById( R.id.recycle_txt_desc );
                description_view.setText( category.get_description() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        // Set editText for donation amount to 0.
        EditText edit_donation_amount = recycleView.findViewById( R.id.recycle_num_quantity );
        edit_donation_amount.clearFocus();

        // Reset spinner and donate amount when user hits donate button.
        Button donate_button = recycleView.findViewById( R.id.recycle_btn_donate );
        donate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zipcode = getZipcode();

                if( zipcode == null ) {
                    ZipDialogFragment zipDialogFragment = new ZipDialogFragment();
                    zipDialogFragment.show( getFragmentManager(), "zipcode" );
                } else {
                    sendDonePost();
                }
            }
        });

        // Setup our fab add item button.
        FloatingActionButton fab_add_item_button = recycleView.findViewById( R.id.recycle_fab_add );
        fab_add_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_donation_amount = recycleView.findViewById( R.id.recycle_num_quantity );
                EditText edit_description = recycleView.findViewById( R.id.recycle_edit_description );

                // Get user information.
                String donation_material = ((DonationCategory) spinner.getSelectedItem()).getDonation_name();
                String donation_unit = ((DonationCategory) spinner.getSelectedItem()).get_unit();
                String donation_amount = edit_donation_amount.getText().toString();
                String donation_description = edit_description.getText().toString();

                // Check that there is a donatable amount, and show a message if not.
                if( donation_amount.equals( "" ) || donation_amount.equals( "0" ) ) {
                    Toast toast = Toast.makeText( getContext(), "Please enter an amount greater than zero", Toast.LENGTH_LONG );
                    TextView v = toast.getView().findViewById( android.R.id.message );
                    v.setGravity( Gravity.CENTER );
                    toast.show();
                } else {
                    // Add a new item to the adapter view.
                    recycledAdapter.add( new DonationCategory( donation_material, Integer.parseInt( donation_amount ), donation_unit, donation_description ) );

                    edit_donation_amount.setText( "" );
                    edit_description.setText( "" );

                    // Turn on elements if there are items to recycle.
                    if( recycledAdapter.getCount() > 0 ) {
                        recycleView.findViewById( R.id.recycle_txt_hold ).setVisibility( View.VISIBLE );
                        recycleView.findViewById( R.id.recycle_txt_items_title ).setVisibility( View.VISIBLE );
                        recycleView.findViewById( R.id.recycle_btn_donate ).setVisibility( View.VISIBLE );
                    }
                }
            }
        });

        return recycleView;
    }

    private void setUpListView() {
        ListView listView = recycleView.findViewById( R.id.recycle_listview );

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                recycled_items.remove( pos );
                recycledAdapter.notifyDataSetChanged();

                // Make Items invisible if there are no items to recycle.
                if( recycled_items.size() == 0 ) {
                    recycleView.findViewById( R.id.recycle_txt_hold ).setVisibility( View.INVISIBLE );
                    recycleView.findViewById( R.id.recycle_btn_donate ).setVisibility( View.INVISIBLE );
                }

                return true;
            }
        });
        listView.setAdapter( recycledAdapter );
    }

    private void sendDonePost() {
        String zipcode = getZipcode();

        String url = "http://charm-web-app.herokuapp.com/api/recycling";

        JSONArray items = new JSONArray();

        for( int i = 0; i < recycled_items.size(); i++ ) {
            JSONObject item = new JSONObject();
            try {
                item.put( "type", recycled_items.get(i).getDonation_name() );
                item.put( "amount", recycled_items.get(i).getDonation_amount() );
                item.put( "notes", recycled_items.get(i).get_description() );
                items.put( item );
            } catch ( Exception e ) {

            }
        }

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put( "items", items );
            jsonBody.put( "zip", zipcode );

        } catch( Exception e ) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        clearRecycledList();
                        Toast.makeText( getContext(), R.string.recycle_donation_success, Toast.LENGTH_LONG ).show();
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( getContext(), R.string.recycle_donation_error, Toast.LENGTH_LONG ).show();
                        return;
                    }
                })
        {
        };

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( jsonObjectRequest );
    }

    private ArrayList<DonationCategory> createCategories() {
        ArrayList<DonationCategory> categories = new ArrayList<>();
        categories.add( new DonationCategory( "Plastics", 0, "lbs.", " " ) );
        categories.add( new DonationCategory( "Plastic Food Containers", 0, "lbs.", " " ) );
        categories.add( new DonationCategory( "Paper", 0, "lbs.", " " ) );
        categories.add( new DonationCategory( "Glass Bottles/jars", 0, "lbs.", " " ) );
        categories.add( new DonationCategory( "Metal", 0, "lbs", " " ) );
        categories.add( new DonationCategory( "Metal Food Cans", 0, "lbs", " " ) );
        categories.add( new DonationCategory( "Paint", 0, "lbs", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Household Chemicals", 0, "lbs", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Electronics", 0, "lbs", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Tires", 0, "lbs", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Mattresses", 0, "", "*This item has a fee" ) );

        return categories;
    }

    private void clearRecycledList() {
        recycled_items.clear();
        recycledAdapter.notifyDataSetChanged();

        recycleView.findViewById( R.id.recycle_txt_hold ).setVisibility( View.INVISIBLE );
        recycleView.findViewById( R.id.recycle_btn_donate ).setVisibility( View.INVISIBLE );
    }

    public void callListener() {
        sendDonePost();
    }

    private String getZipcode() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        return sharedPreferences.getString( getString( R.string.pref_zipcode ), null );
    }

    private void removeZipcode() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString( getString( R.string.pref_zipcode ), null );
        editor.apply();
    }
}
