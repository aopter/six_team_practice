package net.onest.timestoryprj.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.onest.timestoryprj.R;

public class ToastUtil {
    public static void showEncourageToast(Context context, String message, int length) {
        Toast toast = new Toast(context);
        View view = View.inflate(context, R.layout.toast_img_right, null);
        TextView textView = view.findViewById(R.id.toast_message);
        ImageView imageView = view.findViewById(R.id.iv_expression);
        imageView.setBackground(context.getResources().getDrawable(R.mipmap.toast_encourage));
        textView.setText(message);
        toast.setDuration(length);
        toast.setView(view);
        toast.setGravity(1, 0, 250);
        toast.show();
    }

    public static void showCryToast(Context context, String message, int length) {
        Toast toast = new Toast(context);
        View view = View.inflate(context, R.layout.toast_img_left, null);
        TextView textView = view.findViewById(R.id.toast_message);
        ImageView imageView = view.findViewById(R.id.iv_expression);
        imageView.setBackground(context.getResources().getDrawable(R.mipmap.toast_cry));
        textView.setText(message);
        toast.setDuration(length);
        toast.setView(view);
        toast.setGravity(1, 0, 250);
        toast.show();
    }

    public static void showProblemYesToast(Context context, String message, int length) {
        Toast toast = new Toast(context);
        View view = View.inflate(context, R.layout.toast_problem, null);
        TextView textView = view.findViewById(R.id.toast_message);
        textView.setText(message);
        toast.setDuration(length);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showSickToast(Context context, String message, int length) {
        Toast toast = new Toast(context);
        View view = View.inflate(context, R.layout.toast_img_right, null);
        TextView textView = view.findViewById(R.id.toast_message);
        ImageView imageView = view.findViewById(R.id.iv_expression);
        imageView.setBackground(context.getResources().getDrawable(R.mipmap.toast_sorry));
        textView.setText(message);
        toast.setDuration(length);
        toast.setView(view);
        toast.setGravity(1, 0, 250);
        toast.show();
    }
}
