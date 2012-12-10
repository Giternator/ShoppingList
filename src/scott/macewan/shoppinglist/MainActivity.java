package scott.macewan.shoppinglist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button beginButton = (Button) findViewById(R.id.begin);
        
        //create an onclicklistener to switch screens to the next activity
        beginButton.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent newScreen = new Intent(getApplicationContext(), ListActivity.class);
				startActivity(newScreen);				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
