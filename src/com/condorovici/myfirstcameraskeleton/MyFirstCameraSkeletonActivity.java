package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
            intentDefaultCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            // Adaugarea locatiei in care se doreste salvarea imaginii
            intentDefaultCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, originalFileUri);
            // Lansarea aplicatiei ce raspunde la intent
            startActivityForResult(intentDefaultCamera, CAMERA_REQUEST);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        // se verifica daca metoda a fost apelata datorita intoarcerii unui rezultat
        // in urma intentului de captura a pozei
        if(requestCode == CAMERA_REQUEST)
            // se verifica daca rezultatul s-a obtinut cu succes
            if(resultCode == RESULT_OK)
            {
                Toast alertBox;
                alertBox = Toast.makeText(getApplicationContext(), "ImageCaptured", 1);
                alertBox.show();
            }
    }

}