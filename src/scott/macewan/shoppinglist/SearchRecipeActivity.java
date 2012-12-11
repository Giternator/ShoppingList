package scott.macewan.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRecipeActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        
        Button searchBut = (Button) findViewById(R.id.search);
        searchBut.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				TextView searchTerms = (TextView) findViewById(R.id.searchTerms);
				String searchStr = searchTerms.getText().toString();
				Resources res = getResources();
				if(searchStr == res.getString(R.string.search_recipe_hint)){
					Toast.makeText(getApplicationContext(), "Please type a search", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(getApplicationContext(), RequestRecipeActivity.class);
					intent.putExtra("searchStr", searchStr);
					startActivityForResult(intent,0);					
				}				
			}        	
        });
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		Log.d("Search Recipe","onActivityResultFired");
	}
}
