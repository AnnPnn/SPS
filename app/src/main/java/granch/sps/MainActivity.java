package granch.sps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import granch.sps.graphics.CanvasView;

public class MainActivity extends AppCompatActivity implements Serializable {


    public SharedPreferences sP;
    private EditText etGreenZone;
    private EditText etYellowZone;
    private EditText etRedZone;

    private EditText etMAC1;
    private EditText etMAC2;
    private EditText etMAC3;
    private EditText etMAC4;

    private EditText etX1;
    private EditText etX2;
    private EditText etX3;
    private EditText etX4;

    private EditText etY1;
    private EditText etY2;
    private EditText etY3;
    private EditText etY4;

    @SuppressLint("StaticFieldLeak")

    private CanvasView canvasView;

    private int greenZone;
    private int yellowZone;
    private int redZone;
    private String mac1;
    private String mac2;
    private String mac3;
    private String mac4;

    private int ox1;
    private int ox2;
    private int ox3;
    private int ox4;

    private int oy1;
    private int oy2;
    private int oy3;
    private int oy4;

    boolean clickedDraw;


    //Объявляем использование ImageView

    @SuppressLint("StaticFieldLeak")
    public static ImageView ivError;

    private Thread udpServer = null;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sP = getSharedPreferences(CONST.APP_PREFERENCES, MODE_PRIVATE);

        ivError = findViewById(R.id.imagineViewError);
        ivError.setBackgroundResource(R.drawable.anim_button);

        final AnimationDrawable progressAnimation = (AnimationDrawable) ivError.getBackground();
        progressAnimation.start();


        etGreenZone = findViewById(R.id.editTextGreen);
        etYellowZone = findViewById(R.id.editTextYellow);
        etRedZone = findViewById(R.id.editTextRed);

        etMAC1 = findViewById(R.id.editTextMAC1);
        etMAC2 = findViewById(R.id.editTextMAC2);
        etMAC3 = findViewById(R.id.editTextMAC3);
        etMAC4 = findViewById(R.id.editTextMAC4);

        //Отключение возможности редактирования полей ввода
        etMAC1.setFocusable(false);
        etMAC2.setFocusable(false);
        etMAC3.setFocusable(false);
        etMAC4.setFocusable(false);

        etX1 = findViewById(R.id.editTextX1);
        etX2 = findViewById(R.id.editTextX2);
        etX3 = findViewById(R.id.editTextX3);
        etX4 = findViewById(R.id.editTextX4);

        etY1 = findViewById(R.id.editTextY1);
        etY2 = findViewById(R.id.editTextY2);
        etY3 = findViewById(R.id.editTextY3);
        etY4 = findViewById(R.id.editTextY4);

        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonStop = findViewById(R.id.buttonStop);
        CheckBox checkBoxDrawRadius = findViewById(R.id.checkBox);

        canvasView = findViewById(R.id.canView);
        buttonSave.setOnClickListener(v ->
        {
            updatelSettings();
        });

        buttonStart.setOnClickListener(v ->
        {
            if (udpServer != null)
                return;

            Context context = getApplicationContext();
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String ip = null;
            try {
                ip = InetAddress.getByAddress(
                        ByteBuffer
                                .allocate(Integer.BYTES)
                                .order(ByteOrder.LITTLE_ENDIAN)
                                .putInt(wm.getConnectionInfo().getIpAddress())
                                .array()

                ).getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();

            }
            System.out.println(ip);

            udpServer = new Thread(new UdpServer(canvasView));
            udpServer.setName("UDP Thread");
            udpServer.start();
            Log.v(this.getClass().getSimpleName(), "onClick: Starting service.");
            buttonStop.setVisibility(View.VISIBLE);
            buttonStart.setVisibility(View.GONE);
        });


        buttonStop.setOnClickListener(v ->
        {

            udpServer.interrupt();
            udpServer = null;
            Log.v(this.getClass().getSimpleName(), "onClick: Stopping service.");
            buttonStart.setVisibility(View.VISIBLE);
            buttonStop.setVisibility(View.GONE);
        });


        checkBoxDrawRadius.setOnClickListener(v ->
        {
            if (checkBoxDrawRadius.isChecked())
                clickedDraw = true;
            else {
                clickedDraw = false;
            }
        });


