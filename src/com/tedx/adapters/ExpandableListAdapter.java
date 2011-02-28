package com.tedx.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.catchnotes.tedapp.R;
import com.tedx.objects.SessionResult;
import com.tedx.utility.RemoteImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends SimpleExpandableListAdapter {

    private Context context;

    private List<HashMap<String, String>> groups;

    private List<List<HashMap<String, String>>> children;
    
    @SuppressWarnings("unchecked")
	public ExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTon) {
    	super(  context,
    			groupData,
    			groupLayout,
    			groupFrom,
    			groupTo,
                childData,
                childLayout,
                childFrom,
                childTon);
    	
        this.context = context;
        this.groups = (List<HashMap<String, String>>) groupData;
        this.children = (List<List<HashMap<String, String>>>) childData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
		return children.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    // Return a child view. You can load your custom layout here.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {

		View ret = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
		if (ret != null) {
			RemoteImageView riv = (RemoteImageView) ret.findViewById(android.R.id.icon);
			if (riv != null) {
				riv.loadImage();
			}
		}
		
		return ret;
    }
    
    

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public HashMap<String, String> getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Return a group view. You can load your custom layout here.
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        String group = getGroup(groupPosition).get(SessionResult.SESSIONNAME);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sessiongroup, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}
