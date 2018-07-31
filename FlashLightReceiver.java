package sherzodbek.flashlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.widget.RemoteViews;

public class FlashLightReceiver extends BroadcastReceiver {


    static Camera camera = null;
    Camera.Parameters params;
    public static boolean isFlashLighOn;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(!action.equals(FlashLightAppWidget.ACTION_FLASH_STATE)) return;


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_light_app_widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //connectCameraService();
        //appWidgetManager.updateAppWidget(new ComponentName(context, LightWidgetProvider.class), views);
        if (isFlashLighOn) {
            offFlashLight();
            views.setImageViewResource(R.id.btnSwitcher, R.drawable.widget_light_off);
            appWidgetManager.updateAppWidget(new ComponentName(context, FlashLightAppWidget.class), views);
        } else {
            onFlashLight();
            views.setImageViewResource(R.id.btnSwitcher, R.drawable.widget_light_on);
            appWidgetManager.updateAppWidget(new ComponentName(context, FlashLightAppWidget.class), views);
        }
    }

    public void onFlashLight() {
        if(camera == null){
            camera = Camera.open();
        }
        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        isFlashLighOn = true;

    }

    public void offFlashLight() {
        if(camera == null){
            camera = Camera.open();
        }
        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashLighOn = false;
    }

    public void connectCameraService() {
        if (camera == null) {
            camera = Camera.open();
            params = camera.getParameters();
        }
    }
}