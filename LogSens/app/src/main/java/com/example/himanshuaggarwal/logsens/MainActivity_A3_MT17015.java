package com.example.himanshuaggarwal.logsens;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.log10;

public class MainActivity_A3_MT17015 extends AppCompatActivity implements SensorEventListener, LocationListener{

    private SensorManager mSensorManager;
    private LocationManager mLocationManager;
    private TelephonyManager mTelephonyManager;
    private WifiManager mWifiManager;
    private WifiReceiver mWifiReceiver;
    private MediaRecorder mMediaRecorder;
    private long mLastUpdate;
    private TextView mAccText;
    private TextView mGyroText;
    private TextView mGpsText;
    private TextView mTowerText;
    private ListView mWifiList;
//    private TextView mWifiText;
    private TextView mDbText;

    private Database_A3_MT17015 database;

    private Button startButton;
    private Button stopButton;
    private Button saveButton;

    private static final int REQUEST_PERMISSIONS = 1;

    private StringBuilder sb;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity_A3_MT17015.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSIONS);
        }

        database = new Database_A3_MT17015(getApplicationContext());

        mAccText = (TextView) findViewById(R.id.accText);
        mGyroText = (TextView) findViewById(R.id.gyroText);
        mGpsText = (TextView) findViewById(R.id.gpsText);
        mTowerText = (TextView) findViewById(R.id.towerText);
        mWifiList = (ListView) findViewById(R.id.wifiList);
//        mWifiText = (TextView) findViewById(R.id.wifiText);
        mDbText = (TextView) findViewById(R.id.dbText);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        mWifiReceiver = new WifiReceiver();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor gyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE) ;

        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gpp";
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(outputFile);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();


        mLastUpdate = System.currentTimeMillis();

//        mGpsText.setText(mLocationManager.getLastKnownLocation());
        mWifiManager.startScan();

        //click listners

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = database.saveToCSV();

                if (success)
                    Toast.makeText(getApplicationContext(), "CSV Stored!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "CSV Not Stored!", Toast.LENGTH_LONG).show();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSensors();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unregisterSensor();
            }
        });


    }

    //saving to sqlite database
    private void saveToDatabase(){
        long now = System.currentTimeMillis();

        if(now-mLastUpdate < 2000){
            return;
        }


        String currentTime = String.valueOf(Calendar.getInstance().getTime());
        String[] acc = String.valueOf(mAccText.getText()).split(",");
        String[] gyr = String.valueOf(mGyroText.getText()).split(",");
        String[] gps = String.valueOf(mGpsText.getText()).split(",");
        String[] cellinfo = String.valueOf(mTowerText.getText()).split(",");
//        String wifi = String.valueOf(mWifiList.toString());
        String db = String.valueOf(mDbText.getText());

        StringBuilder sb = new StringBuilder();
        int n = mWifiList.getAdapter().getCount();

        for (int i = 0; i < n; i++) {
            sb.append(mWifiList.getAdapter().getItem(i).toString().split(",")[0]);
        }
        String wifi = sb.toString();

        boolean success = database.insertData(currentTime, acc[0], acc[1], acc[2], gyr[0], gyr[1], gyr[2], gps[0], gps[1], cellinfo[0], cellinfo[1], cellinfo[2], cellinfo[3], wifi, db);
//
//        if (success)
//            Toast.makeText(getApplicationContext(), "Data Stored!", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(getApplicationContext(), "Data Not Stored!", Toast.LENGTH_LONG).show();
    }

    //start sensor
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void registerSensors(){
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity_A3_MT17015.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
//        onLocationChanged(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();

        getNetworkTowerInfo();
        getDB();
    }

    private void unregisterSensor(){

        mSensorManager.unregisterListener(this);
        mLocationManager.removeUpdates(this);
        unregisterReceiver(mWifiReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccText.setText(String.valueOf(sensorEvent.values[0])+ ", " + String.valueOf(sensorEvent.values[1])+ ", " + String.valueOf(sensorEvent.values[2]));
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            mGyroText.setText(String.valueOf(sensorEvent.values[0])+ ", " + String.valueOf(sensorEvent.values[1])+ ", " + String.valueOf(sensorEvent.values[2]));
        }

        saveToDatabase();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String longitude = ", Longitude: " + location.getLongitude();
        String latitude = "Latitude " + location.getLatitude();
        mGpsText.setText(latitude+longitude);

        saveToDatabase();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    // Fuction to get network tower information
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getNetworkTowerInfo() {
        int pTypeInt = mTelephonyManager.getPhoneType();
        String phoneType = null;
        phoneType = pTypeInt == TelephonyManager.PHONE_TYPE_CDMA ? "cdma" : phoneType;
        phoneType = pTypeInt == TelephonyManager.PHONE_TYPE_GSM ? "gsm" : phoneType;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity_A3_MT17015.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);
        }

        JSONObject cell = new JSONObject();

        List<CellInfo> nCells = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nCells = mTelephonyManager.getAllCellInfo();

            try {
                CellInfo info = nCells.get(0);
                if (info instanceof CellInfoGsm) {
                    CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                    CellIdentityGsm idGsm = ((CellInfoGsm) info).getCellIdentity();
                    cell.put("CellId", idGsm.getCid());
                    cell.put("LAC", idGsm.getLac());
                    cell.put("DBM", gsm.getDbm());
                } else if (info instanceof CellInfoLte){
                    CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                    CellIdentityLte idLte = ((CellInfoLte) info).getCellIdentity();
                    cell.put("CellId", idLte.getCi());
                    cell.put("LAC", idLte.getTac());
                    cell.put("MCC", idLte.getMcc());
                    cell.put("MNC", idLte.getMnc());
                } else if (info instanceof CellInfoWcdma) {
                    CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                    CellIdentityWcdma idWcdma = ((CellInfoWcdma) info).getCellIdentity();
                    cell.put("CellId", idWcdma.getCid());
                    cell.put("LAC", idWcdma.getLac());
                    cell.put("MCC", idWcdma.getMcc());
                    cell.put("MNC", idWcdma.getMnc());
                }
            } catch (Exception e) {

            }
        }

        try {
//            mTowerText.setText("CellID:"+cell.getString("cellId")+", LAC:"+cell.getString("lac")+", MCC:"+cell.getString("mcc")+", MNC:"+cell.getString("mnc"));
            StringBuilder s = new StringBuilder();
            for (Iterator<String> it = cell.keys(); it.hasNext(); ) {
                String name = it.next();
                s.append(name+": ");
                String t = cell.getString(name);
                s.append(t+", ");
            }

            mTowerText.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveToDatabase();
    }

    //getting db values
    private void getDB(){
        double db;
        while(mMediaRecorder==null) {
        }

        db = mMediaRecorder.getMaxAmplitude()/2700.0;
        Log.v("RECORDER", Double.toString(db));
        db = 20*log10(db / 32767.0);
        Log.v("RECORDER22", Double.toString(db));
        mDbText.setText(Double.toString(db));

        saveToDatabase();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();
//        registerSensors();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onStop() {
        super.onStop();
        registerSensors();
        unregisterSensor();
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "Found access point", Toast.LENGTH_SHORT).show();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
                List<ScanResult> mResults = mWifiManager.getScanResults();
                ArrayAdapter<ScanResult> adapter = new ArrayAdapter<ScanResult>(MainActivity_A3_MT17015.this, android.R.layout.simple_list_item_1, mResults);
                mWifiList.setAdapter(adapter);
            }
        }
    }

}