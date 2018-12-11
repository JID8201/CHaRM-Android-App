package com.charm.charm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ZipDialogFragment extends DialogFragment {

    public interface DialogListener {
        public void onDialogPositiveClick( DialogFragment dialog );
        public void onDialogCancel( DialogFragment dialog );
    }

    private DialogListener listener;
    private View view;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        // Use the Builder class for convenient dialof construction
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate( R.layout.dialog_zipcode, null );

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getString( R.string.pref_preferences ), Context.MODE_PRIVATE );
        String current_zip = sharedPreferences.getString( getString( R.string.pref_zipcode ), "" );

        // If our zip code is already set, show it, and change the title to better reflect user actions.
        if( current_zip != null )
        {
            EditText zip_edit_text = view.findViewById( R.id.zip_edit_zipcode );
            TextView zip_title = view.findViewById( R.id.zip_text_title );

            zip_title.setText( R.string.zip_title_edit );
            zip_edit_text.setText( current_zip );
        }

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

        // Handle case when the user does not enter in their zip code.
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                listener.onDialogCancel( ZipDialogFragment.this );
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
