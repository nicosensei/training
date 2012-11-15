/**
 * 
 */
package fr.nikokode.foodvd;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.nikokode.foodvd.bdb.MovieEntity;

/**
 * @author STAGIAIRE
 *
 */
public class MovieItemAdapter extends ArrayAdapter<MovieEntity> {

	private MovieItemAdapter(
			Context context, 
			int textViewResourceId,
			List<MovieEntity> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MovieEntity item = getItem(position);
		((TextView) convertView.findViewById(R.id.movieItemTitle)).setText(item.getTitle());
		((TextView) convertView.findViewById(R.id.movieItemFilmMaker)).setText(item.getFilmMaker());
		
	    InputStream is = getClass().getClassLoader().getResourceAsStream(item.getImgRelPath());
	    Bitmap bm = BitmapFactory.decodeStream(is);  	    
	    ((ImageView) convertView.findViewById(R.id.movieItemImg)).setImageBitmap(bm);  
	    
	    return parent;
	}

}
