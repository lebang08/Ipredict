package com.woyuce.activity.Utils;

/**
 * Toast工具类
 */

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {  

    private static Toast toast = null;  

    public static void showMessage(Context context, String msg) {  
        showMessage(context, msg, Toast.LENGTH_SHORT);  
    }  

    public static void showMessage(Context context, final String msg,  final int len) {  
        if (toast == null) 
        	toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        else 
        	toast.setText(msg);
        toast.show();
    }  
}