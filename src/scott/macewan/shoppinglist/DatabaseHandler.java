package scott.macewan.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "itemsManager";
    
 
    // Items table name
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_CATEGORIES = "categoriesManager";
    
    // Items/Categories Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SELECTED = "selected";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
									+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CATEGORY_ID +" INTEGER, "
									+ KEY_NAME + " TEXT, " + KEY_SELECTED + " INTEGER)";
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES+ "(" + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
											+ KEY_NAME + " TEXT)";		
		db.execSQL(CREATE_ITEMS_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		
		String[] categories = new String[6];
		categories[0] = "Bread";
		categories[1] = "Dairy";
		categories[2] = "Fruit";
		categories[3] = "Protein";
		categories[4] = "Vegetables";
		categories[5] = "Other";
		for(String category : categories){
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, category);
			db.insert(TABLE_CATEGORIES, null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITEMS);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CATEGORIES);
		onCreate(db);
	}
	
	//add an item to the items table
	public void addItem(Item item){
		SQLiteDatabase db = this.getWritableDatabase();		
		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORY_ID, item.getCategoryId());
		values.put(KEY_NAME, item.getName());
		values.put(KEY_SELECTED, 0);
		db.insert(TABLE_ITEMS, null, values);
		db.close();
	}
	
	public void addCategory(String name){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		db.insert(TABLE_CATEGORIES, null, values);
		db.close();
	}
	
	public long addRecipe(String name){		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		long id= db.insert(TABLE_CATEGORIES, null, values);
		return id;
	}
	
	public List<Category> getCategorys(){
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<Category> categorys = new ArrayList<Category>();
		if(cursor.moveToFirst()){
			do{
				Category category = new Category();
				category.setId(Integer.parseInt(cursor.getString(0)));
				category.setName(cursor.getString(1));
				categorys.add(category);
			}while(cursor.moveToNext());
		}
		db.close();
		return categorys;
	}
	
	//get all items of a certain category
	public List<Item> getCategoryItems(int category_id){
		List<Item> itemList = new ArrayList<Item>();
		String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + KEY_CATEGORY_ID + " = " + category_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		//Log.d("Reading", Integer.toString(cursor.getCount()));
		if(cursor.moveToFirst()){
			do{
				Item item = new Item();
				item.setId(Integer.parseInt(cursor.getString(0)));
				item.setCategoryId(Integer.parseInt(cursor.getString(1)));
				item.setName(cursor.getString(2));
				itemList.add(item);
			} while(cursor.moveToNext());
		}
		db.close();
		return itemList;
	}
	
	//return the number of items in a specific category
	public int getCategoryCount(int category_id){
		String countQuery = "SELECT * FROM " + TABLE_ITEMS + "WHERE " + KEY_CATEGORY_ID + " = " + category_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		return cursor.getCount();
	}
	
	
	//delete an item from the items database
	public void deleteItem(int itemID){
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d("Database", "Deleteing "+Integer.toString(itemID));
		db.delete(TABLE_ITEMS, KEY_ID + " = ?", new String[] {String.valueOf(itemID)});
		db.close();
	}
	
	//delete a category from the category database
	public void deleteCategory(int categoryId){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CATEGORIES, KEY_ID + " = ?", new String[] {String.valueOf(categoryId)});
		db.close();
	}
	
	//get the status of an item on the list
	public boolean itemSelected(int itemID){
		boolean selected = false;
		String selectQuery="SELECT " + KEY_SELECTED + " FROM " + TABLE_ITEMS + " WHERE " + KEY_ID + " = " + itemID;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		//Log.d("Cursor", Integer.toString(cursor.getCount()));
		if(cursor.moveToFirst()){
			int selectedInt = Integer.parseInt(cursor.getString(0));
			if(selectedInt != 0){
				selected = true;
			}
		}
		//Log.d("Database", itemID + "  =  "+ Boolean.toString(selected));
		db.close();
		return selected;
	}
	
	//set the selected status of the item on the list
	public void setSelected(int itemID, boolean selected){
		Log.d("Database", "Updating " + itemID + "  =  "+ Boolean.toString(selected));
		int selectedInt;
		if(selected){
			selectedInt = 1;
		}else{
			selectedInt = 0;
		}
		SQLiteDatabase db = this.getReadableDatabase();
		String filter = KEY_ID+"="+itemID;
		ContentValues values = new ContentValues();
		values.put(KEY_SELECTED, selectedInt);
		db.update(TABLE_ITEMS, values, filter, null);
		db.close();
	}
	

}
