package net.onest.timestoryprj.adapter.dynasty;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.onest.timestoryprj.R;
import net.onest.timestoryprj.entity.SearchIncident;
import net.onest.timestoryprj.entity.donate.CertificateUserBookListVO;

import java.util.ArrayList;
import java.util.List;

public class TextSearchAdapter extends BaseAdapter {
    private Context context;
    private List<SearchIncident> searchIncidents = new ArrayList<>();
    private int itemLayoutRes;

    public TextSearchAdapter(Context context, List<SearchIncident> searchIncidents, int itemLayoutRes) {
        this.context = context;
        this.searchIncidents = searchIncidents;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != searchIncidents) {
            return searchIncidents.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != searchIncidents) {
            return searchIncidents.get(i);
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
            TextView incidentName = view.findViewById(R.id.incident_name);
            TextView dynastyName = view.findViewById(R.id.dynasty_name);
            TextView incidentInfo = view.findViewById(R.id.incident_info);
            if (searchIncidents.get(i).isFlag() == false) {
                incidentName.setTextColor(Color.GRAY);
                incidentInfo.setTextColor(Color.GRAY);
            }
            incidentName.setText(Html.fromHtml(searchIncidents.get(i).getIncidentName()));
            dynastyName.setText(searchIncidents.get(i).getDynastyName());
            incidentInfo.setText(Html.fromHtml(searchIncidents.get(i).getIncidentInfo() + "......"));
        }
        return view;
    }
}
