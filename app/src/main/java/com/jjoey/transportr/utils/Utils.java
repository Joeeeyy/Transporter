package com.jjoey.transportr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Utils {

    public static boolean isNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return cm != null && networkInfo.isConnected();
    }

    public static byte[] bitmapToByteArray(Bitmap cameraBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static File uriToFile(Uri uri){
        return new File(uri.toString());
    }

}
