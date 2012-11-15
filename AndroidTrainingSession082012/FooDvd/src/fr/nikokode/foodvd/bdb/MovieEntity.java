/**
 * 
 */
package fr.nikokode.foodvd.bdb;

import org.xmlpull.v1.XmlPullParser;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import fr.nikokode.foodvd.R;

/**
 * @author STAGIAIRE
 *
 */
@Entity
public class MovieEntity {
	
	@PrimaryKey
	private int hashKey;
	
	@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private int category;
	
	private String title;
	
	private String filmMaker;
	
	private String imgRelPath;
	
	public final static String XML_TAG_NAME = "movie";
	
	public static enum XML_ATTR {
		CATEGORY(0, "category"),
		TITLE(1, "title"),
		FILMMAKER(2, "filmMaker"),
		IMG(3, "img");
		
		private final int rank;
		private final String name;

		private XML_ATTR(int rank, String name) {
			this.rank = rank;
			this.name = name;
		}

		/**
		 * @return the rank
		 */
		public int getRank() {
			return rank;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}
	
	public static enum Category {
		CRIME,
		DOCUMENTARY,
		FICTION,
		SERIAL;
		
		public static Category fromButtonId(int id)  {
			switch(id) {
				case R.id.button_category_crime: return CRIME;
				case R.id.button_category_documentary: return DOCUMENTARY;
				case R.id.button_category_fiction: return FICTION;
				case R.id.button_category_serial: return SERIAL;
				default:
					throw new RuntimeException("Unknown button id!");
			}
		}
	}
	
	/**
	 * Default constructor needed by BDB.
	 */
	public MovieEntity() {
		
	}
	
	public MovieEntity(final XmlPullParser xmlParser) {
		this(Category.values()[Integer.parseInt(xmlParser.getAttributeValue(
					XML_ATTR.CATEGORY.rank))],
			xmlParser.getAttributeValue(XML_ATTR.TITLE.rank),
			xmlParser.getAttributeValue(XML_ATTR.FILMMAKER.rank),
			xmlParser.getAttributeValue(XML_ATTR.IMG.rank));
	}

	public MovieEntity(Category category, String title,
			String filmMaker, String imgRelPath) {
		super();
		this.category = category.ordinal();
		this.title = title;
		this.filmMaker = filmMaker;
		this.imgRelPath = imgRelPath;
		this.hashKey = hashCode();
	}

	/**
	 * @return the hashKey
	 */
	public int getHashKey() {
		return hashKey;
	}

	/**
	 * @param hashKey the hashKey to set
	 */
	public void setHashKey(int hashKey) {
		this.hashKey = hashKey;
	}

	/**
	 * @return the category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(int category) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category;
		result = prime * result
				+ ((filmMaker == null) ? 0 : filmMaker.hashCode());
		result = prime * result
				+ ((imgRelPath == null) ? 0 : imgRelPath.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	
}
