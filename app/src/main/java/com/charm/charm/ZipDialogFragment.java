package com.charm.charm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ZipDialogFragment extends DialogFragment {

    public interface DialogListener {
        public void onDialogPositiveClick( DialogFragment dialog );
    }

    private DialogListener listener;
    private View view;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        // Use the Builder class for convenient dialof construction
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate( R.layout.dialog_zipcode, null );

        builder.setView( view );

        builder.setPositiveButton(R.string.zip_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText enteredZip = view.findViewById( R.id.zip_edit_zipcode );
                String zipcode = enteredZip.getText().toString();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString( getString( R.string.pref_zipcode ), zipcode );
                editor.apply();

                listener.onDialogPositiveClick( ZipDialogFragment.this );
            }
        });

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("The containing class must implement NoticeDialogListener");
        }
    }
}
