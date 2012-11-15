/**
 * 
 */
package fr.nikokode.foodvd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author STAGIAIRE
 *
 */
class MovieDatabase extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	
	private static final String LOGTAG = MovieDatabase.class.getSimpleName();

	/**
	 * @param context
	 */
	MovieDatabase(Context context) {
		super(
			context, 
			MovieDatabase.class.getSimpleName(), 
			null, 
			VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		if (isCreated(db)) {
			Log.i(LOGTAG, "Database already created.");
			return;
		}
		db.execSQL("CREATE TABLE movie ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "category TEXT, "
				+ "title TEXT, "
				+ "filmmaker TEXT, "
				+ "img TEXT)");
		Log.i(LOGTAG, "Database successfully created.");
	}
	
	public boolean isCreated(SQLiteDatabase db) {
		Cursor result = null;
		try {
			result = db.rawQuery("SELECT * FROM movie", null);
		} catch (SQLiteException e) {
			Log.d(LOGTAG, e.getClass().getName() + " - " + e.getLocalizedMessage());
			return false;
		} finally {
			if (result != null) {
				result.close();
			}
		}
		return true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
