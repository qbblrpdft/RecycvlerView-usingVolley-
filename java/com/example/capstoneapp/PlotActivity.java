package com.example.capstoneapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.Settings;
import android.text.InputType;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstoneapp.Request.VolleyRequest;
import com.example.capstoneapp.Response.MyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlotActivity extends AppCompatActivity {
    private String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final GlobalClass global = new GlobalClass();
    private int matchId;
    private double latitude;
    private double longitude;
    private String teamName;
    private String opponent;
    private String dateCreated;
//    private int imageX;
//    private int imageY;
    ImageView halfCourtView;
    Button saveCSV;
    Button missed;
    Button made;
    Button firstQtr;
    Button secondQtr;
    Button thirdQtr;
    Button fourthQtr;
    Button undo;
//    Button redo;
    Button delete;
    Button resultbtn;

    Button cluster;
    TextView madeShots;
    TextView missedShots;
    TextView totalShots;
    int madeCount = 0;
    int missedCount = 0;
    int attemptsCount = 0;

    // Initialize quarter variable to 1 for the first quarter
    final int[] quarter = {1};
    private boolean madeClicked;
    private boolean missedClicked;
    private boolean deleteClicked = false;
    private boolean isQuarterSelected = false;
    private Stack<Bitmap> undoStack = new Stack<>();
    private Stack<Bitmap> redoStack = new Stack<>();
    List<double[]> touchData = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_activtiy);

        if (checkPermission()) {
            Toast.makeText(PlotActivity.this,"WE Have Permission",Toast.LENGTH_SHORT).show();   // WE have a permission just start your work.
        } else {
            requestPermission(); // Request Permission
        }
        //Add these line of code in onCreate Method
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult( ActivityResult result ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager())
                        Toast.makeText(PlotActivity.this,"We Have Permission",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(PlotActivity.this, "You Denied the permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlotActivity.this, "You Denied the permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        halfCourtView = findViewById(R.id.halfCourtImage);
        made = findViewById(R.id.btnMade);
        missed = findViewById(R.id.btnMissed);
        firstQtr = findViewById(R.id.btn1);
        secondQtr = findViewById(R.id.btn2);
        thirdQtr = findViewById(R.id.btn3);
        fourthQtr = findViewById(R.id.btn4);
        undo = findViewById(R.id.btnUndo);
//        redo = findViewById(R.id.btnRedo);
        madeShots = findViewById(R.id.shotsMade);
        missedShots = findViewById(R.id.shotsMissed);
        totalShots = findViewById(R.id.attemptedShots);
        resultbtn = findViewById(R.id.btnResult);
        cluster = findViewById(R.id.btnCluster);
        saveCSV = findViewById(R.id.btnSave);
        TextView shotsMissed = findViewById(R.id.shotsMissed);
        TextView shotsMade = findViewById(R.id.shotsMade);
        TextView attemptedShots = findViewById(R.id.attemptedShots);

        cluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize two ArrayLists to hold made and missed shots
                ArrayList<double[]> madeShots = new ArrayList<>();
                ArrayList<double[]> missedShots = new ArrayList<>();

                // Iterate through the touchData list and add each point to either the madeShots or missedShots list
                for (int i = 0; i < touchData.size(); i++) {
                    double[] point = touchData.get(i);
                    if (point[2] == 1) {
                        madeShots.add(point);
                    } else {
                        missedShots.add(point);
                    }
                }

                // Display the madeShots and missedShots in separate colors on the halfCourtView
//                halfCourtView.drawPoints(madeShots, Color.GREEN);
//                halfCourtView.drawPoints(missedShots, Color.RED);
            }
        });


//        undo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!undoStack.empty()) {
//                    Bitmap lastState = undoStack.pop();
//                    redoStack.push(lastState);
//                    if (!undoStack.empty()) {
//                        lastState = undoStack.peek();
//                    }
//                    halfCourtView.setImageDrawable(new BitmapDrawable(getResources(), lastState));
//                }
//            }
//        });
//
//        redo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!redoStack.empty()) {
//                    Bitmap lastState = redoStack.pop();
//                    undoStack.push(lastState);
//                    halfCourtView.setImageDrawable(new BitmapDrawable(getResources(), lastState));
//                }
//            }
//        });

        SaveDialog saveDialog = new SaveDialog(this, new SaveDialog.SaveDialogListener() {
            @Override
            public void onSave(File file) {
                try {
                    // read data from CSV file
                    File csvFile = new File(file.getParentFile(), file.getName());

                    BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] fields = line.split(",");
                    }
                    reader.close();

                    // Write data to file if not empty
                    String filteredData = "".trim();
                    errorToast("No data found for the selected match ID.");

                    // Write data to the file in the Documents folder
                    FileWriter writer = new FileWriter(new File(file.getParentFile(), file.getName()));
                    writer.write(filteredData);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    // Display an error message
                    successToast("File saved.");
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancel() {
                Toast.makeText(PlotActivity.this, "Save canceled", Toast.LENGTH_SHORT).show();
            }
        });


        saveCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDialog.show(getSupportFragmentManager(), "SaveDialog");
            }
        });

