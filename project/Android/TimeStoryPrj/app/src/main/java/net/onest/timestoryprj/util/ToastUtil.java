package net.onest.timestoryprj.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.timestoryprj.R;

public class ToastUtil {
    public static void showToast(Context context,String message,int length){
        Toast toast = new Toast(context);
        View view = View.inflate(context, R.layout.toast_test,null);
        TextView textView = view.findViewById(R.id.toast_message);
        textView.setText(message);
        toast.setDuration(length);
        toast.setView(view);
        toast.setGravity(1,0,250);
        toast.show();
    }
}
