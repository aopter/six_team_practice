package net.onest.timestoryprj.adapter.problem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.onest.timestoryprj.R;

import java.util.HashMap;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private String[] groups;
    private String[][] infos;
    private Context context;

    public MyExpandableListAdapter() {

    }

    public MyExpandableListAdapter(String[] groups, String[][] infos, Context context) {
        this.groups = groups;
        this.infos = infos;
        this.context = context;
    }

    @Override
    public int getGroupCount() {    //组的数量
        return groups.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {    //某组中子项数量
        return infos[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {     //某组
        return groups[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {  //某子项
        return infos[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_elv_group, null);
            groupHold = new GroupHold();
            groupHold.tvGroupName = (TextView) convertView.findViewById(R.id.tv_groupName);
            groupHold.ivGoToChildLv = (ImageView) convertView.findViewById(R.id.iv_goToChildLV);

            convertView.setTag(groupHold);

        } else {
            groupHold = (GroupHold) convertView.getTag();

        }

        String groupName = groups[groupPosition];
        groupHold.tvGroupName.setText(groupName);

        //取消默认的groupIndicator后根据方法中传入的isExpand判断组是否展开并动态自定义指示器
        if (isExpanded) {   //如果组展开
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.el_group_icon_ex);
        } else {
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.el_group_icon);
        }

        //setTag() 方法接收的类型是object ，所以可将position和converView先封装在Map中。Bundle中无法封装view,所以不用bundle
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put("groupPosition", groupPosition);
        tagMap.put("isExpanded", isExpanded);
        groupHold.ivGoToChildLv.setTag(tagMap);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildHold childHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_elv_child, null);
            childHold = new ChildHold();
            childHold.tvChildName = (TextView) convertView.findViewById(R.id.tv_elv_childName);
            convertView.setTag(childHold);
        } else {
            childHold = (ChildHold) convertView.getTag();
        }

        String childName = infos[groupPosition][childPosition];
        childHold.tvChildName.setText(childName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;    //默认返回false,改成true表示组中的子条目可以被点击选中
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class GroupHold {
        TextView tvGroupName;
        ImageView ivGoToChildLv;
    }

    class ChildHold {
        TextView tvChildName;

    }
}