//        saveCSV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String fileName = "data_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".csv";
//                File csvFile = new File(getExternalFilesDir(null), fileName); // Get the path for the CSV file
//                Uri csvUri = FileProvider.getUriForFile(PlotActivity.this, "com.example.capstoneapp.provider", csvFile); // Get the URI for the CSV file
//
//                // Create an intent to share the CSV file
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/csv");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, csvUri);
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, "Share CSV File"));
//            }
//        });

        //working
        halfCourtView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (madeClicked || missedClicked) { // check if either button is clicked
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Drawable drawable = halfCourtView.getDrawable();
                        Rect bounds = drawable.getBounds();

                        int imageX = (int) event.getX() - bounds.left;
                        int imageY = (int) event.getY() - bounds.top;

                        // Store the touch input data as a double array
                        double[] point = {imageX, imageY};
                        touchData.add(point);

                        if (madeClicked) {
                            madeCount++; // increment the made button counter
                        } else {
                            missedCount++; // increment the missed button counter
                        }
                        attemptsCount++; // increment the attempts counter
                        shotsMade.setText("Shots Made: " + madeCount); // update the made shots text view
                        shotsMissed.setText("Shots Missed: " + missedCount); // update the missed shots text view
                        attemptedShots.setText("Attempted Shots: " + attemptsCount); // up

                        String matchId = getIntent().getStringExtra("matchId");
                        Map<String, Object> jsonParams = new HashMap<>();
                        jsonParams.put("matchId", matchId);
                        jsonParams.put("longitude", imageX);
                        jsonParams.put("latitude", imageY);
                        jsonParams.put("quarter", quarter[0]); // pass the current quarter
                        jsonParams.put("type", madeClicked ? "made" : "missed");
                        VolleyRequest.plot(PlotActivity.this, jsonParams, new MyResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String plotId = data.getString("id");
                                    Log.d("postRequest", "Plot ID: " + plotId);

                                    String csvFileName = "data.csv"; // Specify the name of the CSV file
                                    String csvHeader = "matchId,X,Y,quarter,type\n"; // Specify the header for the CSV file

                                    try {
                                        File csvFile = new File(getExternalFilesDir(null), csvFileName); // Get the path for the CSV file
                                        if (!csvFile.exists()) { // Check if the CSV file exists
                                            csvFile.createNewFile(); // Create the CSV file if it doesn't exist
                                            FileWriter fileWriter = new FileWriter(csvFile);
                                            fileWriter.append(csvHeader); // Write the header to the CSV file
                                            fileWriter.flush();
                                            fileWriter.close();
                                        }

                                        // Write the data to the CSV file
                                        FileWriter fileWriter = new FileWriter(csvFile, true);
                                        fileWriter.append(matchId).append(",");
                                        fileWriter.append(String.valueOf(imageX)).append(",");
                                        fileWriter.append(String.valueOf(imageY)).append(",");
                                        fileWriter.append(String.valueOf(quarter[0])).append(",");
                                        fileWriter.append(madeClicked ? "made" : "missed").append("\n");
                                        fileWriter.flush();
                                        fileWriter.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    Log.e(TAG, "Error parsing response body: " + e.getMessage());
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG, "Failed to execute request: " + error);
                            }
                        });


