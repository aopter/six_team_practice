package net.onest.timestoryprj.adapter.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.CertificateUserBookListVO;

import java.util.ArrayList;
import java.util.List;


public class BookDatailAdapter extends BaseAdapter {
    private Context context;
    private List<CertificateUserBookListVO> bookListVOs = new ArrayList<>();
    private int itemLayoutRes;

    public BookDatailAdapter(Context context, List<CertificateUserBookListVO> bookListVOs, int itemLayoutRes) {
        this.context = context;
        this.bookListVOs = bookListVOs;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != bookListVOs){
            return bookListVOs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != bookListVOs){
            return bookListVOs.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(itemLayoutRes,null);
        TextView tvTime = view.findViewById(R.id.tv_time);
        TextView tvDetail = view.findViewById(R.id.tv_detail);
        tvTime.setText(bookListVOs.get(i).getDonateTime());
        tvDetail.setText("进展详情："+bookListVOs.get(i).getDonateTime()+"向"+bookListVOs.get(i).getDonateObject()+"捐赠图书，同时学生们也录制了视频表示感谢");
        return view;
    }
}
