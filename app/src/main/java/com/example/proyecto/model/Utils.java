package com.example.proyecto.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils
{
    private static final String SHARED_PREFERENCES = "prefs";
    private static String server;
    private static long employeeId;

    public static String getServer()
    {
        return server;
    }

    public static void setServer(String server)
    {
        Utils.server = server;
    }

    public static String getServerUrl()
    {
        return "http://" + server + "/web/RestaurantProject/public/";
    }

    public static long getEmployeeId()
    {
        return employeeId;
    }

    public static void setEmployeeId(long employeeId)
    {
        Utils.employeeId = employeeId;
    }

    public static String getSharedPreferences()
    {
        return SHARED_PREFERENCES;
    }

    public static void toggleActivityTouchable(Activity activity, boolean toggle)
    {
        if(toggle) activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        else activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static File saveSelectedImageInFile(Context context, Uri uri)
    {
        File file = null;
        Bitmap bitmap = null;

        if(Build.VERSION.SDK_INT < 28)
        {
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            }
            catch (IOException e)
            {
                bitmap = null;
            }
        }
        else
        {
            try
            {
                InputStream in = context.getContentResolver().openInputStream(uri);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            }
            catch (IOException e)
            {
                bitmap = null;
            }
        }

        if(bitmap != null)
            file = saveBitmapInFile(context, bitmap);

        return file;
    }

    private static File saveBitmapInFile(Context context, Bitmap bitmap)
    {
        File file = new File(context.getFilesDir(), "temp.jpg");
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            file = null;
        }
        return file;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentTime()
    {
        String time;
        Calendar calendar = Calendar.getInstance();
        time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        return time;
    }
}
