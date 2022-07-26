package com.hydra.modz.toram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {
    final boolean downloadComplete = false;
    private Button start,start2;
    private final static String TAG = "MainActivity";
    private CheckBox a11;

    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        pDialog.dismiss();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a11= (CheckBox)findViewById(R.id.a11);
        start = (Button)findViewById(R.id.btStart);
        String device = getIntent().getStringExtra("device_name");
        String name = getIntent().getStringExtra("name");
        String expiry = getIntent().getStringExtra("expired_at");
        boolean isExpired = getIntent().getBooleanExtra("is_expired",false);

        final TextView tvName = (TextView)findViewById(R.id.tv_username);
        final TextView tvDevice = (TextView) findViewById(R.id.tv_devicename);
        final TextView tvExpiry = (TextView) findViewById(R.id.tv_expired);
        final TextView tvExpired = (TextView) findViewById(R.id.tv_status);

        tvName.setText(name);
        tvDevice.setText(device);
        tvExpiry.setText(expiry);
        if (isExpired) {
            tvExpired.setText("Non-Active");
            tvExpired.setTextColor(Color.RED);
            start.setBackgroundColor(Color.GRAY);
            start.setEnabled(false);
            start.setClickable(false);
        } else {
            tvExpired.setText("Active");
        }

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.asobimo.toramonline");

//        start2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, FloatingService.class);
//                startService(i);
//            }
//        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a11.isChecked()){
                    Intent i = new Intent(MainActivity.this, FloatingService.class);
                    startService(i);
                }
                else{
                    String pathQ = "";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        pathQ = getApplicationContext().getExternalFilesDir(null)+"/assets";
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        pathQ = Environment.getExternalStorageDirectory()+"/assets";
                    } else {
                        pathQ = Environment.getExternalStorageDirectory()+"/assets";
                    }
                    try {
                        File dirs = new File(pathQ);
                        File file = new File(dirs, "hydramodz.so");

                        String pathzz = "/data/data/com.hydra.modz.toram/files/assets";
                        Runtime.getRuntime().exec("su -c chmod 777 /data/data/com.hydra.modz.toram/files/assets/hydramodz.so");
                        File dirz = new File(pathzz);
                        File filez = new File(pathzz, "hydramodz.so");

                        if (!dirz.exists()) {
                            dirz.mkdirs();
                        }
                        if (file.isFile()) {
                            //status.setText("File ada");
                            System.out.println("MainActivity: File exists");
                            Runtime.getRuntime().exec("cp "+pathQ+"/hydramodz.so"+" "+pathzz+"/hydramodz.so");
                        }

                        if (filez.isFile()) {
                            Toast.makeText(MainActivity.this, "Ready!" , Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(launchIntent);
                    Intent i = new Intent(MainActivity.this, FloatingService.class);
                    startService(i);
                }
            }
        });
    }



    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String pathFolder = "";
        String pathFile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String pathQ = "";
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                pathQ = getApplicationContext().getExternalFilesDir(null)+"/assets";
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pathQ = Environment.getExternalStorageDirectory()+"/assets";
                } else {
                    pathQ = Environment.getExternalStorageDirectory()+"/assets";
                }
                pathFolder = getFilesDir().toString();
                pathFile = pathQ + "/hydramodz";
                File futureStudioIconFile = new File(pathQ);
                if (!futureStudioIconFile.exists()) {
                    futureStudioIconFile.mkdirs();
                }

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lengthOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                FileOutputStream output = new FileOutputStream(pathFile);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return pathFile;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pd.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pd != null) {
                pd.dismiss();
            }
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static void copyNow(File source, File dest) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.copy(source.toPath(), dest.toPath());
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit!", Toast.LENGTH_LONG).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}