//                        global.postRequest(global.getServerURL() +"/plotting", jsonParams, PlotActivity.this, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                Log.e(TAG, "Failed to execute request: " + e.getMessage());
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                String responseBody = response.body().string();
//                                Log.d(TAG, "Response: " + responseBody);
//
//                                try {
//                                    JSONObject jsonObject = new JSONObject(responseBody);
//                                    JSONObject data = jsonObject.getJSONObject("data");
//                                    String plotId = data.getString("id");
//                                    Log.d("postRequest", "Plot ID: " + plotId);
//
//                                    // Write the data to CSV file
//                                    String csvFileName = "data.csv"; // Specify the name of the CSV file
//                                    String csvHeader = "matchId,X,Y,quarter,type\n"; // Specify the header for the CSV file
//
//                                    try {
//                                        File csvFile = new File(getExternalFilesDir(null), csvFileName); // Get the path for the CSV file
//                                        if (!csvFile.exists()) { // Check if the CSV file exists
//                                            csvFile.createNewFile(); // Create the CSV file if it doesn't exist
//                                            FileWriter fileWriter = new FileWriter(csvFile);
//                                            fileWriter.append(csvHeader); // Write the header to the CSV file
//                                            fileWriter.flush();
//                                            fileWriter.close();
//                                        }
//
//                                        // Write the data to the CSV file
//                                        FileWriter fileWriter = new FileWriter(csvFile, true);
//                                        fileWriter.append(matchId).append(",");
////                                        fileWriter.append(getIntent().getStringExtra("matchId")).append(",");
//                                        fileWriter.append(String.valueOf(imageX)).append(",");
//                                        fileWriter.append(String.valueOf(imageY)).append(",");
//                                        fileWriter.append(String.valueOf(quarter[0])).append(",");
//                                        fileWriter.append(madeClicked ? "made" : "missed").append("\n");
//                                        fileWriter.flush();
//                                        fileWriter.close();
//
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                } catch (JSONException e) {
//                                    Log.e(TAG, "Error parsing response body: " + e.getMessage());
//                                }
//                            }
//                        });

                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        paint.setColor(madeClicked ? Color.parseColor("#B1D8B7") : Color.parseColor("#C02929"));

                        Bitmap bitmap = Bitmap.createBitmap(halfCourtView.getWidth(), halfCourtView.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawBitmap(((BitmapDrawable) drawable).getBitmap(), null, bounds, null);
                        canvas.drawCircle(imageX, imageY, 15, paint);
                        halfCourtView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

                        undoStack.push(bitmap); // Add current state to undo stack
                        redoStack.clear(); // Clear redo stack
                        Log.d(madeClicked ? "MADE" : "MISSED", "X: " + imageX + ", Y: " + imageY);
                    }
                    return true;
                }
                return false;
            }
        });

//        saveCSV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String csvFileName = getIntent().getStringExtra("matchId") + "data.csv";
//                File csvFile = new File(getExternalFilesDir(null), csvFileName);
//
//                // Read data from the CSV file
//                StringBuilder data = new StringBuilder();
//                try {
//                    BufferedReader reader = new BufferedReader(new FileReader(csvFile));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        data.append(line).append("\n");
//                    }
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                // Create the sharing intent
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/csv");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, data.toString());
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                // Start the sharing activity
//                startActivity(Intent.createChooser(shareIntent, "Share CSV File"));
//            }
//        });

        // Set click listeners for the quarter buttons
        firstQtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quarter[0] = 1;
                firstQtr.setBackgroundColor(Color.parseColor("#CB3636"));
                firstQtr.setTextColor(Color.parseColor("#FFFFFF"));
                secondQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                thirdQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                fourthQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
            }
        });

        secondQtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quarter[0] = 2;
                firstQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                secondQtr.setBackgroundColor(Color.parseColor("#CB3636"));
                secondQtr.setTextColor(Color.parseColor("#FFFFFF"));
                thirdQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                fourthQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
            }
        });

        thirdQtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quarter[0] = 3;
                firstQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                secondQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                thirdQtr.setBackgroundColor(Color.parseColor("#CB3636"));
                thirdQtr.setTextColor(Color.parseColor("#FFFFFF"));
                fourthQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
            }
        });

        fourthQtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quarter[0] = 4;
                firstQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                secondQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                thirdQtr.setBackgroundColor(Color.parseColor("#d4d3d6"));
                fourthQtr.setBackgroundColor(Color.parseColor("#CB3636"));
                fourthQtr.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

// Set click listeners for the made and missed buttons
        made.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                madeClicked = true;
                missedClicked = false;
                made.setBackgroundColor(Color.parseColor("#B1D8B7"));
                missed.setBackgroundColor(Color.GRAY);
            }
        });

        missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missedClicked = true;
                madeClicked = false;
                missed.setBackgroundColor(Color.parseColor("#C02929"));
                made.setBackgroundColor(Color.GRAY);
            }
        });
    }
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(PlotActivity.this)
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                                activityResultLauncher.launch(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activityResultLauncher.launch(intent);
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();

        } else {

            ActivityCompat.requestPermissions(PlotActivity.this, permissions, 30);
        }
    }
    public void successToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.success_toast,
                (ViewGroup) findViewById(R.id.success_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.success_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.TOP, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    public void errorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_toast,
                (ViewGroup) findViewById(R.id.error_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.error_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.TOP, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}