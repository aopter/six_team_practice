package net.onest.timestoryprj.dialog.problem;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import net.onest.timestoryprj.R;

public class ProblemAnswerDialog extends Dialog {

    private String detail;
    private Context context;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        Log.e("setDetail: ", "111");
    }

    public ProblemAnswerDialog(@NonNull Context context) {
        super(context);
        this.context = context;


    }


    private TextView tv_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_problem_answer);
        tv_detail = findViewById(R.id.problem_answer_details);

        tv_detail.setText(detail);

    }
    //    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.dialog_problem_answer,container,false);
//
//
//
//        return view;
//
//
//    }
}
