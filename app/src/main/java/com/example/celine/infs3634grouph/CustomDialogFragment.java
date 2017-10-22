package com.example.celine.infs3634grouph;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Edited by Celine on 21/10/2017.
 * refer to GOOGLE DEVELOPER DOCUMENT : https://developer.android.com/guide/topics/ui/dialogs.html
 */

public class CustomDialogFragment extends DialogFragment {
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(CustomDialogFragment dialog);

        public void onDialogNegativeClick(CustomDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
    public Spinner sp_diff;
    public Spinner sp_speed;
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);

        sp_speed = (Spinner)view.findViewById(R.id.sp_speed);
        sp_diff = (Spinner)view.findViewById(R.id.sp_diffi);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.arr_difficulty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_diff.setAdapter(adapter);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.arr_speed, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_speed.setAdapter(adapter2);

        builder.setView(view).setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(CustomDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(CustomDialogFragment.this);
                    }
                });
        return builder.create();
    }

}
