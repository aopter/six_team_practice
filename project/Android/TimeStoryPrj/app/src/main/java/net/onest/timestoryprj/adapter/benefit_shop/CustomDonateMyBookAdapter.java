package net.onest.timestoryprj.adapter.benefit_shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.benefit_shop.DonateCardActivity;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.donate.UserBookListVO;

import java.util.ArrayList;
import java.util.List;

public class CustomDonateMyBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserBookListVO> userBookList = new ArrayList<>();
    private int itemLayoutRes;
    private ProgressBar progressBar;
    private ImageView ivBookPic;
    private Activity anim;
    private TextView text;
    private TextView processText;
    private Button btnDonateCard;

    public CustomDonateMyBookAdapter(Context mContext, List<UserBookListVO> userBookList, int itemLayoutRes, Activity anim) {
        this.mContext = mContext;
        this.userBookList = userBookList;
        this.itemLayoutRes = itemLayoutRes;
        this.anim = anim;
    }

    @Override
    public int getCount() {
        if (null != userBookList){
            return userBookList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != userBookList){
            return userBookList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(itemLayoutRes, null);
            ivBookPic = convertView.findViewById(R.id.iv_book_pic);
            text = convertView.findViewById(R.id.text);
            btnDonateCard = convertView.findViewById(R.id.btn_donate_card);
            progressBar = convertView.findViewById(R.id.process);
            processText = convertView.findViewById(R.id.process_text);
        }
        initProgress(position);
//        Glide.with(mContext)
//                .load(ServiceConfig.SERVICE_ROOT + "/img/" + userBookList.get(position).getBookListVO().getBookPic())
//                .into(ivBookPic);
        text.setText(userBookList.get(position).getBookListVO().getBookName());
        processText.setText(userBookList.get(position).getProcess() + "");

        btnDonateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_donate_card:
                        Intent intent = new Intent(mContext, DonateCardActivity.class);
                        intent.putExtra("processId", userBookList.get(position).getProcessId());
                        intent.putExtra("process", userBookList.get(position).getProcess());
                        mContext.startActivity(intent);
                        anim.overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                        break;
                }
            }
        });
        return convertView;
    }
    /**
     * 初始化进度条
     */
    private void initProgress(int position) {
        int rate = userBookList.get(position).getProcess();
        int progress = rate * 100;
        progressBar.setProgress(progress);
    }
}
