package org.app.alice.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by dervis on 26/01/17.
 */
public class IniciadorServicio extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent servicio = new Intent();
        servicio.setAction("MiServicio");
        context.startService(servicio);
        context.startService(new Intent(context, NotificacionServicio.class));
    }
}
