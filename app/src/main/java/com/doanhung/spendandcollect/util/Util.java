package com.doanhung.spendandcollect.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.doanhung.spendandcollect.data.repository.AuthRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Util {
    public static boolean isPhoneNumber(String phoneNumber) {
        return Pattern.matches("^\\+84\\d{9,10}", phoneNumber);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    private final static String IS_FIRST_TIME_OPEN_APP = "isFirstTimeOpenApp";
    private final static String TOKEN = "token";
    private final static String AUTH_ID = "authID";

    public static Boolean checkIsFirstOpenApp(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(IS_FIRST_TIME_OPEN_APP, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_TIME_OPEN_APP, true);
    }

    public static void markOpenedFirstTimeApp(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(IS_FIRST_TIME_OPEN_APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FIRST_TIME_OPEN_APP, false);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static String getAuthId(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(AUTH_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AUTH_ID, "");
    }

    public static void setAuthId(Context context, String authId) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(AUTH_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_ID, authId);
        editor.apply();
    }


    public static String getPathFromUri(Context context, Uri uri) {
        String result = "";
        @SuppressLint("Recycle")
        Cursor cursor = context.getContentResolver().query(
                uri,
                new String[]{MediaStore.Images.Media.DATA},
                null,
                null,
                null
        );
        if(cursor.moveToFirst()) {
            result =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return result;
    }



}
