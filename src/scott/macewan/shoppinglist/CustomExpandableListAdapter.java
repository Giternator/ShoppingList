package scott.macewan.shoppinglist;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter{
	
	public List<Category> categories;
	public List<Item> tempChild;
	public List<List<Item>> childItems;
	public LayoutInflater minflater;
	public Activity activity;
	
	
	public CustomExpandableListAdapter(List<Category> categories, List<List<Item>> items){
		this.categories = categories;
		childItems = items;
	}
	
	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}
	
	public Item getChild(int groupPosition, int childPosition) {
		return childItems.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childItems.get(groupPosition).get(childPosition).getId();
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		tempChild =  (List<Item>) childItems.get(groupPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.childrow, null);
		}
		Log.d("ListView","Creating Children");
		text = (TextView) convertView.findViewById(R.id.childTextView);
		text.setText(tempChild.get(childPosition).getName());
		final DatabaseHandler db = new DatabaseHandler(convertView.getContext());
		final boolean selected = db.itemSelected(tempChild.get(childPosition).getCategoryId());
		final int position = childPosition;
		final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.childSelected);
		checkbox.setChecked(selected);
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				if(checkbox.isActivated()){
					checkbox.setActivated(false);
					db.setSelected(tempChild.get(position).getId(), false);
				}else{
					checkbox.setActivated(true);
					db.setSelected(tempChild.get(position).getId(), true);
				}
			*/
				Toast.makeText(activity, tempChild.get(position).getName(),
						Toast.LENGTH_SHORT).show();
						
			}
		});		
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return ((List<Item>) childItems.get(groupPosition)).size();
	}
	
	public Category getGroup(int groupPosition) {
		return categories.get(groupPosition);
	}

	public int getGroupCount() {
		return categories.size();
	}

	public long getGroupId(int groupPosition) {
		return categories.get(groupPosition).getId();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Log.d("ListView","Creating Parent");
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.parentrow, null);
		}
		
		((CheckedTextView) convertView).setText(categories.get(groupPosition).getName());
		((CheckedTextView) convertView).setChecked(true);
		ExpandableListView listView = (ExpandableListView) parent;
		listView.expandGroup(groupPosition);
		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
