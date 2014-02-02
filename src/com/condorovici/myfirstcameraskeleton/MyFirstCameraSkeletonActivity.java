package com.condorovici.myfirstcameraskeleton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    File originalFile = new File(originalPicPath);
    Uri originalFileUri = Uri.fromFile(originalFile);
    String editPicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempOriginal2.jpg";
    File editFile = new File(editPicPath);
    Uri editFileUri = Uri.fromFile(editFile);
    String refPicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/rosuverde.jpg";
    File refFile = new File(refPicPath);
    Uri refFileUri = Uri.fromFile(refFile);

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
        Bitmap sampleBmp;
        Bitmap imageBmpMutable;
        // Decodarea imaginii din fisier
        origBmp = BitmapFactory.decodeFile(originalFileUri.getPath());
        sampleBmp = BitmapFactory.decodeFile(refFileUri.getPath());
        // Crearea unei copii mutable a Bitmap-ului
        imageBmpMutable = origBmp.copy(origBmp.getConfig(), true);
        // Stergerea obiectului initial
        origBmp.recycle();

        imageBmpMutable = histogram_matching_rgb(imageBmpMutable, sampleBmp, 1);
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

    private Bitmap histogram_matching_rgb(Bitmap imageToEditBmp, Bitmap sampleBmp, int opt) {
        final int LEVEL = 256;
        //calculez CRF pentru Imaginea originala
        Double[] sr = new Double[LEVEL];
        Double[] sg = new Double[LEVEL];
        Double[] sb = new Double[LEVEL];
        getCRF(imageToEditBmp, sr, sg, sb);
        //Calculez CRF pentru Imaginea de Referinta
        Double[] srRef = new Double[LEVEL];
        Double[] sgRef = new Double[LEVEL];
        Double[] sbRef = new Double[LEVEL];
//        getCRF(sampleBmp, srRef, sgRef, sbRef);

        //until now was the same code as histogram equalization
        //now starts the histogram matching part
        Double[] pz = new Double[LEVEL];
        Double[] G = new Double[LEVEL];
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
            G[h] = 0.0;
            //accumulating
            for (int j = 0; j < h; j++) {
                G[h] = G[h] + pz[j];
            }//j
            G[h] = (double) Math.round((LEVEL - 1) * (G[h]));
        }//h
        //now it is the new map from s to z

        reshapeHist(LEVEL, sr, sg, sb, G, G, G, opt);

        Log.d("Hist", "Mapping");
        imageToEditBmp = remapImage(imageToEditBmp, sr, sg, sb);

        Log.d("Hist", "Creating edit image");
        return imageToEditBmp;
    }

    private void reshapeHist(int LEVEL, Double[] sr, Double[] sg, Double[] sb, Double[] srRef, Double[] sgRef, Double[] sbRef, int opt) {
        for (int k = 0; k < LEVEL; k++) {
            double temp;
            double[] smallg = new double[2];
            double[] smallr = new double[2];
            double[] smallb = new double[2];
            //small[0] = index and small[1] = valoare
            smallg[0] = 0;
            smallg[1] = 10;

            smallr[0] = 0;
            smallr[1] = 2;

            smallb[0] = 0;
            smallb[1] = 10;

            for (int z = 0; z < LEVEL; z++) {
                //se face la cel mai apropiata valoarea a pixelui
                switch (opt) {

                    case 1:
                        temp = Math.abs(sr[k] - srRef[z]);
                        if (temp < smallr[1]) {
                            smallr[1] = temp;
                            smallr[0] = z;
                        }//if
                        break;
                    case 2:
                        temp = Math.abs(sg[k] - sgRef[z]);
                        if (temp < smallg[1]) {
                            smallg[1] = temp;
                            smallg[0] = z;
                        }//if
                        break;
                    case 3:
                        temp = Math.abs(sb[k] - sbRef[z]);
                        if (temp < smallb[1]) {
                            smallb[1] = temp;
                            smallb[0] = z;
                        }
                        break;
                }//z
            }
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
        }
    }

    private Bitmap remapImage(Bitmap imageToEdit, Double[] sr, Double[] sg, Double[] sb) {
        int imageWidth = imageToEdit.getWidth();
        int imageHeight = imageToEdit.getHeight();
        Bitmap image = Bitmap.createScaledBitmap(imageToEdit, imageWidth, imageHeight, false);
        int[][] redPixel = new int[imageWidth][imageHeight];
        int[][] greenPixel = new int[imageWidth][imageHeight];
        int[][] bluePixel = new int[imageWidth][imageHeight];

        for (int h = 0; h < imageToEdit.getHeight(); h++) {
            for (int w = 0; w < imageToEdit.getWidth(); w++) {
                int pixel = imageToEdit.getPixel(w, h);
                redPixel[w][h] = Color.red(pixel);
                greenPixel[w][h] = Color.green(pixel);
                bluePixel[w][h] = Color.blue(pixel);
                //if (red == l)
                {
                    redPixel[w][h] = (int) sr[redPixel[w][h]].longValue();
                }
                //if (green == l)
                {
                    greenPixel[w][h] = (int) sg[greenPixel[w][h]].longValue();
                }
                //if(blue == l)
                {
                    bluePixel[w][h] = (int) sb[bluePixel[w][h]].longValue();
                }
                int pix = 0xff000000 | (redPixel[w][h] << 16) | (greenPixel[w][h] << 8) | bluePixel[w][h];
                image.setPixel(w, h, pix);
            }
        }
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

    private void getCRF(Bitmap imageToEdit, Double[] sr, Double[] sg, Double[] sb) {
        int imageWidth = imageToEdit.getWidth();
        int imageHeight = imageToEdit.getHeight();

        final int LEVEL = 256;
        int[] lred = new int[LEVEL];
        int[] lgreen = new int[LEVEL];
        int[] lblue = new int[LEVEL];
        double[] pr = new double[LEVEL];
        double[] pg = new double[LEVEL];
        double[] pb = new double[LEVEL];

        int[][] redPixel = new int[imageWidth][imageHeight];
        int[][] greenPixel = new int[imageWidth][imageHeight];
        int[][] bluePixel = new int[imageWidth][imageHeight];


        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                int pixel = imageToEdit.getPixel(w, h);
                redPixel[w][h] = Color.red(pixel);
                greenPixel[w][h] = Color.green(pixel);
                bluePixel[w][h] = Color.blue(pixel);
                //calcularea histogramei
                lred[redPixel[w][h]]++;
                lgreen[greenPixel[w][h]]++;
                lblue[bluePixel[w][h]]++;
            }
        }
        Log.d("Hist", "Getting all the color.");
        for (int h = 0; h < LEVEL; h++) {
            pr[h] = (double) lred[h] / ((double) imageHeight * (double) imageWidth);
            pg[h] = (double) lgreen[h] / ((double) imageHeight * (double) imageWidth);
            pb[h] = (double) lblue[h] / ((double) imageHeight * (double) imageWidth);
        }


        //calcularea domeniului
        for (int h = 0; h < LEVEL; h++) {
            //maparea culori rosi
            sr[h] = 0.0;
            for (int j = 0; j < h; j++) {
                sr[h] = sr[h] + pr[j];
            }
            sr[h] = (LEVEL - 1) * (sr[h]);

            //maparea culori verzi
            sg[h] = 0.0;
            for (int j = 0; j < h; j++) {
                sg[h] = sg[h] + pg[j];
            }
            sg[h] = (LEVEL - 1) * (sg[h]);

            //maparea culorii albastre
            sb[h] = 0.0;
            for (int j = 0; j < h; j++) {
                sb[h] = sb[h] + pb[j];
            }
            sb[h] = (LEVEL - 1) * (sb[h]);
        }

        //se face o digitizare
        for (int h = 0; h < LEVEL; h++) {
            sr[h] = (double) Math.round(sr[h]);
            sg[h] = (double) Math.round(sg[h]);
            sb[h] = (double) Math.round(sb[h]);
        }
    }
}
