package com.example.projetofinalanderson;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyBroadcastReceiver receiver;

    @SuppressLint({"StaticAttributes", "StaticFieldLeak"})
    private static TextView status_do_wifi;
    private static String status;
    private TextView porcentagemBateria;
    private TextView tempoBateria;
    private TextView saudeBateria;

    public long tempoRestante = 0L;

    private static int carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        status_do_wifi = findViewById(R.id.textView1);

        porcentagemBateria = findViewById(R.id.textView4);
        tempoBateria = findViewById(R.id.textView6);
        saudeBateria = findViewById(R.id.textView8);

        //TODO: Informar qual activity está em primeiro plano com este serviço
        Intent intent = new Intent(this,MyIntentService.class);
        intent.putExtra("TELA", "Tela 1");
        startService(intent);

        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver,filter);

        button.setOnClickListener(v-> {
            Intent in = new Intent(this, AppInstalledActivity.class);
            startActivity(in);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter (WifiManager.WIFI_STATE_CHANGED_ACTION);
        IntentFilter filter1 = new IntentFilter (Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
        registerReceiver(receiver, filter1);
//        IntentFilter filter = null;
//        try {
//            filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION, Intent.ACTION_BATTERY_CHANGED);
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        receiver = new MyBroadcastReceiver();
        IntentFilter filter2 = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        IntentFilter filter3 = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter2);
        registerReceiver(receiver, filter3);

//        IntentFilter filter = null;
//        try {
//            filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION, Intent.ACTION_BATTERY_CHANGED);
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
    }

    public boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            carregando = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

            checkBatteryChargeTime();

            float battery_percent = level * 100 / (float)scale;
            porcentagemBateria.setText(battery_percent + "%");

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_COLD) {
                saudeBateria.setText("Cold");
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD) {
                saudeBateria.setText("Dead");
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD) {
                saudeBateria.setText("Good");
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                saudeBateria.setText("OverHeated");
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                saudeBateria.setText("Overvoltage");
            }
            //String status;
            if (checkWifiOnAndConnected()) {
                status = "Habilitado";
            } else {
                status = "Desabilitado";
            }
            Log.d("APS", "O Status do Wi-Fi Mudou: " + status);
            status_do_wifi.setText(status);
        }
    }

    public void checkBatteryChargeTime() {
        BatteryManager batteryM = (BatteryManager) getApplicationContext().getSystemService(Context.BATTERY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            tempoRestante = batteryM.computeChargeTimeRemaining()/600000;
            if (carregando == 0) {
                tempoBateria.setText("Bateria do celular não está carregando" );
            } else {
                tempoBateria.setText("Faltam " + tempoRestante + " minutos para carga completa" );
            }
        }
    }

}