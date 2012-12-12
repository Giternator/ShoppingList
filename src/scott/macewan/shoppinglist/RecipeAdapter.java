package scott.macewan.shoppinglist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
	
	private ArrayList<Recipe> recipes;
	Context context;
	
	public RecipeAdapter(Context context, int textViewResourceId, ArrayList<Recipe> recipes){
		super(context, textViewResourceId, recipes);
		this.context = context;
		this.recipes = recipes;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		if(v == null){
			LayoutInflater li = ((Activity) context).getLayoutInflater();
			v = li.inflate(R.layout.recipe_list_view, null);
		}
		final Recipe recipe = recipes.get(position);
		TextView title = (TextView) v.findViewById(R.id.recipeTitle);
		TextView ingredients = (TextView) v.findViewById(R.id.recipeIngredients);
		title.setText(recipe.getName());
		ingredients.setText(recipe.ingredientString());
		Button addBut = (Button) v.findViewById(R.id.recipeAdd);
		final DatabaseHandler db = new DatabaseHandler(context);
		addBut.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				int id = (int) db.addRecipe(recipe.getName());
				for(Item ingredient: recipe.getIngredients()){
					ingredient.setCategoryId(id);
					db.addItem(ingredient);
				}
				Intent intent = new Intent(context, ListActivity.class);
				context.startActivity(intent);
			}
		});
		return v;
	}
	

}
