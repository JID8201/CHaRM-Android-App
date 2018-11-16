package com.charm.charm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecycle extends Fragment {


    private String zipcode;

    private View recycleView;
    private Spinner spinner;
    private String selected_spinner;
    private DatabaseReference mDatabase;

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

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
//        zipcode = sharedPreferences.getString( getString( R.string.pref_zipcode ), null );
//
//        TextView userZip = recycleView.findViewById( R.id.recycle_user_zip);

        recycled_items = new ArrayList<>();
        recycledAdapter = new RecycledAdapter( getContext(), recycled_items ) ;

        setUpListView();

        spinner = recycleView.findViewById( R.id.recycle_spinner_category );

        ArrayList<DonationCategory> categories = createCategories();
        final RecycleSpinnerAdapter recycleSpinnerAdapter = new RecycleSpinnerAdapter( getActivity(), R.layout.adapter_recycle_spinner, categories );

        spinner.setAdapter( recycleSpinnerAdapter );

        // Set the selected Spinner value to our first position of the adapter.
        selected_spinner = categories.get(0).getDonation_name();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                DonationCategory category = (DonationCategory) adapterView.getItemAtPosition( position );
                TextView description_view = recycleView.findViewById( R.id.recycle_txt_desc );
                description_view.setText( category.get_description() );
                selected_spinner = category.getDonation_name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

//        userZip.setText( zipcode );

        // Set editText for donation amount to 0.
        EditText edit_donation_amount = recycleView.findViewById( R.id.recycle_num_quantity );
        edit_donation_amount.clearFocus();

        mDatabase = FirebaseDatabase.getInstance().getReference("/");

        // Reset spinner and donate amount when user hits donate button.
        Button donate_button = recycleView.findViewById( R.id.recycle_btn_donate );
        donate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_donation_amount = recycleView.findViewById( R.id.recycle_num_quantity );
                EditText edit_description = recycleView.findViewById( R.id.recycle_edit_description );

                String donation_material = ((DonationCategory) spinner.getSelectedItem()).getDonation_name();
                String donation_amount = edit_donation_amount.getText().toString();
                String donation_description = edit_description.getText().toString();

                spinner.setSelection( 0 );

                sendDonePost( donation_material,  donation_description,  Integer.parseInt( donation_amount ) );

                // Clear values.
                edit_donation_amount.setText( "" );
                edit_description.setText( "" );
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

                // Check that there is a donatable amount, and show a message if not.
                if( donation_amount.equals( "" ) || donation_amount.equals( "0" ) ) {
                    Toast toast = Toast.makeText( getContext(), "Please enter an amount greater than zero", Toast.LENGTH_LONG );
                    TextView v = toast.getView().findViewById( android.R.id.message );
                    v.setGravity( Gravity.CENTER );
                    toast.show();
                } else {
                    // Add a new item to the adapter view.
                    recycledAdapter.add( new DonationCategory( donation_material, Integer.parseInt( donation_amount ), donation_unit ) );

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

    private void sendDonePost( String donation_type, String donation_description, int amount ) {
        // Until we get an actual host this needs to be http://<your_ip_address>:<port_number>/api/recycling
        // You can get your ip address from typing ipconfig in your terminal.
        String url = "http://192.168.1.174:3001/api/recycling";

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put( "amount", amount );
            jsonObject.put( "type", donation_type );
            jsonObject.put( "notes", donation_description );
            jsonObject.put( "zip", zipcode );

        } catch( Exception e ) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
}
