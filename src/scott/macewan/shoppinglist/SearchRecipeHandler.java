package scott.macewan.shoppinglist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class SearchRecipeHandler extends AsyncTask<String, Void, List<Recipe>>{
	
	private String searchTerms;
	private String urlHeader;
	private String app_id;
	private String app_key;
	
	public SearchRecipeHandler(){
		app_id = "4900e3fc";
		app_key = "2cbc7ec5d2215bae0f0407c16ea21a27";
		//urlHeader = "http://api.yummly.com/v1";
		urlHeader = "http://www.recipepuppy.com/api";
	}
	
	/*
	private String prepareSearch(String searchTerms){
		StringTokenizer st = new StringTokenizer(searchTerms);
		List<String> terms = new ArrayList<String>();
		while(st.hasMoreTokens()){
			terms.add(st.nextToken());
		}
		String urlHeader = "http://api.yummly.com/v1";
		String urlKey = "_app_id=4900e3fc&_app_key=2cbc7ec5d2215bae0f0407c16ea21a27";
		StringBuffer buff = new StringBuffer();
		boolean first = true;
		for(String term: terms){
			if(first){
				first = false;
			}else{
				buff.append("+");
			}
			buff.append(term);
		}
		String tempTerms = "q="+buff.toString() + urlKey;
		String url = "";
		try {
			url = urlHeader+URLEncoder.encode(tempTerms, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.d("Recipe Handler", "Failed to encode query string");
		}
		return url;
	}
	
	public String getSearchTerms(){
		return searchTerms;
	}
	
	public List<Recipe> searchYummly(){
		List<Recipe> recipes = new ArrayList<Recipe>();
		HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
		HttpGet httpGet = new HttpGet(urlHeader);		
		httpGet.setHeader("Content-type", "application/json");
		httpGet.setHeader("X-Yummly-App-ID",app_id);
		httpGet.setHeader("X-Yummly-App-Key",app_key);
		//httpGet.setParams
		InputStream inputStream = null;
		String result = null;
		try {
			Log.d("Recipe Handler","Before HttpPost");
			HttpResponse response = httpClient.execute(httpGet);
			Log.d("Recipe Handler","Executed HttpPost");
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
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d("Recipe Handler","Failed to execute httpPost--ClientProtocol Exception");
		}catch(IOException e){
			e.printStackTrace();
			Log.d("Recipe Handler","Failed to execute httpPost--IO Exception");
		}
		return recipes;
	}
*/
	@Override
	protected List<Recipe> doInBackground(String... params) {
		List<Recipe> recipes = new ArrayList<Recipe>();
		HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
		HttpGet httpGet = new HttpGet(urlHeader);		
		httpGet.setHeader("Content-type", "application/json");
		//httpGet.setHeader("X-Yummly-App-ID",app_id);
		//httpGet.setHeader("X-Yummly-App-Key",app_key);
		//httpGet.setParams
		InputStream inputStream = null;
		String result = "";
		try {
			Log.d("Recipe Handler","Before HttpPost");
			HttpResponse response = httpClient.execute(httpGet);
			Log.d("Recipe Handler","Executed HttpPost");
			Log.d("Recipe Handler", "Http Response Code: " + Integer.toString(response.getStatusLine().getStatusCode()));
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
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d("Recipe Handler","Failed to execute httpPost--ClientProtocol Exception");
		}catch(IOException e){
			e.printStackTrace();
			Log.d("Recipe Handler","Failed to execute httpPost--IO Exception");
		}catch(JSONException e){
			e.printStackTrace();
			Log.d("Recipe Handler","Failed to convert return to JSON--JSON Exception");
		}
		return recipes;
	}
	
	protected void onPostExecute(String result){
		
	}
}
