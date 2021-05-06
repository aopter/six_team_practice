package net.onest.timestoryprj.adapter.benefit_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.donate.BookListVO;

import java.util.ArrayList;
import java.util.List;


public class CustomDonateBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookListVO> bookList = new ArrayList<>();
    private int itemLayoutRes;
    private ImageView ivBookPic;
    private TextView text;
    private TextView sum;
    private TextView target;

    public CustomDonateBookAdapter(Context mContext, List<BookListVO> bookList, int itemLayoutRes) {
        this.mContext = mContext;
        this.bookList = bookList;
        this.itemLayoutRes = itemLayoutRes;
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
        }
        text.setText(bookList.get(position).getBookName());
        sum.setText(bookList.get(position).getTotalNum() + "");
        target.setText(bookList.get(position).getGoalNum() + "");
        return convertView;
    }
}
