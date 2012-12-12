package scott.macewan.shoppinglist;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
		//get the current child to populate view
		tempChild =  (List<Item>) childItems.get(groupPosition);
		Log.d("ListView","Init: "+Integer.toString(tempChild.size()));
		Log.d("ListView","Group Position: "+Integer.toString(groupPosition));
		TextView text = null;
		//if not infalted then inflate
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.childrow, null);
		}
		//Log.d("ListView","Creating Children");
		
		text = (TextView) convertView.findViewById(R.id.childTextView);
		final String name =tempChild.get(childPosition).getName();
		text.setText(name);
		final Context context = convertView.getContext();
		final DatabaseHandler db = new DatabaseHandler(context);
		final boolean selected = db.itemSelected(tempChild.get(childPosition).getId());
		final int itemID = tempChild.get(childPosition).getId();
		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.childSelected);
		checkbox.setChecked(selected);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					db.setSelected(itemID,true);
				}else{
					db.setSelected(itemID, false);
				}
				
			}
		});
		//variables for deletion
		final int categoryId = tempChild.get(childPosition).getCategoryId();
		final int tempChildPosition = childPosition;
		final int tempGroupPosition = groupPosition;
		final List<Item> currentItems = tempChild;
		Button button = (Button) convertView.findViewById(R.id.childDelete);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				//Log.d("ListView","Attempting Delete");
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Delete");
				alertDialogBuilder.setMessage("Are you sure you wish to delete " + name);
				alertDialogBuilder.setCancelable(true);
				alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						db.deleteItem(itemID);
						currentItems.remove(tempChildPosition);
						if(currentItems.size()<1 && categoryId > 6){
							//childItems.remove(tempGroupPosition);
							db.deleteCategory(categoryId);
						}
						childItems.set(tempGroupPosition, currentItems);
						notifyDataSetChanged();
					}
				});
				alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();
					}
				});
				alertDialogBuilder.show();
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
		//Log.d("ListView","Creating Parent");
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
