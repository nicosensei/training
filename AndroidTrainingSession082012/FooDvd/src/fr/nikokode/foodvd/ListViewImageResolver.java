/**
 * 
 */
package fr.nikokode.foodvd;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

/**
 * @author STAGIAIRE
 *
 */
public class ListViewImageResolver implements ViewBinder {
	
	public boolean setViewValue(View view, Object data, String textRepresentation) {
		if ((view instanceof ImageView) & (data instanceof Bitmap) ) {
			ImageView iv = (ImageView) view;
			Bitmap bm = (Bitmap) data;	
			iv.setImageBitmap(bm);	
			return true;
		}
 
		return false;
	}
	
}