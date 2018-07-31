package sherzodbek.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class FlashLightAppWidget extends AppWidgetProvider {

    public static String ACTION_FLASH_STATE = "ACTION_FLASH_STATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(ACTION_FLASH_STATE.equals(intent.getAction())){
            Toast.makeText(context, "onReceiver()", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ACTION_FLASH_STATE);
            context.sendBroadcast(i);
        }
    }

    /**
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_light_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    **/

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.i("ExampleWidget", "Update widgets "+ Arrays.asList(appWidgetIds));
        for (int i = 0; i<N; i++){
            int appWidgetID = appWidgetIds[i];
            Intent intent = new Intent(context, FlashLightReceiver.class);
            intent.setAction(ACTION_FLASH_STATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flash_light_app_widget);
            views.setOnClickPendingIntent(R.id.btnSwitcher, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetID, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

