package org.app.alice.service;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.app.alice.R;
import org.app.alice.activity.VerCitaActivity;
import org.app.alice.activity.VerEspecialistaActivity;
import org.app.alice.activity.VerEventoActivity;
import org.app.alice.activity.VerEventualidadActivity;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.CategoriaServicioDao;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.Horario;
import org.app.alice.modelo.HorarioDao;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.TipoServicioDao;
import org.app.alice.modelo.Turno;
import org.app.alice.modelo.TurnoDao;
import org.app.alice.modelo.Usuario;
import org.app.alice.modelo.UsuarioDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dervis on 26/01/17.
 */
public class NotificacionServicio extends Service {
    Context context;
    int control = 0;
    int a=0;
    GlobalService globals = GlobalService.getInstance();
    List<Alerta> alertas;
    int x=0;

    public NotificacionServicio(){
        super();
    }

    public NotificacionServicio(Context c){
        super();
        context=c;
        mensajeNotificacion();
    }

    public void mensajeNotificacion(){

        x++;
        DAOApp daoApp=new DAOApp();
        UsuarioDao usuarioDao=daoApp.getUsuarioDao();
        List<Usuario> usuarios =usuarioDao.loadAll();
        if(usuarios.size()>0){
            VerificarConexionService verificar=new VerificarConexionService(this);
            if(verificar.estaConectado()){
                control=2;
                //Toast.makeText(context, "No hay notificaciones "+String.valueOf(x), Toast.LENGTH_SHORT).show();

                new MiTareaGet(globals.getServer() + "notificaciones.json?usuario_id="+String.valueOf(usuarios.get(0).getId()), "").execute();
            }else{
                //CargarDatosPrueba cargarDatosPrueba=new CargarDatosPrueba();
                //cargarDatosPrueba.CargarNotificaciones();
                enviarMensajeAlerta1();
            }
        }
        new MiTarea(60).execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        mensajeNotificacion();
    }

