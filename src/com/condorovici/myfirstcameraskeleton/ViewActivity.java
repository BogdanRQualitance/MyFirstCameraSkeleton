package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ViewActivity extends Activity {

	Button btnBack;
	ImageView imageView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
        
        btnBack = (Button)findViewById(R.id.btn_viewBack);
        imageView = (ImageView)findViewById(R.id.ImageView);
        
        btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
    }
}
