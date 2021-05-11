package net.onest.timestoryprj.adapter.dynasty;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.dynasty.EventDialogActivity;
import net.onest.timestoryprj.entity.Incident;
import net.onest.timestoryprj.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private List<Incident> incidents;
    private int layout;
    private TextView tvIncidentName;
    private ImageView ivIncidentImg;
    private String dynastyId;
    private String dynastyName;
    private Activity anim;
    public EventListAdapter(Context context, List<Incident> incidents, int layout, String dynastyId, String dynastyName, Activity anim) {
        this.context = context;
        this.incidents = incidents;
        this.layout = layout;
        this.dynastyId = dynastyId;
        this.dynastyName = dynastyName;
        this.anim = anim;
    }

    @Override
    public int getCount() {
        return incidents.size();
    }

    @Override
    public Object getItem(int position) {
        return incidents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            tvIncidentName = convertView.findViewById(R.id.tv_incident_name);
            ivIncidentImg = convertView.findViewById(R.id.iv_incident_img);
        }
        AssetManager assets = context.getAssets();
        final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
        String[] pics = incidents.get(position).getIncidentPicture().split(Constant.DELIMITER);
        Glide.with(context).load(ServiceConfig.SERVICE_ROOT + "/img/" + pics[3]).into(ivIncidentImg);
        tvIncidentName.setTypeface(typeface);
        tvIncidentName.setText(incidents.get(position).getIncidentName());
        List<Integer> unlockDynastyIncident = new ArrayList<>();
        for (int j = 0; j < Constant.UnlockDynastyIncident.size();j++){
            Log.e("size", String.valueOf(Constant.UnlockDynastyIncident.size()));
            unlockDynastyIncident.add(Constant.UnlockDynastyIncident.get(j).getIncidentId());
        }
        for (int j = 0; j < unlockDynastyIncident.size();j++){
            if (position == getItemId(unlockDynastyIncident.get(j)-1)){
                tvIncidentName.setTextColor(Color.BLACK);
            }
        }

<<<<<<< Updated upstream
<<<<<<< HEAD
=======
        tvIncidentName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN: //按住执行
                        if(view.getId() == R.id.tv_incident_name){
                            int size1 = DensityUtil.dip2px(context, 40);
//                            tvIncidentName.setScaleX((float) 0.7);
//                            tvIncidentName.setScaleY((float) 0.7);
                            tvIncidentName.setTextColor(Color.parseColor("#AE5B41"));
                            Log.e("执行", "run");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(view.getId() == R.id.tv_incident_name){
                            Log.e("停止", "stop");
                            int size2 = DensityUtil.dip2px(context, 25);
//                            tvIncidentName.setScaleY(1);
//                            tvIncidentName.setScaleX(1);
                            tvIncidentName.setTextColor(Color.parseColor("#000000"));
                        }
                        break;
                }
                return false;
            }
        });
        ivIncidentImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN: //按住执行
                        int size1 = DensityUtil.dip2px(context, 100);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ivIncidentImg.getLayoutParams());
                        params.width = size1;
                        params.height = size1;
                        ivIncidentImg.setLayoutParams(params);
                        Log.e("执行", "run");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("停止", "stop");
                        int size2 = DensityUtil.dip2px(context, 80);
                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ivIncidentImg.getLayoutParams());
                        params1.width = size2;
                        params1.height = size2;
                        ivIncidentImg.setLayoutParams(params1);
                        break;
                        default:
                            break;
                }
                return false;
            }
        });
>>>>>>> c481c8f1baec166589edef760b0b6ecc407c2a31
=======
//        tvIncidentName.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN: //按住执行
//                        if(view.getId() == R.id.tv_incident_name){
//                            int size1 = DensityUtil.dip2px(context, 40);
////                            tvIncidentName.setScaleX((float) 0.7);
////                            tvIncidentName.setScaleY((float) 0.7);
//                            tvIncidentName.setTextColor(Color.parseColor("#AE5B41"));
//                            Log.e("执行", "run");
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if(view.getId() == R.id.tv_incident_name){
//                            Log.e("停止", "stop");
//                            int size2 = DensityUtil.dip2px(context, 25);
////                            tvIncidentName.setScaleY(1);
////                            tvIncidentName.setScaleX(1);
//                            tvIncidentName.setTextColor(Color.parseColor("#000000"));
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
//        ivIncidentImg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN: //按住执行
//                        int size1 = DensityUtil.dip2px(context, 100);
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ivIncidentImg.getLayoutParams());
//                        params.width = size1;
//                        params.height = size1;
//                        ivIncidentImg.setLayoutParams(params);
//                        Log.e("执行", "run");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.e("停止", "stop");
//                        int size2 = DensityUtil.dip2px(context, 80);
//                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ivIncidentImg.getLayoutParams());
//                        params1.width = size2;
//                        params1.height = size2;
//                        ivIncidentImg.setLayoutParams(params1);
//                        break;
//                        default:
//                            break;
//                }
//                return false;
//            }
//        });

//        tvIncidentName.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN: //按住执行
//                        if(view.getId() == R.id.tv_incident_name){
//                            int size1 = DensityUtil.dip2px(context, 40);
////                            tvIncidentName.setScaleX((float) 0.7);
////                            tvIncidentName.setScaleY((float) 0.7);
//                            tvIncidentName.setTextColor(Color.parseColor("#AE5B41"));
//                            Log.e("执行", "run");
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if(view.getId() == R.id.tv_incident_name){
//                            Log.e("停止", "stop");
//                            int size2 = DensityUtil.dip2px(context, 25);
////                            tvIncidentName.setScaleY(1);
////                            tvIncidentName.setScaleX(1);
//                            tvIncidentName.setTextColor(Color.parseColor("#000000"));
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
//        ivIncidentImg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN: //按住执行
//                        int size1 = DensityUtil.dip2px(context, 100);
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ivIncidentImg.getLayoutParams());
//                        params.width = size1;
//                        params.height = size1;
//                        ivIncidentImg.setLayoutParams(params);
//                        Log.e("执行", "run");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.e("停止", "stop");
//                        int size2 = DensityUtil.dip2px(context, 80);
//                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ivIncidentImg.getLayoutParams());
//                        params1.width = size2;
//                        params1.height = size2;
//                        ivIncidentImg.setLayoutParams(params1);
//                        break;
//                        default:
//                            break;
//                }
//                return false;
//            }
//        });
>>>>>>> Stashed changes

        tvIncidentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转
                Intent intent = new Intent();
                intent.setClass(context, EventDialogActivity.class);
                intent.putExtra("dynastyId2", dynastyId);
                intent.putExtra("dynastyName", dynastyName);
                intent.putExtra("incidentId", incidents.get(position).getIncidentId().toString());
                context.startActivity(intent);
                anim.overridePendingTransition(R.anim.anim_in_right,R.anim.anim_out_left);
            }
        });
        ivIncidentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转
                Intent intent = new Intent();
                intent.setClass(context, EventDialogActivity.class);
                intent.putExtra("incidentId", incidents.get(position).getIncidentId().toString());
                intent.putExtra("dynastyId2", dynastyId);
                intent.putExtra("dynastyName", dynastyName);
                context.startActivity(intent);
                anim.overridePendingTransition(R.anim.anim_in_right,R.anim.anim_out_left);
            }
        });
        return convertView;
    }
}
