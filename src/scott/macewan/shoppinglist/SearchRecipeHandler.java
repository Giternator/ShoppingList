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

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SearchRecipeHandler extends AsyncTask<String, Void, List<Recipe>>{
	
	@Override
	protected List<Recipe> doInBackground(String... params) {
		
		//Create an new array for storing the recipies from the site
		String urlHeader = "http://www.recipepuppy.com/api";
		List<Recipe> recipes = new ArrayList<Recipe>();
		HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
		HttpGet httpGet = new HttpGet(urlHeader);		
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
					StringTokenizer st = new StringTokenizer(ingredients);
					List<Item> items = new ArrayList<Item>();
					while(st.hasMoreTokens()){
						String name = st.nextToken();
						name = name.substring(0,name.length()-1);
						Item item = new Item(name);
						items.add(item);
					}
					Log.d("Recipe Handler","Number of ingredients = " + items.size());
					Recipe recipe = new Recipe(recipeObject.getString("title"), items);
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
		return recipes;
	}
	
	protected void onPostExecute(List<Recipe> recipes){
		if(recipes == null){
			//TextView status = (TextView) findViewById(R.id.searchStatus);
		}else if(recipes.size() == 0){
			
		}else{
			
		}
		
	}
}
