/**
 * 
 */
package fr.nikokode.foodvd.bdb;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * @author STAGIAIRE
 *
 */
@Entity
public class MovieEntity {
	
	@PrimaryKey
	private long uid;
	
	@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private String category;
	
	private String title;
	
	private String filmMaker;
	
	private String imgRelPath;
	
	/**
	 * Default constructor needed by BDB.
	 */
	public MovieEntity() {
		
	}

	public MovieEntity(long uid, String category, String title,
			String filmMaker, String imgRelPath) {
		super();
		this.uid = uid;
		this.category = category;
		this.title = title;
		this.filmMaker = filmMaker;
		this.imgRelPath = imgRelPath;
	}

	/**
	 * @return the uid
	 */
	public long getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(long uid) {
		this.uid = uid;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the filmMaker
	 */
	public String getFilmMaker() {
		return filmMaker;
	}

	/**
	 * @param filmMaker the filmMaker to set
	 */
	public void setFilmMaker(String filmMaker) {
		this.filmMaker = filmMaker;
	}

	/**
	 * @return the imgRelPath
	 */
	public String getImgRelPath() {
		return imgRelPath;
	}

	/**
	 * @param imgRelPath the imgRelPath to set
	 */
	public void setImgRelPath(String imgRelPath) {
		this.imgRelPath = imgRelPath;
	}
	
}
