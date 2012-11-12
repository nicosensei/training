package fr.nikokode.helloandy2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lefty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClickSwitchView(View view)  {
    	int viewId = view.getId();
    	switch (viewId) {
    		case R.id.buttonLefty:
    			setContentView(R.layout.layout_lefty);
    			break;
    		case R.id.buttonRighty:
    			setContentView(R.layout.layout_righty);
    			break;
    		default:
    			Log.e("HelloAndy2", "Button ID is unknown!");
    	}
    }
    
}
