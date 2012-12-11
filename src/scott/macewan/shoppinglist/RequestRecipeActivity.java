package scott.macewan.shoppinglist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class RequestRecipeActivity extends Activity {
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		Bundle extras = getIntent().getExtras();
		if(extras == null){
			return;
		}
		List<Recipe> recipes = new ArrayList<Recipe>();
		String searchStr = extras.getString("searchStr");
		if(searchStr != null){
			SearchRecipeHandler recipeHandler = new SearchRecipeHandler();
			recipeHandler.execute(new String[]{searchStr});
		}
		finish(recipes);
	}
	
	public void finish(List<Recipe> recipes){
		Intent data = new Intent();
		int i = recipes.size();
		data.putExtra("Value",i);
		setResult(0, data);
		super.finish();
	}
}
