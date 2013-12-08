package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        // Bitmap-ul ce va contine imaginea de afisat
        Bitmap displayPic;
        // Intentul ce a fost folosit pentru deschiderea activitatii
        Intent callingIntent = new Intent();
        // URI-ul corespunator fisierului de afisat
        Uri imageFileUri;

        // Obtinerea Intent-ului de afisat
        callingIntent = getIntent();
        // Extragerea datelor de tip Extras
        Bundle extras = callingIntent.getExtras();
        // Extragerea adresei fisierului de afisat
        imageFileUri = (Uri)extras.get("imageUri");


        // Decodarea fisierului intr-un obiect de tipul Bitmap
        displayPic = BitmapFactory.decodeFile(imageFileUri.getPath());
        // Afisarea Bitmap-ului
        imageView.setImageBitmap(displayPic);


        btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                finish();
			}
		});
    }
}
