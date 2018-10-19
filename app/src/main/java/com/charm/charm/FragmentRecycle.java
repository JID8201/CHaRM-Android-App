package com.charm.charm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecycle extends Fragment {

    /** TODO : Zipcode stored as m_var to prevent access to pref in onClick meth below.
     *          - Need to find better way to handle this in future
    **/
    private String zipcode;

    private View recycleView;
    private Spinner spinner;
    private DatabaseReference mDatabase;

    public FragmentRecycle() {
        // Required empty public constructor
    }

    private void post2DB(Date fname, String field, String value) {
        mDatabase.child("users").child(fname.toString()).child(field).setValue(value);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recycleView = inflater.inflate(R.layout.fragment_recycle, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        zipcode = sharedPreferences.getString( getString( R.string.pref_zipcode ), null );

        TextView userZip = recycleView.findViewById( R.id.recycle_user_zip);

        spinner = recycleView.findViewById( R.id.recycle_spinner_category );
        RecycleSpinnerAdapter recycleSpinnerAdapter = new RecycleSpinnerAdapter( getActivity(), R.layout.adapter_recycle_spinner, createCategories() );

        spinner.setAdapter( recycleSpinnerAdapter );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                DonationCategory category = (DonationCategory) adapterView.getItemAtPosition( position );
//                TextView chosen_category = view.findViewById(R.id.recycle_spinner_category);
                TextView desctiption_view = recycleView.findViewById( R.id.recycle_txt_desc );
                desctiption_view.setText( category.get_description() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        userZip.setText( zipcode );

        // Set editText for donation amount to 0.
        EditText edit_donation_amount = recycleView.findViewById( R.id.recycle_num_quantity );
        edit_donation_amount.clearFocus();
//        edit_donation_amount.setText( "0" );

        mDatabase = FirebaseDatabase.getInstance().getReference("/");

        // Reset spinner and donate amount when user hits donate button.
        Button donate_button = recycleView.findViewById( R.id.recycle_btn_donate );
        donate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setSelection( 0 );
                EditText edit_donation_amount = recycleView.findViewById( R.id.recycle_num_quantity );
                EditText edit_description = recycleView.findViewById( R.id.recycle_edit_description );

                String type = spinner.getSelectedItem().toString();
                String desc = edit_description.getText().toString();
                int amount = Integer.parseInt( edit_donation_amount.getText().toString() );
                sendDonePost( type,  desc,  amount );
                edit_donation_amount.setText( "" );
                edit_description.setText( "" );
//                Toast.makeText( getActivity(), R.string.recycle_donation_toast, Toast.LENGTH_LONG ).show();
            }
        });

        return recycleView;
    }

    private void sendDonePost( String donation_type, String donation_description, int amount ) {
//        String url = "http://localhost:3001/api/recycling";
        String url = "http://10.0.2.2:8080/api/recycling";

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
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        categories.add( new DonationCategory( "Plastics", 0, "Gallons", " " ) );
        categories.add( new DonationCategory( "Plastic Food Containers", 0, "Gallons", " " ) );
        categories.add( new DonationCategory( "Paper", 0, "Gallons", " " ) );
        categories.add( new DonationCategory( "Glass Bottles/jars", 0, "Gallons", " " ) );
        categories.add( new DonationCategory( "Metal", 0, "Gallons", " " ) );
        categories.add( new DonationCategory( "Metal Food Cans", 0, "Gallons", " " ) );
        categories.add( new DonationCategory( "Paint", 0, "Gallons", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Household Chemicals", 0, "Gallons", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Electronics", 0, "Gallons", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Tires", 0, "Gallons", "*This item has a fee" ) );
        categories.add( new DonationCategory( "Mattresses", 0, "Gallons", "*This item has a fee" ) );

        return categories;
    }
}
