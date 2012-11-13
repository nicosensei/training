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
	
	public static final String ANDY_ANSWER = AskAndyActivity.class.getSimpleName() + "_1";
	public static final String LAST_ANSWER_NUM = AskAndyActivity.class.getSimpleName() + "_2";
	
	private static enum ANSWERS {
		
		DOIN_FINE("Doin' fine dude!"),
		STILL_DOIN_FINE("Still doin' fine."),
		OK_IM_FINE("Ok I'm fine..."),
		I_TOLD_YOU("Already told you about that dude!"),
		STOP_IT("Come on dude, stop it!"),
		MUTE("...");
		
		private final String text;

		private ANSWERS(String text) {
			this.text = text;
		}
		
	}
	
	/**
	 * 
	 */
	public AskAndyActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent sent = getIntent();
        int lastAnswerNum = sent.getExtras().getInt(LAST_ANSWER_NUM);
        
        Intent i = new Intent();
        i.putExtra(ANDY_ANSWER, ANSWERS.values()[lastAnswerNum].text);
        i.putExtra(LAST_ANSWER_NUM, (lastAnswerNum + 1) % ANSWERS.values().length);
        setResult(RESULT_CODE, i);
        finish();
        
        
	}
	
}
