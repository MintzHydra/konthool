package com.hydra.modz.toram;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FloatingService extends Service implements AdapterView.OnItemSelectedListener {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private Handler handler = new Handler();
    Context context = this;
    Switch godmode, crit, stab, nogr, noev, nogu, ldefmdef, deff, mdeff, autogd, radius, inscs, unboss, cutsc, nodom, ailm, buff, cf, mobinv, cdmg,aspd, crate, insfin, skillburst, nomove;
    Spinner ele;
    SeekBar skdamage, skaspd, skCr;
    TextView txAspd, txDmg, txCr;
    ToggleButton bypass;
    private LinearLayout linearLayout;
    String res = "";


    public FloatingService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.lgl_layout, null);
        String[] elementArray = {"--Please Select Element--","None","Fire", "Water", "Wind", "Earth", "Light", "Dark", "Neutral"};
        //activity = (MainActivity) act.mActivity;

        godmode = (Switch)mFloatingView.findViewById(R.id.godmode);
        crit = (Switch)mFloatingView.findViewById(R.id.crit);
        stab = (Switch)mFloatingView.findViewById(R.id.stable);
        nogr = (Switch)mFloatingView.findViewById(R.id.nograze);
        nogu = (Switch)mFloatingView.findViewById(R.id.nogu);
        noev = (Switch)mFloatingView.findViewById(R.id.noev);
        ldefmdef = (Switch)mFloatingView.findViewById(R.id.ldeffmdeff);
        deff = (Switch)mFloatingView.findViewById(R.id.deff);
        mdeff = (Switch)mFloatingView.findViewById(R.id.mdeff);
        autogd = (Switch)mFloatingView.findViewById(R.id.autoguard);
        radius = (Switch)mFloatingView.findViewById(R.id.radius);
        inscs = (Switch)mFloatingView.findViewById(R.id.inscs);
        unboss = (Switch)mFloatingView.findViewById(R.id.unlockboss);
        cutsc = (Switch)mFloatingView.findViewById(R.id.cutscene);
        nodom = (Switch)mFloatingView.findViewById(R.id.nodomi);
        ailm = (Switch)mFloatingView.findViewById(R.id.ailment);
        buff = (Switch)mFloatingView.findViewById(R.id.unlibuff);
        cf = (Switch)mFloatingView.findViewById(R.id.cf);
        mobinv = (Switch)mFloatingView.findViewById(R.id.mobinv);
        ele = (Spinner)mFloatingView.findViewById(R.id.sp_ele);
        cdmg = (Switch)mFloatingView.findViewById(R.id.dmgboost);
        aspd = (Switch)mFloatingView.findViewById(R.id.aspd);
        crate = (Switch)mFloatingView.findViewById(R.id.crate);
        insfin = (Switch) mFloatingView.findViewById(R.id.insfin);
        skillburst = (Switch) mFloatingView.findViewById(R.id.skillburst);
        nomove = (Switch) mFloatingView.findViewById(R.id.nomove);

        //seekbar
        skaspd = (SeekBar)mFloatingView.findViewById(R.id.value_aspd);
        skdamage = (SeekBar)mFloatingView.findViewById(R.id.value_damage);
        skCr = (SeekBar)mFloatingView.findViewById(R.id.value_cr);

        //toggle button
        bypass = (ToggleButton) mFloatingView.findViewById(R.id.bypass);

        //textview
        txAspd = (TextView) mFloatingView.findViewById(R.id.textValueAspd);
        txDmg = (TextView) mFloatingView.findViewById(R.id.textValueDmg);
        txCr = (TextView) mFloatingView.findViewById(R.id.textValueCr);


        ele.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, elementArray);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ele.setAdapter(ad);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

//        Typeface face = Typeface.createFromAsset(getAssets(),
//                "font/orbit.ttf");

        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);
        final View exitz = mFloatingView.findViewById(R.id.exit);
