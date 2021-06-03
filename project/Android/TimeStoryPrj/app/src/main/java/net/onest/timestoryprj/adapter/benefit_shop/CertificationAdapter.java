package net.onest.timestoryprj.adapter.benefit_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.donate.CertificateUserBookListVO;

import java.util.ArrayList;
import java.util.List;

public class CertificationAdapter extends BaseAdapter {
    private Context context;
    private List<CertificateUserBookListVO> certificateUserBookListVOS = new ArrayList<>();
    private int itemLayoutRes;

    public CertificationAdapter(Context context, List<CertificateUserBookListVO> certificateUserBookListVOS, int itemLayoutRes) {
        this.context = context;
        this.certificateUserBookListVOS = certificateUserBookListVOS;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != certificateUserBookListVOS) {
            return certificateUserBookListVOS.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != certificateUserBookListVOS) {
            return certificateUserBookListVOS.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(itemLayoutRes, null);
            TextView tvTime = view.findViewById(R.id.tv_time);
            TextView tvDetail = view.findViewById(R.id.tv_detail);
            tvTime.setText(certificateUserBookListVOS.get(i).getDonateTime());
            tvDetail.setText("您向'" + certificateUserBookListVOS.get(i).getDonateObject() + "'捐赠了一本《" + certificateUserBookListVOS.get(i).getBookName() + "》，学生们录制了视频向您表示感谢");
        }
        return view;
    }
}
