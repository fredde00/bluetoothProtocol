package com.example.bluetooth_protocol;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter btUnit = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        System.out.println(btUnit.getBondedDevices());

        BluetoothDevice hc05 = btUnit.getRemoteDevice("mac adressen för bluetoothenheten");
        System.out.println(hc05.getName());

        BluetoothSocket btSock = null;

        int cnt = 0;
        do {
            try {
                btSock = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSock);
                btSock.connect();
                System.out.println(btSock.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ++cnt;
        } while ( !btSock.isConnected() && cnt < 3 );


        try {
            OutputStream outputStream = btSock.getOutputStream();
            outputStream.write("skriv in vad arduinon ska ta emot för signal så den kan mata ut ett visst värde eller göra något ex tända en lampa. ex om streamen skickar 1 till arduinon" +
                    " och arduinon är programmerad att när den tar emot 1 kommer den att ex tända lampa 4, i denna ruta ska det stå en int");
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            inputStream = btSock.getInputStream();
            inputStream.skip(inputStream.available());

            for ("villkor hur länge mobiletelefonen ska ta emot signal från arduionon" ){
                // Följande kod nedanför är endast exempel.
                byte b = (byte) inputStream.read();
                System.out.println(b);
            }

        } catch ( IOException e) {
            e.printStackTrace();
        }


        try {
            btSock.close();
            System.out.println(btSock.isConnected());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}