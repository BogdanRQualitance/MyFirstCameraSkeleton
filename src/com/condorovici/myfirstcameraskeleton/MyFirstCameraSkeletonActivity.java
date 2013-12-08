package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MyFirstCameraSkeletonActivity extends Activity {
	
	Button takePicDefault;
	Button takePicCustom;
	Button viewPic;
	Button editPic;
//   Intentul de pornire a aplicatiei implicite de captura
    Intent intentDefaultCamera;

    //Locul in care se va salva imaginea: nume f
    String originalPicPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tempOriginal.jpg";
    File originalFile = new File(originalPicPath);
    Uri originalFileUri = Uri.fromFile(originalFile);

    //Tipul rezultatelor
    final static int CAMERA_REQUEST = 0;


    View.OnClickListener takePicDefaultListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	View.OnClickListener takePicCustomListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	View.OnClickListener viewPicListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	View.OnClickListener editPicListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        takePicDefault = (Button)findViewById(R.id.btn_takePicDefault);
        takePicCustom = (Button)findViewById(R.id.btn_takePicCustom);
        viewPic = (Button)findViewById(R.id.btn_viewPic);
        editPic = (Button)findViewById(R.id.btn_editPic);
                
        takePicDefault.setOnClickListener(takePicDefaultListener);
        takePicCustom.setOnClickListener(takePicCustomListener);
        viewPic.setOnClickListener(viewPicListener);
        editPic.setOnClickListener(editPicListener);
    }
}