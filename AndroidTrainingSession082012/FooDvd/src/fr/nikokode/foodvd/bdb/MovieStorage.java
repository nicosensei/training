/**
 * 
 */
package fr.nikokode.foodvd.bdb;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;

/**
 * @author STAGIAIRE
 *
 */
public class MovieStorage extends AbstractBDB {

	/**
	 * Constant, default max memory usage (percentage of JVM memory) to use for the BDB.
	 * TODO this should be configurable
	 */
	private static final int DEFAULT_MEM_CACHE_PERCENTAGE = 20;

	private static final String STORAGE_DIR_BASENAME = MovieStorage.class.getSimpleName();

	public static final String LOGTAG = MovieStorage.class.getSimpleName();
	
	private final Context appContext; 

	private EntityStore movieStore;

	private PrimaryIndex<Integer, MovieEntity> moviesByHashKey;

	private SecondaryIndex<Integer, Integer, MovieEntity> moviesByCategory;

	public MovieStorage(final Context context) {
		this.appContext = context;

		startEnvironment();
		
		moviesByHashKey = movieStore.getPrimaryIndex(Integer.class, MovieEntity.class);
		moviesByCategory = movieStore.getSecondaryIndex(moviesByHashKey, Integer.class, "category");
	}

	@Override
	protected String getStorageFolderPath() {
		return appContext.getDir(STORAGE_DIR_BASENAME, Context.MODE_PRIVATE).getAbsolutePath();
	}

	@Override
	protected int getCachePercentage() {
		return DEFAULT_MEM_CACHE_PERCENTAGE;
	}

	@Override
	protected void closeStores() {
		if (movieStore != null) {
			try {
				movieStore.close();
			} catch (final DatabaseException e) {
				throw new RuntimeException(e); // TODO proper exception handling
			}
			if (Log.isLoggable(LOGTAG, Log.INFO)) {
				Log.i(LOGTAG, "Closed entity store.");
			}
		}		
	}

	@Override
	protected void initStores(Environment dbEnv) {
		try {
			StoreConfig storeCfg = new StoreConfig();
			List<String> dbNames = dbEnv.getDatabaseNames();
			boolean allowCreate = dbNames.isEmpty();
			storeCfg.setAllowCreate(allowCreate);

			movieStore = new EntityStore(dbEnv, MovieStorage.class.getSimpleName(), storeCfg);

			if (Log.isLoggable(LOGTAG, Log.INFO)) {
				Log.i(LOGTAG, "Initialized entity store (allowCreate=" + allowCreate + ").");
			}
		} catch (final DatabaseException e) {
			throw new RuntimeException(e); // TODO proper exception handling
		}

	}

	/**
	 * Closes the storage.
	 * @param destroy if true wipes the BDB storage.
	 * @throws DatabaseException if close failed.
	 */
	public void close(boolean destroy) throws DatabaseException {
		stopEnvironment();
		if (destroy) {
			removeStorageDir();
		}
	}

	public void loadMovieSet(int xmlResourceId) {
		long count = 0;
		long dupCount = 0;
		try {
			XmlPullParser parser= appContext.getResources().getXml(xmlResourceId);
			while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG) {
					if (parser.getName().equals(MovieEntity.XML_TAG_NAME)) {
						MovieEntity entry = new MovieEntity(parser);
						if (moviesByHashKey.contains(entry.getHashKey())) {
							dupCount++;
						} else {
							moviesByHashKey.putNoReturn(entry);
							count++;
						}
					}
				}
				parser.next();
			}
		} catch (Exception e) {
			Log.e(LOGTAG, e.getLocalizedMessage());
		}
		if (Log.isLoggable(LOGTAG, Log.INFO)) {
			Log.i(LOGTAG, "Loaded " + count + " items from file,"
					+ " discarded " + dupCount + " duplicates");
		}
	}

}
