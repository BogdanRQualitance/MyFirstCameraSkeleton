package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
    String originalPicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempOriginal.jpg";
    String editPicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempOriginal2.jpg";
    File originalFile = new File(originalPicPath);
    File editFile = new File(editPicPath);
    Uri originalFileUri = Uri.fromFile(originalFile);
    Uri editFileUri = Uri.fromFile(editFile);

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
            showPicture();
        }
    };

    private void showPicture() {
        Intent intentView = new Intent();
        // Indicarea clasei ce va raspunde intentului
        intentView.setClass(getApplicationContext(), ViewActivity.class);
        // Stocarea adresei pozei de afisat sub denumirea <<imageUri>>
        intentView.putExtra("imageUri", editFileUri);
        // Pornirea activitatii
        startActivity(intentView);
    }

    View.OnClickListener editPicListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            editPhoto();
            showPicture();
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
        imageBmpMutable = histogram_matching_rgb(imageBmpMutable, 1);
        // parcurgerea imaginii
        // actualizarea Bitmap-ului cu noile valorile ale pixelilor
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(editPicPath);
            imageBmpMutable.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Toast alertBox = Toast.makeText(getApplicationContext(), "Edit photo finish.", Toast.LENGTH_LONG);
        alertBox.show();
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        takePicDefault = (Button) findViewById(R.id.btn_takePicDefault);
        takePicCustom = (Button) findViewById(R.id.btn_takePicCustom);
        viewPic = (Button) findViewById(R.id.btn_viewPic);
        editPic = (Button) findViewById(R.id.btn_editPic);

        takePicDefault.setOnClickListener(takePicDefaultListener);
        takePicCustom.setOnClickListener(takePicCustomListener);
        viewPic.setOnClickListener(viewPicListener);
        editPic.setOnClickListener(editPicListener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // se verifica daca metoda a fost apelata datorita intoarcerii unui rezultat
        // in urma intentului de captura a pozei
        if (requestCode == CAMERA_REQUEST)
            // se verifica daca rezultatul s-a obtinut cu succes
            if (resultCode == RESULT_OK) {
                Toast alertBox = Toast.makeText(getApplicationContext(), "ImageCaptured", Toast.LENGTH_LONG);
                alertBox.show();
            }
    }

    private Bitmap histogram_matching_rgb(Bitmap imageToEdit, int opt) {
        int imageWidth = imageToEdit.getWidth();
        int imageHeight = imageToEdit.getHeight();

        Bitmap image = Bitmap.createScaledBitmap(imageToEdit, imageWidth, imageHeight, false);


        final int LEVEL = 256;
        int[] lred = new int[LEVEL];
        int[] lgreen = new int[LEVEL];
        int[] lblue = new int[LEVEL];
        double[] pr = new double[LEVEL];
        double[] pg = new double[LEVEL];
        double[] pb = new double[LEVEL];

        double[] sr = new double[LEVEL];
        double[] sg = new double[LEVEL];
        double[] sb = new double[LEVEL];

        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
//                int alpha = 0xff &(image.getPixel(w, h)>>24);
                int red = 0xff & (imageToEdit.getPixel(w, h) >> 16);
                int green = 0xff & (imageToEdit.getPixel(w, h) >> 8);
                int blue = 0xff & imageToEdit.getPixel(w, h);
                //we gonna go to count the number of each gray level
                //mounting a histogram to red
                lred[red]++;
                lgreen[green]++;
                lblue[blue]++;
            }
        }
        Log.d("Hist", "Getting all the color.");
        for (int h = 0; h < LEVEL; h++) {
            pr[h] = (double) lred[h] / ((double) imageHeight * (double) imageHeight);
            pg[h] = (double) lgreen[h] / ((double) imageHeight * (double) imageWidth);
            pb[h] = (double) lblue[h] / ((double) imageHeight * (double) imageWidth);
        }


        //now we need to map to s domain
        for (int h = 0; h < LEVEL; h++) {
            //mapping the red color
            sr[h] = 0;
            for (int j = 0; j < h; j++) {
                sr[h] = sr[h] + pr[j];
            }
            sr[h] = (LEVEL - 1) * (sr[h]);

            //mapping the green color
            sg[h] = 0;
            for (int j = 0; j < h; j++) {
                sg[h] = sg[h] + pg[j];
            }
            sg[h] = (LEVEL - 1) * (sg[h]);

            //mapping the blue color
            sb[h] = 0;
            for (int j = 0; j < h; j++) {
                sb[h] = sb[h] + pb[j];
            }
            sb[h] = (LEVEL - 1) * (sb[h]);
        }

        //digital levels so round the values
        for (int h = 0; h < LEVEL; h++) {
            sr[h] = Math.round(sr[h]);
            sg[h] = Math.round(sg[h]);
            sb[h] = Math.round(sb[h]);
        }
        Log.d("Hist", "Getting the hist");
        //until now was the same code as histogram equalization
        //now starts the histogram matching part
        double[] pz = new double[LEVEL];
        double[] G = new double[LEVEL];
        //the first step is to set the wished histogram, that's, in our case,
        //we want to enhance the bright part of the colours, so values close to 255,
        //this is the reason that we implement an histogram with the ramp shape
        for (int h = 0; h < LEVEL; h++) {
            // pzGreen[h] = rampPositive(h);
            switch (opt) {
                case 1:
                case 2:
                case 3:
                    pz[h] = rampPositive(h);
                    break;
                case 4:
                case 5:
                case 6:
                    pz[h] = ramp(h);
                    break;
            }
        }//for
        //getting the G values of the histogram specified
        for (int h = 0; h < LEVEL; h++) {
            //mapping to the red color
            G[h] = 0;
            //accumulating
            for (int j = 0; j < h; j++) {
                G[h] = G[h] + pz[j];
            }//j
            G[h] = Math.round((LEVEL - 1) * (G[h]));
        }//h
        //now it is the new map from s to z

        //the new mapping
        //scanning all s mapping
        for (int k = 0; k < LEVEL; k++) {
            //scanning Gz mapping to re-map
            double temp = 0.0;
            double[] smallg = new double[2];
            double[] smallr = new double[2];
            double[] smallb = new double[2];
            //small[0] = index and small[1] = value
            smallg[0] = 0; //index or z
            smallg[1] = 10; //value

            smallr[0] = 0;
            smallr[1] = 10;

            smallb[0] = 0;
            smallb[1] = 10;

            for (int z = 0; z < LEVEL; z++) {
                //choosing the smallest number
                switch (opt) {
                    case 1: //bright red
                        temp = Math.abs(sr[k] - G[z]);
                        if (temp < smallr[1]) {
                            smallr[1] = temp;
                            smallr[0] = z;
                        }//if
                        break;
                    case 2: //bright green
                        temp = Math.abs(sg[k] - G[z]);
                        if (temp < smallg[1]) {
                            smallg[1] = temp;
                            smallg[0] = z;
                        }//if
                        break;
                    case 3: //bright blue
                        temp = Math.abs(sb[k] - G[z]);
                        if (temp < smallb[1]) {
                            smallb[1] = temp;
                            smallb[0] = z;
                        }//if
                        break;
                }
            }//z
            switch (opt) {
                case 1:
                    sr[k] = smallr[0];
                    break;
                case 2:
                    sg[k] = smallg[0];
                    break;
                case 3:
                    sb[k] = smallb[0];
                    break;
            }
        }//s mapping
        Log.d("Hist", "Mapping");
        for (int h = 0; h < imageToEdit.getHeight(); h++) {
            for (int w = 0; w < imageToEdit.getWidth(); w++) {
                int red = 0xff & (imageToEdit.getPixel(w, h) >> 16);
                int green = 0xff & (imageToEdit.getPixel(w, h) >> 8);
                int blue = 0xff & imageToEdit.getPixel(w, h);
                //if (red == l)
                {
                    red = (int) sr[red];
                }
                //if (green == l)
                {
                    green = (int) sg[green];
                }
                //if(blue == l)
                {
                    blue = (int) sb[blue];
                }
                int pix = 0xff000000 | (red << 16) | (green << 8) | blue;
                image.setPixel(w, h, pix);
            }
        }
        Log.d("Hist", "Creating edit image");
        return image;
    }


    public double ramp(int x) {
        double a = -2.0 / ((255.0) * (255.0));
        double b = 2.0 / (255.0);
        return ((double) x * a + b);
    }

    public double rampPositive(int x) {
        double a = (2.0 / (255.0 * 255.0));
        return ((double) x * a);
    }
}
