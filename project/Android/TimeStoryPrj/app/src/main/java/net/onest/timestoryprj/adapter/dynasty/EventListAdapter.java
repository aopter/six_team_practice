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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.Constant;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.dynasty.EventDialogActivity;
import net.onest.timestoryprj.entity.Incident;

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
//            Constant.UnlockDynastyIncident
            unlockDynastyIncident.add(Constant.UnlockDynastyIncident.get(j).getIncidentId());
        }
        for (int j = 0; j < unlockDynastyIncident.size();j++){
            if (position == getItemId(unlockDynastyIncident.get(j)-1)){
                tvIncidentName.setTextColor(Color.BLACK);
            }
        }
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