    @Override
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);

    }
    private class MiTarea extends AsyncTask<String, Float, String> {
        private int x;

        public MiTarea(int x){
            this.x=x;
        }
        protected void onPreExecute() {

        }
        protected String doInBackground(String... urls) {
            String responce="";
            try
            {
                int i=0;
                while(i<x){
                    Thread.sleep(1000);
                    i++;
                }
            }catch(InterruptedException e){}
            return responce;
        }
        protected void onProgressUpdate (Float... valores) {

        }
        protected void onPostExecute(String tiraJson) {

            mensajeNotificacion();

        }
    }


    public void cargarAlertas(String datos) {
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<Alerta> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        Alerta item=new Alerta();

                        if (!(objO.get("id").isJsonNull())) {
                            DAOApp daoApp=new DAOApp();
                            AlertaDao alertaDao=daoApp.getAlertaDao();
                            if(alertaDao.load(objO.get("id").getAsLong())==null){
                                //Toast.makeText(NotificacionServicio.this, "Prueba null", Toast.LENGTH_SHORT).show();
                                item.setId(objO.get("id").getAsLong());
                                if (!(objO.get("mensaje").isJsonNull())) {
                                    item.setMensaje(objO.get("mensaje").getAsString());
                                }if (!(objO.get("descripcion").isJsonNull())) {
                                    item.setDescripcion(objO.get("descripcion").getAsString());
                                }if (!(objO.get("estatus").isJsonNull())) {
                                    item.setEstatus(objO.get("estatus").getAsInt());
                                }if (!(objO.get("tipo_notificacion_id").isJsonNull())) {
                                    item.setTipo_notificacion_id(objO.get("tipo_notificacion_id").getAsInt());
                                }if (!(objO.get("url").isJsonNull())) {
                                    item.setUrl(objO.get("url").getAsString());
                                }if (!(objO.get("created_at").isJsonNull())) {
                                    item.setFecha(globals.fechaFormat(objO.get("created_at").getAsString()));
                                }if (!(objO.get("usuario_id").isJsonNull())) {
                                    item.setIdUsuarioAlerta(objO.get("usuario_id").getAsLong());
                                }if (!(objO.get("entidad_id").isJsonNull())) {
                                    item.setEntidad_id(objO.get("entidad_id").getAsLong());
                                }
                                lista.add(item);
                                AlertaDao dao=daoApp.getAlertaDao();
                                dao.insert(item);
                            }

                            }
                    }

                        /*DAOApp daoApp=new DAOApp();
                        AlertaDao dao=daoApp.getAlertaDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);*/
                        //showAlertDialog(this,"Alerta",String.valueOf(dao.count()),true);


                }
            }catch (Exception e){
                //showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            AlertaDao dao=daoApp.getAlertaDao();
            dao.deleteAll();
        }
        enviarMensajeAlerta1();

    }

    public void enviarMensajeAlerta1(){
        int a=0;
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        alertas=alertaDao.queryBuilder().where(AlertaDao.Properties.Estatus.in(1),AlertaDao.Properties.Tipo_notificacion_id.in(3)).list();
        //Toast.makeText(NotificacionServicio.this, String.valueOf(alertas.size()), Toast.LENGTH_SHORT).show();
        if (alertas.size()>0){
            control=3;
            //new MiTareaGet(globals.getServer() + "eventos.json", "").execute();
            alerta1();
        }else{
            enviarMensajeAlerta2();

        }

    }

    public void alerta1(){
        for (int x=0;x<alertas.size();x++){
            Alerta item=alertas.get(x);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable)getResources()
                                    .getDrawable(R.drawable.alice_mini)).getBitmap()))
                            .setContentTitle(item.getDescripcion())
                            .setContentText(item.getMensaje())
                            .setContentInfo(String.valueOf(a++))
                            .setTicker("Alerta!");
            Intent notIntent = new Intent(context, VerEventoActivity.class);
            notIntent.putExtra("id",item.getEntidad_id());
            notIntent.putExtra("alerta",item.getId());
            PendingIntent contIntent = PendingIntent.getActivity(context, item.getId().intValue(), notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(item.getId().intValue(), mBuilder.build());
            DAOApp daoApp=new DAOApp();
            AlertaDao alertaDao=daoApp.getAlertaDao();
            item.setEstatus(3);
            alertaDao.update(item);

        }
        enviarMensajeAlerta2();
    }

    public void enviarMensajeAlerta2(){
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        alertas=alertaDao.queryBuilder().where(AlertaDao.Properties.Estatus.in(1),AlertaDao.Properties.Tipo_notificacion_id.in(2)).list();
        if (alertas.size()>0){
            control=4;
            //new MiTareaGet(globals.getServer() + "categorias.json", "").execute();
            alerta2();
        }else{
            enviarMensajeAlerta3();
        }

    }

    public void alerta2(){
        for (int x=0;x<alertas.size();x++){
            Alerta item=alertas.get(x);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable)getResources()
                                    .getDrawable(R.drawable.alice_mini)).getBitmap()))
                            .setContentTitle(item.getDescripcion())
                            .setContentText(item.getMensaje())
                            .setContentInfo(String.valueOf(a++))
                            .setTicker("Alerta!");
            Intent notIntent = new Intent(context, VerEspecialistaActivity.class);
            //Intent notIntent = new Intent(context, VerEventoActivity.class);
            notIntent.putExtra("id",item.getEntidad_id());
            notIntent.putExtra("alerta",item.getId());
            PendingIntent contIntent = PendingIntent.getActivity(context, item.getId().intValue(), notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(item.getId().intValue(), mBuilder.build());
            DAOApp daoApp=new DAOApp();
            AlertaDao alertaDao=daoApp.getAlertaDao();
            item.setEstatus(3);
            alertaDao.update(item);
        }
        enviarMensajeAlerta3();
    }

    public void enviarMensajeAlerta3(){
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        alertas=alertaDao.queryBuilder().where(AlertaDao.Properties.Estatus.in(1),AlertaDao.Properties.Tipo_notificacion_id.in(1)).list();
        //new MiTareaGet(globals.getServer()+"citas/1.json","").execute();
        if (alertas.size()>0){
            control=5;
            alerta3();
        }else{

        }

    }

    public void alerta3(){
        for (int x=0;x<alertas.size();x++){
            Alerta item=alertas.get(x);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable)getResources()
                                    .getDrawable(R.drawable.alice_mini)).getBitmap()))
                            .setContentTitle(item.getDescripcion())
                            .setContentText(item.getMensaje())
                            .setContentInfo(String.valueOf(a++))
                            .setTicker("Alerta!");
            Intent notIntent = new Intent(context, VerCitaActivity.class);
            //Intent notIntent = new Intent(context, VerEventoActivity.class);
            notIntent.putExtra("id",item.getEntidad_id());
            notIntent.putExtra("alerta",item.getId());
            PendingIntent contIntent = PendingIntent.getActivity(context, item.getId().intValue(), notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(item.getId().intValue(), mBuilder.build());
            DAOApp daoApp=new DAOApp();
            AlertaDao alertaDao=daoApp.getAlertaDao();
            item.setEstatus(3);
            alertaDao.update(item);
        }
        enviarMensajeAlerta4();

    }

    public void enviarMensajeAlerta4(){
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        alertas=alertaDao.queryBuilder().where(AlertaDao.Properties.Estatus.in(4),AlertaDao.Properties.Tipo_notificacion_id.in(1)).list();
        //new MiTareaGet(globals.getServer()+"citas/1.json","").execute();
        if (alertas.size()>0){
            control=5;
            alerta4();
        }else{

        }

    }

    public void alerta4(){
        for (int x=0;x<alertas.size();x++){
            Alerta item=alertas.get(x);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.stat_sys_warning)
                            .setLargeIcon((((BitmapDrawable)getResources()
                                    .getDrawable(R.drawable.alice_mini)).getBitmap()))
                            .setContentTitle(item.getDescripcion())
                            .setContentText(item.getMensaje())
                            .setContentInfo(String.valueOf(a++))
                            .setTicker("Alerta!");
            Intent notIntent = new Intent(context, VerEventualidadActivity.class);
            //Intent notIntent = new Intent(context, VerEventoActivity.class);
            notIntent.putExtra("id",item.getEntidad_id());
            notIntent.putExtra("alerta",item.getId());
            PendingIntent contIntent = PendingIntent.getActivity(context, item.getId().intValue(), notIntent, 0);
            mBuilder.setContentIntent(contIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(item.getId().intValue(), mBuilder.build());
            DAOApp daoApp=new DAOApp();
            AlertaDao alertaDao=daoApp.getAlertaDao();
            item.setEstatus(3);
            alertaDao.update(item);
        }
    }

    public void cargarEventos(String datos){
        //showAlertDialog(this,"Eventos",datos,true);
        Toast.makeText(context, "--No hay notificaciones "+String.valueOf(x), Toast.LENGTH_SHORT).show();
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<Evento> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {

                        JsonObject objO = array.get(x).getAsJsonObject();
                        Evento item=new Evento();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setCodigo(objO.get("id").getAsInt());
                        }
                        item.setTitulo("Titulo");
                        if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }if (!(objO.get("contenido").isJsonNull())) {
                            item.setContenido(objO.get("contenido").getAsString());
                        }if (!(objO.get("created_at").isJsonNull())) {
                            item.setFecha(objO.get("created_at").getAsString());
                        }if (!(objO.get("tipo_evento").isJsonNull())) {
                            JsonObject obj1 = objO.get("tipo_evento").getAsJsonObject();
                            if (!(obj1.get("descripcion").isJsonNull())) {
                                item.setTipoEvento(obj1.get("descripcion").getAsString());
                            }
                        }if (!(objO.get("ubicacion").isJsonNull())) {
                            JsonObject obj1 = objO.get("ubicacion").getAsJsonObject();
                            if (!(obj1.get("descripcion").isJsonNull())) {
                                item.setLugar(obj1.get("descripcion").getAsString());
                            }
                        }if (!(objO.get("tipo_servicios").isJsonNull())) {
                            JsonArray array1 = objO.get("tipo_servicios").getAsJsonArray();
                            if (!array1.isJsonNull()) {
                                String tipo="";
                                for (int i=0;i<array1.size(); i++){
                                    JsonObject obj2 = array1.get(i).getAsJsonObject();
                                    if (!(obj2.get("descripcion").isJsonNull())) {
                                        tipo=tipo+obj2.get("descripcion").getAsString()+"\n";
                                    }
                                }
                                item.setTipoServicio(tipo);
                            }
                        }
                        lista.add(item);
                    }
                    Toast.makeText(context, "1. lista "+String.valueOf(lista.size()), Toast.LENGTH_SHORT).show();
                    if(lista.size()>0){
                        DAOApp daoApp=new DAOApp();
                        EventoDao dao=daoApp.getEventoDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        alerta1();
                        //showAlertDialog(this,"EVENTO",String.valueOf(dao.count()),true);

                    }
                }
            }catch (Exception e){
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }
        enviarMensajeAlerta2();
    }

    public void cargarServicios(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<CategoriaServicio> lista=new ArrayList<>();
                    List<TipoServicio> items2=new ArrayList<>();
                    List<Servicio> items3=new ArrayList<>();
                    List<Horario> items4=new ArrayList<>();
                    List<Turno> items5=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        CategoriaServicio item=new CategoriaServicio();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }if (!(objO.get("tipo_servicios").isJsonNull())) {
                            JsonArray array1 = objO.get("tipo_servicios").getAsJsonArray();
                            if (!array1.isJsonNull()) {

                                for (int i=0;i<array1.size(); i++){
                                    JsonObject obj2 = array1.get(i).getAsJsonObject();
                                    TipoServicio item2=new TipoServicio();
                                    if (!(obj2.get("id").isJsonNull())) {
                                        item2.setId(obj2.get("id").getAsLong());
                                    }if (!(obj2.get("descripcion").isJsonNull())) {
                                        item2.setDescripcion(obj2.get("descripcion").getAsString());
                                    }if (!(obj2.get("texto").isJsonNull())) {
                                        item2.setTexto(obj2.get("texto").getAsString());
                                    }if (!(obj2.get("estatus").isJsonNull())) {
                                        item2.setEstatus(obj2.get("estatus").getAsInt());
                                    }if (!(obj2.get("servicios").isJsonNull())) {
                                        JsonArray array2 = obj2.get("servicios").getAsJsonArray();
                                        if (!array2.isJsonNull()) {

                                            for (int y=0;y<array2.size(); y++){
                                                JsonObject obj3 = array2.get(y).getAsJsonObject();
                                                Servicio item3=new Servicio();
                                                if (!(obj3.get("id").isJsonNull())) {
                                                    item3.setId(obj3.get("id").getAsLong());
                                                }if (!(obj3.get("descripcion").isJsonNull())) {
                                                    item3.setDescripcion(obj3.get("descripcion").getAsString());
                                                }if (!(obj3.get("precio").isJsonNull())) {
                                                    item3.setPrecio(obj3.get("precio").getAsDouble());
                                                }if (!(obj3.get("estatus").isJsonNull())) {
                                                    item3.setEstatus(obj3.get("estatus").getAsInt());
                                                }if (!(obj3.get("especialista").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("especialista").getAsJsonObject();
                                                    if (!(obj4.get("persona").isJsonNull())) {
                                                        JsonObject obj5 = obj4.get("persona").getAsJsonObject();
                                                        String persona="";
                                                        if (!(obj5.get("nombre").isJsonNull())) {
                                                            persona=obj5.get("nombre").getAsString()+" ";
                                                        }if (!(obj5.get("apellido").isJsonNull())) {
                                                            persona="- "+persona+obj5.get("apellido").getAsString()+" ";
                                                        }
                                                        item3.setPersona(persona);
                                                    }
                                                }if (!(obj3.get("ubicacion").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("ubicacion").getAsJsonObject();
                                                    if (!(obj4.get("descripcion").isJsonNull())) {
                                                        item3.setUbicacion(obj4.get("descripcion").getAsString());
                                                    }
                                                }

                                                if (!(obj3.get("horarios").isJsonNull())) {
                                                    JsonArray array3= obj3.get("horarios").getAsJsonArray();
                                                    if (!array3.isJsonNull()) {

                                                        for (int j=0;j<array3.size(); j++){
                                                            JsonObject obj4 = array3.get(j).getAsJsonObject();
                                                            Horario item4=new Horario();
                                                            if (!(obj4.get("id").isJsonNull())) {
                                                                item4.setId(obj4.get("id").getAsLong());
                                                            }if (!(obj4.get("servicio_id").isJsonNull())) {
                                                                item4.setIdServicioHorario(obj4.get("servicio_id").getAsLong());
                                                            }if (!(obj4.get("estatus").isJsonNull())) {
                                                                item4.setEstatus(obj4.get("estatus").getAsInt());
                                                            }

                                                            if (!(obj4.get("turnos").isJsonNull())) {
                                                                JsonArray array4= obj4.get("turnos").getAsJsonArray();
                                                                if (!array4.isJsonNull()) {

                                                                    for (int l=0;l<array4.size(); l++){
                                                                        JsonObject obj5 = array4.get(l).getAsJsonObject();
                                                                        Turno item5=new Turno();
                                                                        if (!(obj5.get("id").isJsonNull())) {
                                                                            item5.setId(obj5.get("id").getAsLong());
                                                                        }if (!(obj5.get("horario_id").isJsonNull())) {
                                                                            item5.setIdHorarioTurno(obj5.get("horario_id").getAsLong());
                                                                        }if (!(obj5.get("estatus").isJsonNull())) {
                                                                            item5.setEstatus(obj5.get("estatus").getAsInt());
                                                                        }if (!(obj5.get("hora_inicio").isJsonNull())) {
                                                                            item5.setInicio(obj5.get("hora_inicio").getAsString());
                                                                        }if (!(obj5.get("hora_fin").isJsonNull())) {
                                                                            item5.setFin(obj5.get("hora_fin").getAsString());
                                                                        }if (!(obj5.get("tipo_turno_id").isJsonNull())) {
                                                                            item5.setTipo(obj5.get("tipo_turno_id").getAsInt());
                                                                        }if (!(obj5.get("dia_id").isJsonNull())) {
                                                                            item5.setDia(obj5.get("dia_id").getAsInt());
                                                                        }
                                                                        items5.add(item5);
                                                                    }

                                                                }
                                                            }

                                                            items4.add(item4);
                                                        }

                                                    }
                                                }

                                                item3.setIdTipoServicio(obj2.get("id").getAsLong());
                                                items3.add(item3);
                                            }

                                        }
                                    }
                                    item2.setIdCategoriaServicio(objO.get("id").getAsLong());
                                    items2.add(item2);
                                }
                            }
                        }
                        lista.add(item);
                    }
                    if(lista.size()>0){
                        DAOApp daoApp=new DAOApp();
                        CategoriaServicioDao dao=daoApp.getCategoriaServicioDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this,"Categoria",String.valueOf(dao.count()),true);

                    }
                    if(items2.size()>0){
                        DAOApp daoApp=new DAOApp();
                        TipoServicioDao dao=daoApp.getTipoServicioDao();
                        dao.deleteAll();
                        dao.insertInTx(items2);
                        //showAlertDialog(this,"tipo servicios",String.valueOf(dao.count()),true);

                    }if(items3.size()>0){
                        DAOApp daoApp=new DAOApp();
                        ServicioDao dao=daoApp.getServicioDao();
                        dao.deleteAll();
                        dao.insertInTx(items3);
                        //showAlertDialog(this,"servicios",String.valueOf(dao.count()),true);

                    }if(items4.size()>0){
                        DAOApp daoApp=new DAOApp();
                        HorarioDao dao=daoApp.getHorarioDao();
                        dao.deleteAll();
                        dao.insertInTx(items4);
                        //showAlertDialog(this,"Horarios",String.valueOf(dao.count()),true);

                    }if(items5.size()>0){
                        DAOApp daoApp=new DAOApp();
                        TurnoDao dao=daoApp.getTurnoDao();
                        dao.deleteAll();
                        dao.insertInTx(items5);
                        //showAlertDialog(this,"Turnos",String.valueOf(dao.count()),true);

                    }
                    alerta2();
                }
            }catch (Exception e){
                //showAlertDialog(this, "Error", e.toString(),false);
            }
        }
        enviarMensajeAlerta3();
    }

    public void cargarCitas(String datos){
        datos = "["+datos+"]";
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<Cita> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        Cita item=new Cita();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }if (!(objO.get("fecha").isJsonNull())) {
                            item.setFecha(objO.get("fecha").getAsString());
                        }if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }if (!(objO.get("turno").isJsonNull())) {
                            JsonObject obj1 = objO.get("turno").getAsJsonObject();
                            if (!(obj1.get("horario").isJsonNull())) {
                                JsonObject obj2 = obj1.get("horario").getAsJsonObject();
                                if (!(obj2.get("servicio").isJsonNull())) {
                                    JsonObject obj3 = obj2.get("servicio").getAsJsonObject();
                                    if (!(obj3.get("descripcion").isJsonNull())) {
                                        item.setDescripcion(obj3.get("descripcion").getAsString());
                                    }
                                    if (!(obj3.get("precio").isJsonNull())) {
                                        item.setPrecio(obj3.get("precio").getAsDouble());
                                    }
                                    if (!(obj3.get("tipo_servicio_id").isJsonNull())) {
                                        item.setIdServicioCita(obj3.get("tipo_servicio_id").getAsLong());
                                    }if (!(obj3.get("ubicacion").isJsonNull())) {
                                        JsonObject obj4 = obj3.get("ubicacion").getAsJsonObject();
                                        if (!(obj4.get("descripcion").isJsonNull())) {
                                            item.setUbicacion(obj4.get("descripcion").getAsString());
                                        }
                                    }
                                }
                            }
                        }
                        lista.add(item);
                    }
                    if(lista.size()>0){
                        DAOApp daoApp=new DAOApp();
                        CitaDao dao=daoApp.getCitaDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this,"Cita",String.valueOf(dao.count()),true);
                        if (alertas.size()>0){
                            control=5;
                            alerta3();
                        }else{

                        }

                    }
                }
            }catch (Exception e){
                //showAlertDialog(this, "citassss", e.toString(),false);
            }
        }
    }


    /*public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }*/

    //Clase para consumir servicio por get
    private class MiTareaGet extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTareaGet(String url, String x) {
            Log.d("url", url);
            this.ur = url + x;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            String responce = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet(ur);
            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                responce = EntityUtils.toString(resp.getEntity());
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
            }
            return responce;
        }

        @Override
        protected void onProgressUpdate(Float... valores) {

        }

        @Override//Acción a realizar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {
            switch (control) {
                case 2:
                    cargarAlertas(tiraJson);
                    break;
                case 3:
                    cargarEventos(tiraJson);
                    break;
                case 4:
                    cargarServicios(tiraJson);
                    break;
                case 5:
                    cargarCitas(tiraJson);
                    break;

            }
        }
    }
}