        LinearLayout configPanel = findViewById(R.id.configPanel);
        findViewById(R.id.buttonConfig).setOnClickListener(v -> {
            if (configPanel.getVisibility() == View.VISIBLE) {
                configPanel.setVisibility(View.GONE);
            } else {
                configPanel.setVisibility(View.VISIBLE);
                fillSettingsForm();

            }

        });

        loadConfig();

        canvasView.setZones(greenZone, yellowZone, redZone, mac1, mac2, mac3, mac4, ox1, oy1, ox2, oy2, ox3, oy3, ox4, oy4, clickedDraw);


    }


    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void updatelSettings() {
        try {
            yellowZone = Integer.parseInt(etYellowZone.getText().toString());
            greenZone = Integer.parseInt(etGreenZone.getText().toString());
            redZone = Integer.parseInt(etRedZone.getText().toString());
            ox1 = Integer.parseInt(etX1.getText().toString());
            ox2 = Integer.parseInt(etX2.getText().toString());
            ox3 = Integer.parseInt(etX3.getText().toString());
            ox4 = Integer.parseInt(etX4.getText().toString());
            oy1 = Integer.parseInt(etY1.getText().toString());
            oy2 = Integer.parseInt(etY2.getText().toString());
            oy3 = Integer.parseInt(etY3.getText().toString());
            oy4 = Integer.parseInt(etY4.getText().toString());
        } catch (Exception ex) {
            Toast.makeText(this, "Значение указано неверно", Toast.LENGTH_SHORT).show();
            return;
        }
        canvasView.setZones(greenZone, yellowZone, redZone, mac1, mac2, mac3, mac4, ox1, oy1, ox2, oy2, ox3, oy3, ox4, oy4, clickedDraw);
        saveConfig();
        findViewById(R.id.configPanel).setVisibility(View.GONE);
    }

    private void fillSettingsForm() {
        etGreenZone.setText(String.valueOf(greenZone));
        etYellowZone.setText(String.valueOf(yellowZone));
        etRedZone.setText(String.valueOf(redZone));
        etMAC1.setText(String.valueOf(mac1));
        etMAC2.setText(String.valueOf(mac2));
        etMAC3.setText(String.valueOf(mac3));
        etMAC4.setText(String.valueOf(mac4));
        etX1.setText(String.valueOf(ox1));
        etX2.setText(String.valueOf(ox2));
        etX3.setText(String.valueOf(ox3));
        etX4.setText(String.valueOf(ox4));
        etY1.setText(String.valueOf(oy1));
        etY2.setText(String.valueOf(oy2));
        etY3.setText(String.valueOf(oy3));
        etY4.setText(String.valueOf(oy4));
    }

    private void loadConfig() {
        greenZone = sP.getInt("greenZone_m", 9);
        yellowZone = sP.getInt("yellowZone_m", 5);
        redZone = sP.getInt("redZone_m", 2);
        mac1 = sP.getString("MAC1", "[00, 5B, 50]");
        mac2 = sP.getString("MAC2", "[00, 5B, 4E]");
        mac3 = sP.getString("MAC3", "[00, 5B, 51]");
        mac4 = sP.getString("MAC4", "[00, 5B, 52]");
        ox1 = sP.getInt("ox1", 100);
        ox2 = sP.getInt("ox2", 100);
        ox3 = sP.getInt("ox3", 100);
        ox4 = sP.getInt("ox4", 100);
        oy1 = sP.getInt("oy1", 100);
        oy2 = sP.getInt("oy2", 100);
        oy3 = sP.getInt("oy3", 100);
        oy4 = sP.getInt("oy4", 100);
        clickedDraw = sP.getBoolean("clickedDraw", false);
    }

    private void saveConfig() {
        SharedPreferences.Editor ed = sP.edit();
        ed.putInt("greenZone_m", greenZone);
        ed.putInt("yellowZone_m", yellowZone);
        ed.putInt("redZone_m", redZone);
        ed.putString("MAC1", mac1);
        ed.putString("MAC2", mac2);
        ed.putString("MAC3", mac3);
        ed.putString("MAC4", mac4);
        ed.putInt("ox1", ox1);
        ed.putInt("ox2", ox2);
        ed.putInt("ox3", ox3);
        ed.putInt("ox4", ox4);
        ed.putInt("oy1", oy1);
        ed.putInt("oy2", oy2);
        ed.putInt("oy3", oy3);
        ed.putInt("oy4", oy4);
        ed.putBoolean("clickedDraw", clickedDraw);

        ed.apply();
    }
}
