package com.example.alzheimersapp.PatientModule.Painting;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alzheimersapp.CaregiverModule.CaregiverDashboard;
import com.example.alzheimersapp.R;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class PaintingActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, backgroundColorBtn;
    private float smallBrush, mediumBrush, largeBrush;
    boolean isImageSaved = false;
    String color, colorValue;
    int backgroundColor = 0;
    int BtnColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting);

        requestPermissions();

        drawView = findViewById(R.id.drawing);
        LinearLayout paintLayout = findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawView.setBrushSize(mediumBrush);


        drawBtn = findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        eraseBtn = findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        backgroundColorBtn = findViewById(R.id.background_btn);
        backgroundColorBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        //respond to clicks
        if (view.getId() == R.id.draw_btn) {
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
//            Paint p = new Paint();
//            p.setAlpha(0xFF);
//            p.setColor(Color.WHITE);
            drawView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            final ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                   // drawView.setColor("#FFFFFF");
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                   // drawView.setColor("#FFFFFF");
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    //drawView.setColor("#FFFFFF");
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    setColor("#FFFFFFFF");
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();
        } else if (view.getId() == R.id.save_btn) {
            saveImageToGallery();
        }

        else if(view.getId() == R.id.background_btn){
            changeColor();
        }
    }

    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        //use chosen color
        if (view != currPaint) {
            //update color
            ImageButton imgView = (ImageButton) view;
            color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;

        }
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(!isImageSaved){
            saveImageToGallery();
        }
        else{
            finish();
        }
    }

    public void saveImageToGallery(){
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Enter the name for image");
        final EditText input = new EditText(this);
        saveDialog.setView(input);
        saveDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //getting the name of image from user
                String imageName = input.getText().toString();

                //save drawing
                drawView.setDrawingCacheEnabled(true);
                Bitmap bitmap = drawView.getDrawingCache();
                int count = 0;

                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
                File myDir = new File(root + "/Paint My Screen");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = imageName + n + ".jpg";
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    Toast.makeText(PaintingActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
                    isImageSaved = true;
                    out.flush();
                    out.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(PaintingActivity.this, new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });

                drawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        saveDialog.show();
    }

    public void changeColor(){
        backgroundColor++;
        if(backgroundColor == 1){
            setColor("#FF660000");
        }

        else if(backgroundColor == 2){
            setColor("#FFFF0000");
        }

        else if(backgroundColor == 3){
            setColor("#FFFF6600");
        }

        else if(backgroundColor == 4){
            setColor("#FFFFCC00");
        }

        else if(backgroundColor == 5){
            setColor("#FF009900");
        }

        else if(backgroundColor == 6){
            setColor("#FF009999");
        }

        else if(backgroundColor == 7){
            setColor("#FF0000FF");
        }

        else if(backgroundColor == 8){
            setColor("#FF990099");
        }


        else if(backgroundColor == 9){
            setColor("#FFFF6666");
        }

        else if(backgroundColor == 10){
            setColor("#FFFFFFFF");
        }

        else if(backgroundColor == 11){
            setColor("#FF787878");
        }

        else if(backgroundColor == 12){
            setColor("#FF000000");
        }
        else if(backgroundColor == 13){
            backgroundColor = 1;
        }
    }

    public void setColor(String myColor){
        drawView.setBackgroundColorOfView(myColor);
        BtnColor = Color.parseColor(myColor);
        backgroundColorBtn.setBackgroundColor(BtnColor);
    }
}