package com.santosh.sensor_service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView x;
    private TextView y;
    private TextView z;
    private TextView connectionStatus;
    private Button connectBtn;

    private ISensorAidlInterface sensorAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x = findViewById(R.id.xVal);
        y = findViewById(R.id.yVal);
        z = findViewById(R.id.zVal);
        connectionStatus = findViewById(R.id.connectionStatus);
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(view->{
            bindToSensorService();
        });

    }

    public void bindToSensorService(){
        Intent intent = new Intent("com.santosh.sensor_service.CONNECT_SENSOR_SERVICE");
        Intent explicitIntent = getExplicitIntent(intent);
        if (explicitIntent != null) {
            bindService(explicitIntent, connection, Context.BIND_AUTO_CREATE);
            connectionStatus.setText("Connected");
        } else {
            Toast.makeText(this, "Could not find the service!", Toast.LENGTH_SHORT).show();
        }
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            sensorAidlInterface = ISensorAidlInterface.Stub.asInterface(iBinder);
            try {
                sensorAidlInterface.setListener(new ISensorDataListener.Stub() {
                    @Override
                    public void onSensorDataChanged(float x, float y, float z) throws RemoteException {
                        MainActivity.this.x.setText(String.valueOf(x));
                        MainActivity.this.y.setText(String.valueOf(y));
                        MainActivity.this.z.setText(String.valueOf(z));
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connectionStatus.setText("Disconnected");
        }
    };

    private Intent getExplicitIntent(Intent implicitIntent){
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentServices(implicitIntent, 0);
        if (resolveInfos != null && resolveInfos.size() > 0){
            ResolveInfo resolveInfo = resolveInfos.get(0);
            ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName,
                    resolveInfo.serviceInfo.name);
            Intent intent = new Intent(implicitIntent);
            intent.setComponent(componentName);
            return intent;
        }
        return null;
    }
}