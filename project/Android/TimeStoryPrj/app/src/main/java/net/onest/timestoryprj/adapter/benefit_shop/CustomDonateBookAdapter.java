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
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.book.BookDetailActivity;
import net.onest.timestoryprj.entity.donate.BookListVO;

import java.util.ArrayList;
import java.util.List;


public class CustomDonateBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookListVO> bookList = new ArrayList<>();
    private int itemLayoutRes;
    private Activity anim;
    private Button btnBookDetail;
    private ImageView ivBookPic;
    private TextView text;
    private TextView sum;
    private TextView target;

    public CustomDonateBookAdapter(Context mContext, List<BookListVO> bookList, int itemLayoutRes, Activity anim) {
        this.mContext = mContext;
        this.bookList = bookList;
        this.itemLayoutRes = itemLayoutRes;
        this.anim = anim;
    }


    @Override
    public int getCount() {
        if (null != bookList){
            return bookList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != bookList){
            return bookList.get(position);
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
            sum = convertView.findViewById(R.id.sum);
            target = convertView.findViewById(R.id.target);
            btnBookDetail = convertView.findViewById(R.id.btn_book_detail);
        }
        text.setText(bookList.get(position).getBookName());
        sum.setText(bookList.get(position).getTotalNum() + "");
        target.setText(bookList.get(position).getGoalNum() + "");
        btnBookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_book_detail:
                        Intent intent = new Intent(mContext, BookDetailActivity.class);
                        intent.putExtra("book_id", bookList.get(position).getBookId());
                        mContext.startActivity(intent);
                        anim.overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                        break;
                }
            }
        });
        return convertView;
    }
}
