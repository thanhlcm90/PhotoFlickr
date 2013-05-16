package com.example.photoflickr.ui;

import org.holoeverywhere.app.AlertDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.example.photoflickr.R;

public class PhotoUploadDialog extends SherlockDialogFragment {
	private String photoTitle, photoDescription;
	
	 /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface PhotoUploadDialogListener {
        public void onDialogPositiveClick(SherlockDialogFragment dialog);
        public void onDialogNegativeClick(SherlockDialogFragment dialog);
    }
    
    // Use this instance of the interface to deliver action events
    PhotoUploadDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PhotoUploadDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View v = (View)inflater.inflate(R.layout.dialog_photoupload, null);
		final EditText etTitle = (EditText) v.findViewById(R.id.photo_title);
		final EditText etDescription = (EditText) v.findViewById(R.id.photo_description);
		
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(v)
				.setTitle(R.string.photo_detail)
				// Add action buttons
				.setPositiveButton(R.string.button_upload,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								photoTitle=etTitle.getText().toString();
								photoDescription=etDescription.getText().toString();
								mListener.onDialogPositiveClick(PhotoUploadDialog.this);
							}
						})
				.setNegativeButton(R.string.button_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogNegativeClick(PhotoUploadDialog.this);
							}
						});
		return builder.create();
	}

	public String GetPhotoTitle() {
		return photoTitle;
	}

	public String GetPhotoDescription() {
		return photoDescription;
	}
}
