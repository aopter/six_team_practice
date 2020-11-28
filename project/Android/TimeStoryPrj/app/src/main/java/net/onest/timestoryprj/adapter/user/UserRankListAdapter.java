package net.onest.timestoryprj.adapter.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRankListAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> users;
    private int itemLayoutRes;

    public UserRankListAdapter(Context mContext, List<User> users, int itemLayoutRes) {
        this.mContext = mContext;
        this.users = users;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=users)
            return users.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null!=users)
            return users.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(itemLayoutRes, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.rankLevel.setText(users.get(position).getUserStatus().getStatusName());
        holder.rankHeader.setImageResource(R.mipmap.test_header);
        holder.rankName.setText("小美");
        holder.rankSign.setText("落红不是无情物，化作春泥更护花");

//        更改图片
        switch (position){
            case 0:
                holder.rankImage.setImageResource(R.mipmap.rank_f);
                break;
            case 1:
                holder.rankImage.setImageResource(R.mipmap.rank_s);
                break;
            case 2:
                holder.rankImage.setImageResource(R.mipmap.rank_t);
                break;
            default:
                holder.rankImage.setImageResource(R.mipmap.rank_e);
                break;
        }
        holder.rank.setText(position+1+"");
        // etc...
        return view;
    }


//    public static Bitmap drawTextToBitmap(Context gContext,
//                                          int gResId,
//                                          String text) {
////        String text = String.valueOf(gText);
//        Log.e( "drawTextToBitmap: ", text);
//
//        Resources resources = gContext.getResources();
//        float scale = resources.getDisplayMetrics().density;
//        Bitmap bitmap =
//                BitmapFactory.decodeResource(  resources, gResId);
//
//        // resource bitmaps are imutable,
//        // so we need to convert it to mutable one
//        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.BLACK);
//        paint.setTextSize((int) (12*scale));
//        paint.setDither(true);
//        paint.setFilterBitmap(true);
//        paint.setTextAlign(Paint.Align.CENTER);
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        int x = (bitmap.getWidth() - bounds.width())/2;
//        int y = (bitmap.getHeight() - bounds.height())/2;
//        canvas.drawText(text, x, y , paint);
//        return bitmap;
//    }

    // 穿件带字母的标记图片
//    private Drawable createDrawable(String letter,int width,int height,Bitmap imgMarker) {
//        Bitmap imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(imgTemp);
//        Paint paint = new Paint(); // 建立画笔
//        paint.setDither(true);
//        paint.setFilterBitmap(true);
//        Rect src = new Rect(0, 0, width, height);
//        Rect dst = new Rect(0, 0, width, height);
//        canvas.drawBitmap(imgMarker, src, dst, paint);
//
//        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
//                | Paint.DEV_KERN_TEXT_FLAG);
//        textPaint.setTextSize(10);
//        textPaint.setTypeface(Typeface.DEFAULT); // 采用默认的宽度
//        textPaint.setColor(Color.WHITE);
//        canvas.drawText(String.valueOf(letter), width / 2 - 5, height / 2 + 5,
//                textPaint);
//        canvas.save();
//        canvas.restore();
//        Log.e("createDrawable: ", "之心");
//        return (Drawable) new BitmapDrawable(mContext.getResources(), imgTemp);
//    }


        static class ViewHolder {
        @BindView(R.id.rank_header)
        ImageView rankHeader;
        @BindView(R.id.tv_user_level)
        TextView rankLevel;
        @BindView(R.id.rank_name)
        TextView rankName;
        @BindView(R.id.rank_sign)
        TextView rankSign;

        @BindView(R.id.rank_img)
        ImageView rankImage;

        @BindView(R.id.rank_rank)
        TextView rank;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
