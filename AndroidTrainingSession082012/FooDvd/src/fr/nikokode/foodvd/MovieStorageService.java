/**
 * 
 */
package fr.nikokode.foodvd;

import android.content.Context;
import fr.nikokode.foodvd.bdb.MovieStorage;

/**
 * Singleton that wraps the storage. This could be externalized as a service.
 *
 */
public class MovieStorageService {
	
	private static MovieStorageService instance;
	
	private MovieStorage storage;
	
	public static MovieStorageService getInstance(Context context) {
		if (instance == null) {
			instance = new MovieStorageService(context);
		}
		return instance;
	}
	
	private MovieStorageService(Context context) {
		storage = new MovieStorage(context);
	}
	
	public MovieStorage getStorage() {
		return storage;
	}
	
	public static void close() {
		if (instance != null) {
			instance.storage.close(false);
		}
	}

}
