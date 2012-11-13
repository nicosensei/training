/**
 * 
 */
package fr.nikokode.helloandy5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author STAGIAIRE
 *
 */
public class AskAndyActivity extends Activity {
	
	public static final int RESULT_CODE = 50;
	
	public static final String ANDY_ANSWER = AskAndyActivity.class.getSimpleName();
	public static final String ANDY_ANSWER_STRING = "Doin' fine dude!";

	/**
	 * 
	 */
	public AskAndyActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = new Intent();
        i.putExtra(ANDY_ANSWER, ANDY_ANSWER_STRING);
        setResult(RESULT_CODE, i);
        finish();
	}
	
}
