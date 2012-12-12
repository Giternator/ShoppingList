package scott.macewan.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddItemActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		
		//call the original method
        super.onCreate(savedInstanceState);
        
        //set the content view via the xml I wish
        setContentView(R.layout.activity_add_item);
        
        //grab the button that I'll will add a listener too
        Button addItemButton = (Button) findViewById(R.id.create_item);
        
        //create finals of the objects used in the onClickListener
        final DatabaseHandler db = new DatabaseHandler(this);
        final EditText itemName = (EditText) findViewById(R.id.item_name);
        final Spinner categoryName = (Spinner) findViewById(R.id.category);
        
        //set and create the onClickListener
        addItemButton.setOnClickListener(new View.OnClickListener(){			
			public void onClick(View v) {
				//grab the name and category of the item being created
				String name = itemName.getText().toString();
				String categoryNameStr = categoryName.getSelectedItem().toString();
				
				//convert the category name to the proper category id
				int categoryId = categoryId(categoryNameStr);
				
				if(name.length() > 0){
					//make a new item with the provided information 
					Item item = new Item(categoryId, name);
					
					// add the item to the database
					db.addItem(item);
					
					//create a toast message saying the item was added
					Context context = getApplicationContext();
					CharSequence text = name + " added to "+ categoryNameStr;
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					
					//switch back to the list of items
					Intent newScreen = new Intent(getApplicationContext(), ListActivity.class);
					startActivity(newScreen);
				}else{
					Toast.makeText(getApplicationContext(), "Please provide an item name", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        Button searchRecipeBut = (Button) findViewById(R.id.search_recipe);
        searchRecipeBut.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		Intent newScreen = new Intent(getApplicationContext(), SearchRecipeActivity.class);
        		startActivity(newScreen);
        	}
        });
	}
	
	private int categoryId(String category){
		int id;
		if(category.equals("Bread")){
			id = 1;
		}else if(category.equals("Dairy")){
			id = 2;
		}else if(category.equals("Fruit")){
			id = 3;
		}else if(category.equals("Protein")){
			id = 4;
		}else if(category.equals("Vegetable")){
			id = 5;
		}else{
			id = 6;
		}
		return id;
	}

}
