package com.charm.charm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
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

        Button donateButton = view.findViewById( R.id.home_btn_donate );

        donateButton.setOnClickListener( donateClickListener );

        return view;
    }

    private ArrayList<DonationCategory> createCategories() {
        ArrayList<DonationCategory> categories = new ArrayList<>();
        categories.add( new DonationCategory( "Paint", 219245, "Gallons" ) );
        categories.add( new DonationCategory( "Illegally Dumped Tires", 21500, "Tires" ) );
        categories.add( new DonationCategory( "Hazardous Chemicals", 111643, "Gallons" ) );
        categories.add( new DonationCategory( "Electronics", 211, "lbs." ) );
        categories.add( new DonationCategory( "Styrofoam", 2578, "lbs." ) );
        categories.add( new DonationCategory( "Metal", 231705, "lbs." ) );
        categories.add( new DonationCategory( "Mattresses", 1074, "" ) );
        categories.add( new DonationCategory( "Textiles", 67320, "lbs." ) );
        categories.add( new DonationCategory( "Single Stream", 22, "Tons" ) );
        categories.add( new DonationCategory( "Cooking Grease", 175, "Gallons" ) );
        categories.add( new DonationCategory( "Glass", 121, "Tons" ) );
        categories.add( new DonationCategory( "Visitors", 15520, "People" ) );

        return categories;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState ) {
        // Uncomment this and comment out the code below to reset the entered zip code.
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString( getString( R.string.pref_zipcode ), null );
//        editor.apply();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        String zipcode = sharedPreferences.getString( getString( R.string.pref_zipcode ), null );

        if( zipcode == null ) {
            ZipDialogFragment zipDialogFragment = new ZipDialogFragment();
            zipDialogFragment.show( getFragmentManager(), "zipcode" );
        }
    }


    private View.OnClickListener donateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PayPalConfiguration config = new PayPalConfiguration()
                    .environment( PayPalConfiguration.ENVIRONMENT_SANDBOX )
                    .clientId( getString( R.string.charm_client_id ) );

            PayPalPayment payment = new PayPalPayment( new BigDecimal( "1.00" ), "USD", "sample item", PayPalPayment.PAYMENT_INTENT_SALE );

            Intent intent = new Intent( getActivity(), PaymentActivity.class );

            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config );
            intent.putExtra( PaymentActivity.EXTRA_PAYMENT, payment );

            startActivityForResult( intent, 0 );
        }
    };

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if ( resultCode == Activity.RESULT_OK ) {
            PaymentConfirmation confirm = data.getParcelableExtra( PaymentActivity.EXTRA_RESULT_CONFIRMATION );
            if( confirm != null ) {
                Toast.makeText( getActivity(), "Payment Successful", Toast.LENGTH_LONG ).show();
            }
        } else if ( resultCode == Activity.RESULT_CANCELED ) {
            Log.i( "paymentExample", "The user canceled." );
        } else if ( resultCode == PaymentActivity.RESULT_EXTRAS_INVALID ) {
            Log.i( "paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs." );
        }
    }

}
