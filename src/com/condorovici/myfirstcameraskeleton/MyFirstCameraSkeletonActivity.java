package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
            intentDefaultCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Adaugarea locatiei in care se doreste salvarea imaginii
            intentDefaultCamera.putExtra(MediaStore.EXTRA_OUTPUT, originalFileUri);
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
            Intent intentView = new Intent();
            // Indicarea clasei ce va raspunde intentului
            intentView.setClass(getApplicationContext(), ViewActivity.class);
            // Stocarea adresei pozei de afisat sub denumirea <<imageUri>>
            intentView.putExtra("imageUri", originalFileUri);
            // Pornirea activitatii
            startActivity(intentView);
        }
	};
	
	View.OnClickListener editPicListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
            editPhoto();
        }
	};

    private void editPhoto() {
        // TODO Auto-generated method stub
        Bitmap origBmp;
        Bitmap imageBmpMutable;
        // Decodarea imaginii din fisier
        origBmp = BitmapFactory.decodeFile(originalFileUri.getPath());
        // Crearea unei copii mutable a Bitmap-ului
        imageBmpMutable = origBmp.copy(origBmp.getConfig(), true);
        // Stergerea obiectului initial
        origBmp.recycle();
        // Dimensiunile imaginii
        int width = imageBmpMutable.getWidth();
        int height = imageBmpMutable.getHeight();
        // Vectorul ce va contine valorile pixelilor
        int[] pixArr = new int[width * height];
        // Stream-ul corespunzator fisierului de iesire

        // Obtinerea pixelilor
        imageBmpMutable.getPixels(pixArr, 0, width, 0, 0, width, height);
        int R, G, B;
        int index = 0;
        // parcurgerea imaginii
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                // extragerea planelor de culoare
                int r = (pixArr[index] >> 16) & 0xff;
                int g = (pixArr[index] >> 8) & 0xff;
                int b = pixArr[index] & 0xff;
                R = r;G = 0;B = 0;
                // reimpachetarea valorilor modificate
                pixArr[index] = 0xff000000 | (R<<16) | (G<<8) | B;
                index = index + 1;
            }
        }
        // actualizarea Bitmap-ului cu noile valorile ale pixelilor
        imageBmpMutable.setPixels(pixArr, 0, width, 0, 0, width, height);
        // Salvarea fisierului
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(originalFile);
            imageBmpMutable.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
                Toast alertBox = Toast.makeText(getApplicationContext(), "ImageCaptured", Toast.LENGTH_LONG);
                alertBox.show();
            }
    }

}