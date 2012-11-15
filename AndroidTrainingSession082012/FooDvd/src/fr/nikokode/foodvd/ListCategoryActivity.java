package fr.nikokode.foodvd;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.nikokode.foodvd.bdb.MovieEntity.Category;

public class ListCategoryActivity extends Activity {
	
	public enum IntentParam {
		CATEGORY;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        
        Category category = 
        		Category.values()[getIntent().getExtras().getInt(IntentParam.CATEGORY.name())];
        
        SimpleAdapter adapter = MovieStorageService.getInstance(getApplicationContext())
        		.getStorage().getListViewAdapter(category, R.layout.movie_details_item);
        ((ListView) findViewById(R.id.categoryListView)).setAdapter(adapter);
    }
    
}
