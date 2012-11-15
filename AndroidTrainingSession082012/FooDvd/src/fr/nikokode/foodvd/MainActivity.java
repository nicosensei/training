package fr.nikokode.foodvd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private MovieDatabase mdb;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Handle database initialization
        mdb = new MovieDatabase(getBaseContext());
        SQLiteDatabase db = mdb.getWritableDatabase();
        db.setLockingEnabled(false);
        mdb.onCreate(db);
        
        // Set view
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		int itemCode = -1;
		switch (item.getItemId()) {
			case R.id.menu_search:
				itemCode = R.string.menu_search;
				break;
			case R.id.menu_book:
				itemCode = R.string.menu_book;
				break;
			case R.id.menu_shops:
				itemCode = R.string.menu_shops;
				break;
			case R.id.menu_about:
				// Sub-menu root, don't show any dialog
				return true;
			case R.id.menu_about_contact:
				itemCode = R.string.menu_about_contact;
				break;
			case R.id.menu_about_growth:
				itemCode = R.string.menu_about_growth;
				break;
			case R.id.menu_about_team:
				itemCode = R.string.menu_about_team;
				break;
			default:
				itemCode = -1;
				break;
		}		
		builder.setMessage("\"" + itemCode + "\" not yet implemented!");
		builder.setPositiveButton(
				"Ok", 
				new OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						Log.d(MainActivity.class.getSimpleName(), "Dialog closed");						
					}				
				});
		builder.create().show();
		
		return true;
	}

	@Override
	protected void onDestroy() {
		mdb.close();
		super.onDestroy();
	}	
	
	
}
