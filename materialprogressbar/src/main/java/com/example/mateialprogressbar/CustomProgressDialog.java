package com.example.mateialprogressbar;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by admin on 6/27/2016.
 */
public class CustomProgressDialog extends Dialog {
    MaterialProgressBar progress1;

    Context mContext;
    CustomProgressDialog dialog;
    public CustomProgressDialog(Context context) {
        super(context);
        this.mContext=context;
    }
    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomProgressDialog show(CharSequence message) {

        dialog = new CustomProgressDialog (mContext,R.style.ProgressDialog);
        dialog.setContentView(R.layout.view_material_progress);

        progress1 = (MaterialProgressBar) dialog.findViewById (R.id.progress1);


        progress1.setColorSchemeResources(R.color.red,R.color.green,R.color.blue,R.color.orange);
        dialog.setCancelable(false);
        if(dialog!= null) {
            dialog.show ();
        }
        return dialog;
    }
    public CustomProgressDialog dismiss(CharSequence message) {
        if(dialog!=null) {
            dialog.dismiss();
        }

        return dialog;

    }


}