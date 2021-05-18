package net.onest.timestoryprj.adapter.benefit_shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.activity.book.BookDetailActivity;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.entity.donate.BookListVO;

import java.util.ArrayList;
import java.util.List;


public class CustomDonateBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookListVO> bookList = new ArrayList<>();
    private int itemLayoutRes;
    private Activity anim;

    public CustomDonateBookAdapter(Context mContext, List<BookListVO> bookList, int itemLayoutRes, Activity anim) {
        this.mContext = mContext;
        this.bookList = bookList;
        this.itemLayoutRes = itemLayoutRes;
        this.anim = anim;
    }


    @Override
    public int getCount() {
        if (null != bookList) {
            return bookList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != bookList) {
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
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(itemLayoutRes, null);
            viewHolder = new ViewHolder();
            viewHolder.ivBookPic = convertView.findViewById(R.id.iv_book_pic);
            viewHolder.text = convertView.findViewById(R.id.text);
            viewHolder.sum = convertView.findViewById(R.id.sum);
            viewHolder.target = convertView.findViewById(R.id.target);
            viewHolder.btnBookDetail = convertView.findViewById(R.id.btn_book_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(bookList.get(position).getBookName());
        viewHolder.sum.setText(bookList.get(position).getTotalNum() + "");
        viewHolder.target.setText(bookList.get(position).getGoalNum() + "");
        Glide.with(mContext)
                .load(ServiceConfig.SERVICE_ROOT + "/img/" + bookList.get(position).getBookPic())
                .into(viewHolder.ivBookPic);
        viewHolder.btnBookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                Log.e("click", bookList.get(position).getBookId() + "");
                intent.putExtra("bookId", bookList.get(position).getBookId());
                mContext.startActivity(intent);
                anim.overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivBookPic;
        private TextView text;
        private TextView sum;
        private TextView target;
        private Button btnBookDetail;
    }
}
