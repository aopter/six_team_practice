package net.onest.timestoryprj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.onest.timestoryprj.R;

public class LoadingDialog extends Dialog {

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static class Builder {
        private View mLayout;
        private LoadingDialog mDialog;
        private ImageView loading;

        public Builder(Context context) {
            mDialog = new LoadingDialog(context, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 加载布局文件
            mLayout = inflater.inflate(R.layout.dialog_loading, null, false);
            // 添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            loading = mLayout.findViewById(R.id.loading_img);
            AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
            animationDrawable.setOneShot(false);
            animationDrawable.start();
        }


        public LoadingDialog create() {
            mDialog.setContentView(mLayout);
            return mDialog;
        }
    }
}
