package fr.nikokode.helloandy5;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private int nextAnswerNum = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClickHowdy(View view) {    	
    	Intent askHowdyIntent = new Intent(this, AskAndyActivity.class);
    	askHowdyIntent.putExtra(AskAndyActivity.LAST_ANSWER_NUM, nextAnswerNum);
    	startActivityForResult(askHowdyIntent, AskAndyActivity.RESULT_CODE);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (AskAndyActivity.RESULT_CODE == resultCode) {
			String answer = data.getExtras().getString(AskAndyActivity.ANDY_ANSWER);
			TextView answerTv = (TextView) findViewById(R.id.answerTextView);
			answerTv.setText(answer);
			
			int answerNum = data.getExtras().getInt(AskAndyActivity.LAST_ANSWER_NUM);
			this.nextAnswerNum = answerNum;
		}
	}
}
