package com.hydra.modz.toram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hydra.modz.toram.Model.Payload;
import com.hydra.modz.toram.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpHeaders;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Login extends AppCompatActivity {
    public static native void Check();
    public static native String username();
    public static native String password();
    public static native String url(String path);
    public static native String Download_URL();

    private Button btLogin;

    private final static String TAG = "Login";

    final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Handler handler = new Handler();

    public static final int SYSTEM_ALERT_WINDOW_PERMISSION = 7;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        pd.dismiss();
//    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !Settings.canDrawOverlays(Login.this)) {
            RuntimePermissionForUser();
        }
        checkPermissions();
        System.loadLibrary("hydramodz");
        String dateTime;
        Calendar calendar;
        SimpleDateFormat sdf;
        //datetime
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        dateTime = sdf.format(calendar.getTime());

//        Toast.makeText(this, "url: " + url(""), Toast.LENGTH_SHORT).show();

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperatorName();
        String device = Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.DEVICE_NAME);

        btLogin = (Button)findViewById(R.id.button_login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View v) {
                try {
                    Runtime.getRuntime().exec("su");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //download
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
                    File dirz = new File(pathzz);
                    File filez = new File(pathzz, "hydramodz.so");

                    if (!dirz.exists()) {
                        dirz.mkdirs();
                    } else if (file.isFile()) {
                        System.out.println("Login: File exists!");
                        Runtime.getRuntime().exec("su -c cp "+pathQ+"/hydramodz.so"+" "+pathzz+"/hydramodz.so");
                    }

                    if (filez.isFile()) {
                        Toast.makeText(Login.this, "Ready!" , Toast.LENGTH_LONG).show();
                    }

                    if (!file.isFile()) {
                        new DownloadFileFromURL().execute(Download_URL());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //login
                String android_id = "";
                if (networkOperator.contains("51001")) {
                    File hydraconfig = new File(getFilesDir(), "hydraconfig.txt");
                    if (!hydraconfig.exists()) {
                        writeToFile(MD5_Hash(dateTime+"hydramodz"), Login.this);
                    } else if (hydraconfig.exists() && readFromFile(Login.this) != null){
                        String device_id = readFromFile(Login.this);
                        android_id = MD5_Hash(device_id.trim().replaceAll("\\s+", " "));
                    }
                } else {
                    android_id = MD5_Hash(Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID)+"hydramodz");
                }

                if (isNetworkAvailable()) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new OkHttpCustomInterceptor(username(), password()))
                            .build();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                    builder.setView(customLayout);
                    LottieAnimationView lav = customLayout.findViewById(R.id.lottie);

                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    JSONObject object = new JSONObject();
                    try {
                        object.put("device_id", android_id);
//                        object.put("device_id", "26012001");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(object.toString(), JSON);
                    Request request = new Request.Builder()
                            .url(url("getdeviceid"))
                            .post(body)
                            .build();

                    try {
                        Response r = client.newCall(request).execute();
                        new Handler().post(new Runnable() {
                            @SuppressLint("Range")
                            @Override
                            public void run() {
                                if (r.isSuccessful()) {
                                    Gson gson = new Gson();
                                    User user = null;
                                    try {
                                        user = gson.fromJson(r.body().string(), User.class);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (user.isStatus() == true) {
                                        lav.setMinProgress(0f);
                                        lav.setMaxProgress(0.47f);
                                        lav.loop(false);
                                        User finalUser = user;
                                        lav.addAnimatorListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                Intent i = new Intent(Login.this, MainActivity.class);
                                                i.putExtra("name", finalUser.getPayload().getUsername().toString());
                                                i.putExtra("expired_at", finalUser.getPayload().getExpiry().toString());
                                                i.putExtra("is_expired", finalUser.getPayload().isExpired());
                                                i.putExtra("device_name", device.toString());
                                                dialog.dismiss();
                                                startActivity(i);
                                                finish();
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                                    }
                                } else {
                                    lav.setMinProgress(0.55f);
                                    lav.setMaxProgress(1f);
                                    lav.loop(false);
                                    lav.addAnimatorListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.show();
                }
            }
        });
    }

private void writeToFile(String data, Context context) {
    try {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("hydraconfig.txt", Context.MODE_PRIVATE));
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }
    catch (IOException e) {
        Log.e("Exception", "File write failed: " + e);
    }
}

    private String readFromFile(Context context) {

        String text = "";

        try {
            InputStream inputStream = context.openFileInput("hydraconfig.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                text = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e);
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e);
        }
        return text;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public String MD5_Hash(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(Login.this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(Login.this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

//    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Login.this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        Login.this.finish();
                        return;
                    }
                }
                break;
        }
    }

    public void RuntimePermissionForUser() {
        Intent PermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(PermissionIntent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String pathFolder = "";
        String pathFile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(true);
            //pd.show();
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
                pathFile = pathQ + "/hydramodz.so";
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
}
