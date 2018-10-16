package com.charm.charm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class DonateDialogFragment extends DialogFragment {
    private View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState ) {
        // Use the Builder class for convenient dialof construction
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate( R.layout.dialog_donate, null );

        setCancelable( false );

        builder.setView( dialogView );

        EditText donationAmountView = dialogView.findViewById( R.id.donate_edit_amount );

        // Set focus to this element.
        donationAmountView.requestFocus();

        builder.setPositiveButton(R.string.zip_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // This is handled in the onStart cycle.
            }
        });

        builder.setNegativeButton(R.string.donate_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // This is handled in the onStart cycle.
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();

        if( dialog != null ) {
            Button positiveButton = dialog.getButton( Dialog.BUTTON_POSITIVE );
            Button negativeButton = dialog.getButton( Dialog.BUTTON_NEGATIVE );

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText donationAmountView = dialogView.findViewById( R.id.donate_edit_amount );
                    EditText donationNoteView = dialogView.findViewById( R.id.donate_edit_note );

                    String donationAmount = donationAmountView.getText().toString();
                    String donationNote = donationNoteView.getText().toString();

                    if( donationAmount.isEmpty() ) {
                        Toast.makeText( getActivity(), R.string.donate_empty_amount, Toast.LENGTH_LONG ).show();
                        return;
                    }

                    if( donationNote.isEmpty() ) {
                        donationNote = "CHaRM Donation";
                    }

                    PayPalConfiguration config = new PayPalConfiguration()
                            .environment( PayPalConfiguration.ENVIRONMENT_SANDBOX )
                            .clientId( getString( R.string.charm_client_id ) );


                    PayPalPayment payment = new PayPalPayment( new BigDecimal( donationAmount ), "USD", donationNote, PayPalPayment.PAYMENT_INTENT_SALE );

                    Intent intent = new Intent( getActivity(), PaymentActivity.class );

                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config );
                    intent.putExtra( PaymentActivity.EXTRA_PAYMENT, payment );

                    startActivityForResult( intent, 0 );
                    dismiss();
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }
}
