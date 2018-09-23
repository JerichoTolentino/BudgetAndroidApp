package jericho.budgetapp.Presentation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class CustomAlertDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        dialog.setTitle(title);
        dialog.setMessage(message);

        return dialog.create();
    }

    public static CustomAlertDialog newInstance(String title, String message)
    {
        CustomAlertDialog customAD = new CustomAlertDialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);

        customAD.setArguments(args);

        return customAD;
    }

}
