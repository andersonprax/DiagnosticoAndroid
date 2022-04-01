package com.example.projetofinalanderson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AppInstalledActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_installed);

        PackageManager packageManager = getPackageManager();
        @SuppressLint("ListApp")
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        Intent intent = new Intent(this,MyIntentService.class);
        intent.putExtra("TELA", "Tela 2");
        startService(intent);

        List<String> values = new ArrayList<>(0);

        for(ApplicationInfo ap:list){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ap.enabled){
                    values.add(ap.packageName + " - Habilitado");

                } else {
                    values.add(ap.packageName + " - Desabilitado");
                }
            }
        }
        ListView lista = findViewById(R.id.lista);
        lista.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values));
    }

}
