/**
 * 
 */
package fr.nikokode.helloandy6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * @author STAGIAIRE
 *
 */
public class LeftFragment extends Fragment {

	/**
	 * 
	 */
	public LeftFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.left_fragment, container, false);
	}

}
