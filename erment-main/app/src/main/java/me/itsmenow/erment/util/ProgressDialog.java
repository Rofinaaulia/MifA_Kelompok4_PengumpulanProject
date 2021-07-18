package me.itsmenow.erment.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import me.itsmenow.erment.R;

public class ProgressDialog {
    Activity activity;
    Dialog dialog;
    public ProgressDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}
