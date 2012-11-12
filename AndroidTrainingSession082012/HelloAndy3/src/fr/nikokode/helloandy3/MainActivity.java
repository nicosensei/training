package fr.nikokode.helloandy3;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	
	private enum ITEM {
		
		CALL("Call", "Make a phone call", R.drawable.phone),
		MAIL("Email", "Send an email", R.drawable.email),
		LOCATE("Locate", "Find a location", R.drawable.location);		
		
		private String title;
		private String desc;
		private int drawableId;
		private ITEM(String title, String desc, int drawableId) {
			this.title = title;
			this.desc = desc;
			this.drawableId = drawableId;
		}
		
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Populate listview
        ListView list = (ListView) findViewById(R.id.listviewperso);
        
        ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String,String>>();
        for (ITEM i : ITEM.values()) {      
        	HashMap<String, String> itemMap = new HashMap<String, String>();
        	itemMap.put("title", i.title);
        	itemMap.put("description", i.desc);
        	itemMap.put("img", String.valueOf(i.drawableId));
        	items.add(itemMap);
        }
        
        SimpleAdapter fillList = new SimpleAdapter(
        		getBaseContext(),
        		items,
        		R.layout.layout_listview_item,
        		new String[] {"img", "title", "description"}, 
        		new int[] {R.id.img, R.id.title, R.id.description});
        list.setAdapter(fillList);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
