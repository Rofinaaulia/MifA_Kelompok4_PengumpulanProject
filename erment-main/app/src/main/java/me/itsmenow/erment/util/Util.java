package me.itsmenow.erment.util;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class Util {
    public static String formattedMoney(int number){
        return String.format("Rp. %s-", new DecimalFormat("#,###.").format(number));
    }

    public static boolean isValidUsername(CharSequence username){
        return (!TextUtils.isEmpty(username) && TextUtils.getTrimmedLength(username) >= 4);
    }

    public static boolean isValidPassword(CharSequence password){
        return (!TextUtils.isEmpty(password) && TextUtils.getTrimmedLength(password) >= 3);
    }

    public static boolean isValidEmail(CharSequence email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isEmpty(TextInputLayout inputLayout){
        return (TextUtils.isEmpty(inputLayout.getEditText().getText().toString()));
    }
}
