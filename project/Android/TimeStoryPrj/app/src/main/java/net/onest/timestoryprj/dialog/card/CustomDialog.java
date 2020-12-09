package net.onest.timestoryprj.dialog.card;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.onest.timestoryprj.R;


public class CustomDialog extends Dialog {
    private CustomDialog(Context context) {
        super(context);
    }

    private CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /* Builder */
    public static class Builder {
        private TextView tvTitle, tvContent;
        private Button btnConfirm;

        private View mLayout;

        private CustomDialog mDialog;

        public Builder(Context context) {
            mDialog = new CustomDialog(context, R.style.Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 加载布局文件
            mLayout = inflater.inflate(R.layout.dialog_tip_style1, null, false);
            // 添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            tvTitle = mLayout.findViewById(R.id.title);
            tvContent = mLayout.findViewById(R.id.message);
            btnConfirm = mLayout.findViewById(R.id.btn_confirm);
        }

        /**
         * 设置 Dialog 标题
         */
        public Builder setTitle(String title) {
            tvTitle.setText(title);
            return this;
        }

        public Builder setMessage(String message) {
            tvContent.setText(message);
            return this;
        }

        /**
         * 设置确认按钮文字和监听
         */
        public Builder setButtonConfirm(String text, View.OnClickListener listener) {
            btnConfirm.setText(text);
            btnConfirm.setOnClickListener(listener);
            return this;
        }

        public CustomDialog create() {
            mDialog.setContentView(mLayout);
            return mDialog;
        }
    }
}