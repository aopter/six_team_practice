package net.onest.timestoryprj.dialog.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.user.LoginActivity;
import net.onest.timestoryprj.constant.Constant;

public class CustomDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog,container,false);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText("退出登录");
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        tvMessage.setText("您确定要退出登录吗?");
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭对话框
                getDialog().dismiss();
            }
        });
        Button btnYes = view.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.User = null;
                Intent intent = new Intent(getContext(),LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
}
