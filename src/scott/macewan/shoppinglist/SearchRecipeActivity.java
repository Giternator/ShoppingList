package scott.macewan.shoppinglist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
//import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRecipeActivity extends Activity {
	private TextView searchStatus;
	private ListView recipeList;
	private Context activityContext;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = this;
        setContentView(R.layout.activity_search_recipe);
        
        try {
			Class.forName("android.os.SearchRecipeHandler");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
        searchStatus = (TextView) findViewById(R.id.searchStatus);
        recipeList = (ListView) findViewById(R.id.recipeList);
        Button searchBut = (Button) findViewById(R.id.search);
        searchBut.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				TextView searchTerms = (TextView) findViewById(R.id.searchTerms);
				String searchStr = searchTerms.getText().toString();
				//Resources res = getResources();
				boolean valid = searchStr.matches("^[a-zA-Z\\s]+$");
				if(!valid){
					Toast.makeText(getApplicationContext(), "Please type a valid search", Toast.LENGTH_SHORT).show();
				}else{
					SearchRecipeHandler recipeHandler = new SearchRecipeHandler(activityContext);
					recipeHandler.execute(new String[]{searchStr});
				}				
			}        	
        });
	}
	public class SearchRecipeHandler extends AsyncTask<String, Void, ArrayList<Recipe>>{
		
		
		Context context;
		public SearchRecipeHandler(Context context){
			this.context = context;
		}
		
		
		@Override
		protected ArrayList<Recipe> doInBackground(String... params) {
			
			String paramString = params[0].replace(" ", ",");
			
			String urlHeader = "http://www.recipepuppy.com/api/?i=";
			ArrayList<Recipe> recipes = new ArrayList<Recipe>();
			HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
			HttpGet httpGet = new HttpGet(urlHeader+paramString);		
			httpGet.setHeader("Content-type", "application/json");
			InputStream inputStream = null;
			String result = "";
			
			//atempt the url query and then parse the json response
			try {
				Log.d("Recipe Handler","Before HttpPost");
				HttpResponse response = httpClient.execute(httpGet);
				Log.d("Recipe Handler","Executed HttpPost");
				Log.d("Recipe Handler", "Http Response Code: " + Integer.toString(response.getStatusLine().getStatusCode()));
				if(response.getStatusLine().getStatusCode() == 200){
					HttpEntity entity = response.getEntity();
					inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null)
					{
					    sb.append(line + "\n");
					}
					result = sb.toString();
					JSONObject jobject = new JSONObject(result);
					JSONArray jarray = jobject.getJSONArray("results");
					for(int i = 0; i<jarray.length(); i++){
						JSONObject recipeObject = jarray.getJSONObject(i);
						String ingredients = recipeObject.getString("ingredients");
						StringTokenizer st = new StringTokenizer(ingredients, ",");
						List<Item> items = new ArrayList<Item>();
						while(st.hasMoreTokens()){
							String name = st.nextToken();
							name = name.trim();
							Item item = new Item(name);
							items.add(item);
						}
						Log.d("Recipe Handler","Number of ingredients = " + items.size());
						Recipe recipe = new Recipe(recipeObject.getString("title").trim(), items);
						recipes.add(recipe);
					}
				}else{
					recipes = null;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				recipes = null;
				Log.d("Recipe Handler","Failed to execute httpPost--ClientProtocol Exception");
			}catch(IOException e){
				e.printStackTrace();
				recipes = null;
				Log.d("Recipe Handler","Failed to execute httpPost--IO Exception");
			}catch(JSONException e){
				e.printStackTrace();
				recipes = null;
				Log.d("Recipe Handler","Failed to convert return to JSON--JSON Exception");
			}
			Log.d("Recipe Handler","Finished doInBackground");
			return recipes;
		}
		
		 protected void onPostExecute(ArrayList<Recipe> recipes){
			//super.onPostExecute(recipes);
			Log.d("Recipe Handler","Started OnPostExecute");
			if(recipes == null){
				//TextView status = (TextView) findViewById(R.id.searchStatus);
				searchStatus.setText("Error Occured -- Try again in a few moments...");
				Log.d("Recipe Handler","Fell in recipes = null");
			}else if(recipes.size() == 0){
				searchStatus.setText("No recipes found. Try another search...");
				Log.d("Recipe Handler","Fell in recipes = 0");
			}else{
				Log.d("Recipe Handler","Fell good one");
				searchStatus.setText("Sucess!!!");				
				RecipeAdapter adapter = new RecipeAdapter(context, R.layout.activity_search_recipe, recipes);
				recipeList.setAdapter(adapter);
				
			}			
		}
	}
}
