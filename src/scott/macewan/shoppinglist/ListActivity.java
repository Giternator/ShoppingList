package scott.macewan.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ListActivity extends ExpandableListActivity  implements OnGroupExpandListener{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        
        //create the onClickListener to send the user to the create a new item screen
        Button addItemButton = (Button) findViewById(R.id.add_items);
        addItemButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent newScreen = new Intent(getApplicationContext(), AddItemActivity.class);
				startActivity(newScreen);				
			}
		});
	}
	
	public void onResume(){
		super.onResume();
		DatabaseHandler db = new DatabaseHandler(this);
		
		List<Category> categoryNames = db.getCategorys();
		//Log.d("Items", Integer.toString(categoryNames.size()));
		ExpandableListView shoppingList = (ExpandableListView) findViewById(android.R.id.list);
		shoppingList.setClickable(true);
		ArrayList<List<Item>> childItems = new ArrayList<List<Item>>();
		for(Category category: categoryNames){
			List<Item> items = db.getCategoryItems(category.getId());
			Log.d("Child Items",Integer.toString(items.size()));
			childItems.add(items);
		}
		CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(categoryNames, childItems);
		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),	this);
		getExpandableListView().setAdapter(adapter);
		
	}
}