//        final View moveTitle = mFloatingView.findViewById(R.id.move);
//
//
//        title.setSelected(true);

        final View back = mFloatingView.findViewById(R.id.ChangeWindow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        exitz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
                HYDRAROOT("/assets/hydramodz.so 61 0","EXIT GAME");
                killApp();
            }
        });

        collapsedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapsedView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
            }
        });

        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        mFloatingView.findViewById(R.id.t2).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

        /**
         * Initialize all cheat
         */
        bypass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bypass.setBackgroundColor(Color.parseColor("#B000FF00"));
                    HYDRAROOT("/assets/hydramodz.so 1 0","BYPASS ON");
                } else {
                    bypass.setBackgroundColor(Color.parseColor("#B0FF0000"));
                    HYDRAROOT("/assets/hydramodz.so 2 0","BYPASS OFF");
                }
            }
        });

        godmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 3 0","Immortal ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 4 0","Immortal OFF");
                }
            }
        });

        crit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 53 0","Always Crit ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 54 0","Always Crit OFF");
                }
            }
        });

        stab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 5 0","Stable ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 6 0","Stable OFF");
                }
            }
        });

        nogr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 7 0","No Graze ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 8 0","No Graze OFF");
                }
            }
        });

        noev.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 9 0","No Eva ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 10 0","No Eva OFF");
                }
            }
        });

        nogu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 11 0","No Guard ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 12 0","No Guard OFF");
                }
            }
        });

        ldefmdef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 13 0","Low Deff/M Deff ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 14 0","Low Deff/M Deff OFF");
                }
            }
        });

        deff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 15 0","Deff ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 16 0","Deff OFF");
                }
            }
        });

        mdeff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 17 0","M Deff ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 18 0","M Deff OFF");
                }
            }
        });

        autogd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 19 0","Auto Guard ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 20 0","Auto Guard OFF");
                }
            }
        });

        radius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 21 0","Big Radius ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 22 0","Big Radius OFF");
                }
            }
        });

        inscs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 23 0","Instant Cast ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 24 0","Instant Cast OFF");
                }
            }
        });

        unboss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 27 0","Unlock Boss No MQ ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 28 0","Unlock Boss no MQ OFF");
                }
            }
        });

        cutsc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 29 0","Cutscene ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 30 0","Cutscene OFF");
                }
            }
        });

        nodom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 31 0","No Domination ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 32 0","No Domination OFF");
                }
            }
        });

        ailm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 33 0","Ailment 100% ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 34 0","Ailment 100% OFF");
                }
            }
        });

        cf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 35 0","Instant Charge CF ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 36 0","Instant Charge CF OFF");
                }
            }
        });

        buff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 37 0","Buff Unlimited ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 38 0","Buff Unlimited OFF");
                }
            }
        });

        mobinv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 39 0","Mob Invicible ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 40 0","Mob Invicible OFF");
                }
            }
        });

        nomove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 57 0","No Move ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 58 0","No Move OFF");
                }
            }
        });

        insfin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 59 0","Instant Finale ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 60 0","Instant Finale OFF");
                }
            }
        });

        skillburst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, "DON'T USE DECOY \n OR ELSE YOU WILL GOT BANNED!", Toast.LENGTH_LONG).show();
                    HYDRAROOT("/assets/hydramodz.so 62 0","Skill Burst ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 63 0","Skill Burst OFF");
                }
            }
        });

        aspd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = skaspd.getProgress();
                if (isChecked) {
                    try {
                        HYDRAROOT("/assets/hydramodz.so 51" + " " + arm(pos),"Attack Speed ON");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    HYDRAROOT("/assets/hydramodz.so 52 0","Attack Speed OFF");
                }
            }
        });

        cdmg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HYDRAROOT("/assets/hydramodz.so 49 0","Damage Boost ON");
                } else {
                    HYDRAROOT("/assets/hydramodz.so 50 0","Damage Boost OFF");
                }
            }
        });

        crate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = skCr.getProgress();
                if (isChecked) {
                    try {
                        HYDRAROOT("/assets/hydramodz.so 55" + " " + arm(pos),"Crit Rate ON");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    HYDRAROOT("/assets/hydramodz.so 56 0","Crit Rate OFF");
                }
            }
        });

        skdamage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        txDmg.setText(""+progress);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skaspd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        txAspd.setText(""+progress);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skCr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        txCr.setText(""+progress);
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }



    private String arm(int x) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mt = MediaType.parse("application/json; charset=utf-8");

        JsonObject postData = new JsonObject();
        postData.addProperty("asm", "MOV W0, #"+x);
        postData.addProperty("offset", "");

        RequestBody body = RequestBody.create(postData.toString(), mt);
        Request request = new Request.Builder()
                .url("https://armconverter.com/api/convert")
                .post(body)
                .build();

        try {
            Response r = client.newCall(request).execute();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(r.body().string());
                        JSONObject jObject = jsonObject.getJSONObject("hex");
                        JSONArray jArray = jObject.getJSONArray("arm64");
                        res = jArray.getString(1);
                        //Toast.makeText(context, "HEX: " + jArray.getString(1), Toast.LENGTH_LONG).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    public void killApp() {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pids = am.getRunningAppProcesses();
        int processid = 0;
        for (int i = 0; i < pids.size(); i++) {
            ActivityManager.RunningAppProcessInfo info = pids.get(i);
            if (info.processName.equalsIgnoreCase("com.asobimo.toramonline")) {
                processid = info.pid;
            }
        }
        Process.sendSignal(processid, Process.SIGNAL_KILL);
        am.killBackgroundProcesses("com.asobimo.toramonline");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    private void HYDRAROOT(String HYDRAA, String HYDRAB)
    {
        try
        {
            String HYDRAC = "/data/data/"+getPackageName()+"/files/"+HYDRAA;
            Runtime.getRuntime().exec("su -c chmod 777 "+HYDRAC);
            Runtime.getRuntime().exec("su -c ./"+HYDRAC);
            Toast.makeText(context, HYDRAB, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void TAKINROOT(String TAKIA, String TAKIB)
    {
        try
        {
            String TAKIC = "/data/data/"+getPackageName()+"/files/"+TAKIA;
            Runtime.getRuntime().exec("chmod 777 "+TAKIC);
            Runtime.getRuntime().exec(""+TAKIC);
            Toast.makeText(context, TAKIB, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 1:
                HYDRAROOT("/assets/hydramodz.so 48 0","None Element");
                break;
            case 2:
                HYDRAROOT("/assets/hydramodz.so 41 0","Fire Element");
                break;
            case 3:
                HYDRAROOT("/assets/hydramodz.so 42 0","Water Element");
                break;
            case 4:
                HYDRAROOT("/assets/hydramodz.so 43 0","Wind Element");
                break;
            case 5:
                HYDRAROOT("/assets/hydramodz.so 44 0","Earth Element");
                break;
            case 6:
                HYDRAROOT("/assets/hydramodz.so 45 0","Light Element");
                break;
            case 7:
                HYDRAROOT("/assets/hydramodz.so 46 0","Dark Element");
                break;
            case 8:
                HYDRAROOT("/assets/hydramodz.so 47 0","Neutral Element");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ele.setSelection(0);
    }
}
