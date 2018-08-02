package sherzodbek.flashlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    ImageButton imageButton;
    Camera camera;
    Camera.Parameters parameters;
    Boolean isFlash = false;
    Boolean isOn = false;
    String status;
    TextView textView;
    private int mProgressStatus = 0;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageButton) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.pb);

        mContext = getApplicationContext();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(mBroadcastReceiver,iFilter);
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();

                parameters = camera.getParameters();
                if(camera==null) {

                        Toast.makeText(this,
                                "The permission for camera could be disabled and should " +
                                        "be enabled from the app settings. Settings -> Apps -> [Your App] -> Permissions",
                                Toast.LENGTH_SHORT);
                    }


            isFlash = true;
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlash) {
                    if (!isOn) {
                        imageButton.setImageResource(R.drawable.off);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                        isOn = true;
                    } else {
                        imageButton.setImageResource(R.drawable.on);
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        isOn = false;
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error.....");
                    builder.setMessage("FlashLight is not available this device");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    // bir minut
    public void broadcastMessage() {
        Intent intent = new Intent();
        intent.setAction("com.example.flashlight.LightWidgetReceiver.LIGHT_STATUS");
        intent.putExtra("Status", status);
        sendBroadcast(intent);
    }

    public void connectCameraService() {
        if (camera == null) {
            camera = android.hardware.Camera.open();
            parameters = camera.getParameters();
        }
    }

    public void onFlashLight() {
        if (!isFlash) {
            status = "ON";
            if (camera == null || parameters == null) {
                return;
            }
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isFlash = true;
            imageButton.setImageResource(R.drawable.on);
            broadcastMessage();

        }
    }

    public void offFlashLight() {
        if (isFlash) {
            status = "ON";
            if (camera == null || parameters == null) {
                return;
            }
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isFlash = false;
            imageButton.setImageResource(R.drawable.off);
            broadcastMessage();


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        offFlashLight();

    }

    @Override
    protected void onPause() {
        super.onPause();
        offFlashLight();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        connectCameraService();
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);// Display the battery level in TextView
            float percentage = level/ (float) scale;
            mProgressStatus = (int)((percentage)*100);

            textView.setText("" + mProgressStatus + "%");
            progressBar.setProgress(mProgressStatus);
        }
    };

}
