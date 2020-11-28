package net.onest.timestoryprj.adapter.dynasty;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.constant.ServiceConfig;
import net.onest.timestoryprj.dialog.dynasty.EventDialogActivity;
import net.onest.timestoryprj.entity.Incident;

import java.util.List;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private List<Incident> incidents;
    private int layout;
    private TextView tvIncidentName;
    private ImageView ivIncidentImg;
    private String dynastyId;
    public EventListAdapter(Context context, List<Incident> incidents, int layout, String dynastyId) {
        this.context = context;
        this.incidents = incidents;
        this.layout = layout;
        this.dynastyId = dynastyId;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            tvIncidentName = convertView.findViewById(R.id.tv_incident_name);
            ivIncidentImg = convertView.findViewById(R.id.iv_incident_img);
        }
        AssetManager assets = context.getAssets();
        final Typeface typeface = Typeface.createFromAsset(assets, "fonts/custom_font.ttf");
        Glide.with(context).load(ServiceConfig.SERVICE_ROOT + "/picture/download/incident/inc-" + (position + 1) + ".png").into(ivIncidentImg);
        tvIncidentName.setTypeface(typeface);
        tvIncidentName.setText(incidents.get(position).getIncidentName());
        tvIncidentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转
                Intent intent = new Intent();
                intent.setClass(context, EventDialogActivity.class);
                intent.putExtra("dynastyId2", dynastyId);
                intent.putExtra("incidentId", incidents.get(position).getIncidentId().toString());
                context.startActivity(intent);
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
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
