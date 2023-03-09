package edu.msu.sharinic.cloudhatter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.msu.sharinic.cloudhatter.Cloud.Cloud;

public class SaveDlg extends DialogFragment {

    private AlertDialog dlg;
    //private String saveName = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.save_to_title);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.save_cloud_dlg, null);
        builder.setView(view);

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Cancel just closes the dialog box
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                EditText editName = (EditText)dlg.findViewById(R.id.editName);
                save(editName.getText().toString());
            }
        });

        dlg = builder.create();

        return dlg;
    }


    /**
     * Actually save the hatting
     * @param name name to save it under
     */
    private void save(final String name) {

        if (!(getActivity() instanceof HatterActivity)) {
            return;
        }
        final HatterActivity activity = (HatterActivity) getActivity();
        final HatterView view = (HatterView) activity.findViewById(R.id.hatterView);

        // Create a thread to save the catalog
        new Thread(new Runnable() {

            @Override
            public void run() {
                Cloud cloud = new Cloud();
                final boolean ok = cloud.saveToCloud(name, view);
                if(!ok) {
                    /*
                     * If we fail to save, display a toast
                     */
                    // Please fill this in...

                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(view.getContext(),
                                    R.string.saving_fail,
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        }).start();

    }
}
