package org.app.alice.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.app.alice.R;
import org.app.alice.adapter.CatalogoServicioAdapter;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.CategoriaServicioDao;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.Criterio;
import org.app.alice.modelo.CriterioDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.Horario;
import org.app.alice.modelo.HorarioDao;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;
import org.app.alice.modelo.Pregunta;
import org.app.alice.modelo.PreguntaDao;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;
import org.app.alice.modelo.TipoOpinio;
import org.app.alice.modelo.TipoOpinioDao;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.TipoServicioDao;
import org.app.alice.modelo.Turno;
import org.app.alice.modelo.TurnoDao;
import org.app.alice.modelo.Usuario;
import org.app.alice.modelo.UsuarioDao;
import org.app.alice.service.CargarDatosPrueba;
import org.app.alice.service.GlobalService;
import org.app.alice.service.VerificarConexionService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brayan on 08/03/17.
 */

public class InicioActivity extends AppCompatActivity {
    int control;
    GlobalService globals = GlobalService.getInstance();
    ProgressBar progressBarInicio;
    VerificarConexionService verificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        getSupportActionBar().hide();
        progressBarInicio=(ProgressBar)findViewById(R.id.progressbarInicio);
        globals.setServer("http://192.168.0.115:3000/");
        verificar=new VerificarConexionService(this);
        if(verificar.estaConectado()){
            progressBarInicio.setVisibility(View.VISIBLE);
            control=1;
            new MiTareaGet(globals.getServer()+"eventos.json","").execute();
        }else{
            /*CargarDatosPrueba cargarDatosPrueba=new CargarDatosPrueba();
            cargarDatosPrueba.CargarEvento();
            cargarDatosPrueba.CargarNoticia();*/
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void cargarEventos(String datos){
        //showAlertDialog(this,"Eventos",datos,true);
        if(datos.length()>2) {
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
                        }if (!(objO.get("foto_url").isJsonNull())) {
                            item.setFoto(objO.get("foto_url").getAsString());
                        }if (!(objO.get("created_at").isJsonNull())) {
                            item.setFecha(globals.fechaFormat(objO.get("created_at").getAsString()));
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
                        DAOApp daoApp=new DAOApp();
                        EventoDao dao=daoApp.getEventoDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this,"EVENTO",String.valueOf(dao.count()),true);
                }
            }catch (Exception e){
                showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            EventoDao dao=daoApp.getEventoDao();
            dao.deleteAll();
        }
        control=2;
        new MiTareaGet(globals.getServer()+"noticias.json","").execute();
    }

    public void cargarNoticias(String datos){
        //showAlertDialog(this,"Noticias",datos,true);
        if(datos.length()>2) {
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<Noticia> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        Noticia item=new Noticia();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }if (!(objO.get("titulo").isJsonNull())) {
                            item.setTitulo(objO.get("titulo").getAsString());
                        }if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }if (!(objO.get("contenido").isJsonNull())) {
                            item.setContenido(objO.get("contenido").getAsString());
                        }if (!(objO.get("created_at").isJsonNull())) {
                            item.setFecha(globals.fechaFormat(objO.get("created_at").getAsString()));
                        }if (!(objO.get("tipo_noticia").isJsonNull())) {
                            JsonObject obj1 = objO.get("tipo_noticia").getAsJsonObject();
                            if (!(obj1.get("descripcion").isJsonNull())) {
                                item.setTipoNoticia(obj1.get("descripcion").getAsString());
                            }
                        }
                        lista.add(item);
                    }
                        DAOApp daoApp=new DAOApp();
                        NoticiaDao dao=daoApp.getNoticiaDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this,"noticias",String.valueOf(dao.count()),true);

                }
            }catch (Exception e){
                showAlertDialog(this, "Error", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            NoticiaDao dao=daoApp.getNoticiaDao();
            dao.deleteAll();
        }

        control=3;
        new MiTareaGet(globals.getServer()+"categorias.json","").execute();

    }

    public void cargarServicios(String datos){
        //showAlertDialog(this,"Servicios",datos,true);
        if(datos.length()>2) {
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array = (JsonArray) obje;
                if (!array.isJsonNull()) {
                    List<CategoriaServicio> lista = new ArrayList<>();
                    List<TipoServicio> items2 = new ArrayList<>();
                    List<Servicio> items3 = new ArrayList<>();
                    List<Horario> items4 = new ArrayList<>();
                    List<Turno> items5 = new ArrayList<>();
                    List<Criterio> items6 = new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        CategoriaServicio item = new CategoriaServicio();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }
                        if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }
                        if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }
                        if (!(objO.get("tipo_servicios").isJsonNull())) {
                            JsonArray array1 = objO.get("tipo_servicios").getAsJsonArray();
                            if (!array1.isJsonNull()) {

                                for (int i = 0; i < array1.size(); i++) {
                                    JsonObject obj2 = array1.get(i).getAsJsonObject();
                                    TipoServicio item2 = new TipoServicio();
                                    if (!(obj2.get("id").isJsonNull())) {
                                        item2.setId(obj2.get("id").getAsLong());
                                    }
                                    if (!(obj2.get("descripcion").isJsonNull())) {
                                        item2.setDescripcion(obj2.get("descripcion").getAsString());
                                    }
                                    if (!(obj2.get("texto").isJsonNull())) {
                                        item2.setTexto(obj2.get("texto").getAsString());
                                    }
                                    if (!(obj2.get("estatus").isJsonNull())) {
                                        item2.setEstatus(obj2.get("estatus").getAsInt());
                                    }
                                    if (!(obj2.get("criterios").isJsonNull())) {
                                        JsonArray array2 = obj2.get("criterios").getAsJsonArray();
                                        if (!array2.isJsonNull()) {
                                            for (int y = 0; y < array2.size(); y++) {
                                                JsonObject obj3 = array2.get(y).getAsJsonObject();
                                                Criterio item3 = new Criterio();
                                                if (!(obj3.get("id").isJsonNull())) {
                                                    item3.setCodigo(obj3.get("id").getAsLong());
                                                }
                                                if (!(obj3.get("descripcion").isJsonNull())) {
                                                    item3.setDescripcion(obj3.get("descripcion").getAsString());
                                                }
                                                if (!(obj3.get("estatus").isJsonNull())) {
                                                    item3.setEstatus(obj3.get("estatus").getAsInt());
                                                }
                                                if (!(obj3.get("tipo_criterio").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("tipo_criterio").getAsJsonObject();
                                                    if (!(obj4.get("descripcion").isJsonNull())) {
                                                        item3.setTipoCriterio(obj4.get("descripcion").getAsString());
                                                    }
                                                }
                                                item3.setValor(3);
                                                item3.setIdTipoServicioCriterio(obj2.get("id").getAsLong());
                                                items6.add(item3);
                                            }
                                        }
                                    }
                                    if (!(obj2.get("servicios").isJsonNull())) {
                                        JsonArray array2 = obj2.get("servicios").getAsJsonArray();
                                        if (!array2.isJsonNull()) {
                                            for (int y = 0; y < array2.size(); y++) {
                                                JsonObject obj3 = array2.get(y).getAsJsonObject();
                                                Servicio item3 = new Servicio();
                                                if (!(obj3.get("id").isJsonNull())) {
                                                    item3.setId(obj3.get("id").getAsLong());
                                                }
                                                if (!(obj3.get("descripcion").isJsonNull())) {
                                                    item3.setDescripcion(obj3.get("descripcion").getAsString());
                                                }
                                                if (!(obj3.get("precio").isJsonNull())) {
                                                    item3.setPrecio(obj3.get("precio").getAsDouble());
                                                }
                                                if (!(obj3.get("foto_url").isJsonNull())) {
                                                    item3.setFoto(obj3.get("foto_url").getAsString());
                                                }
                                                if (!(obj3.get("estatus").isJsonNull())) {
                                                    item3.setEstatus(obj3.get("estatus").getAsInt());
                                                }
                                                if (!(obj3.get("especialista_id").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("especialista").getAsJsonObject();
                                                    if (!(obj4.get("persona").isJsonNull())) {
                                                        JsonObject obj5 = obj4.get("persona").getAsJsonObject();
                                                        String persona = "";
                                                        if (!(obj5.get("nombre").isJsonNull())) {
                                                            persona = obj5.get("nombre").getAsString() + " ";
                                                        }
                                                        if (!(obj5.get("apellido").isJsonNull())) {
                                                            persona = "- " + persona + obj5.get("apellido").getAsString() + " ";
                                                        }
                                                        item3.setPersona(persona);
                                                    }
                                                }
                                                if (!(obj3.get("ubicacion").isJsonNull())) {
                                                    JsonObject obj4 = obj3.get("ubicacion").getAsJsonObject();
                                                    if (!(obj4.get("descripcion").isJsonNull())) {
                                                        item3.setUbicacion(obj4.get("descripcion").getAsString());
                                                    }
                                                }

                                                if (!(obj3.get("horarios").isJsonNull())) {
                                                    JsonArray array3 = obj3.get("horarios").getAsJsonArray();
                                                    if (!array3.isJsonNull()) {

                                                        for (int j = 0; j < array3.size(); j++) {
                                                            JsonObject obj4 = array3.get(j).getAsJsonObject();
                                                            Horario item4 = new Horario();
                                                            if (!(obj4.get("id").isJsonNull())) {
                                                                item4.setId(obj4.get("id").getAsLong());
                                                            }
                                                            if (!(obj4.get("servicio_id").isJsonNull())) {
                                                                item4.setIdServicioHorario(obj4.get("servicio_id").getAsLong());
                                                            }
                                                            if (!(obj4.get("estatus").isJsonNull())) {
                                                                item4.setEstatus(obj4.get("estatus").getAsInt());
                                                            }

                                                            if (!(obj4.get("turnos").isJsonNull())) {
                                                                JsonArray array4 = obj4.get("turnos").getAsJsonArray();
                                                                if (!array4.isJsonNull()) {

                                                                    for (int l = 0; l < array4.size(); l++) {
                                                                        JsonObject obj5 = array4.get(l).getAsJsonObject();
                                                                        Turno item5 = new Turno();
                                                                        if (!(obj5.get("id").isJsonNull())) {
                                                                            item5.setId(obj5.get("id").getAsLong());
                                                                        }
                                                                        if (!(obj5.get("horario_id").isJsonNull())) {
                                                                            item5.setIdHorarioTurno(obj5.get("horario_id").getAsLong());
                                                                        }
                                                                        if (!(obj5.get("estatus").isJsonNull())) {
                                                                            item5.setEstatus(obj5.get("estatus").getAsInt());
                                                                        }
                                                                        if (!(obj5.get("hora_inicio").isJsonNull())) {
                                                                            item5.setInicio(obj5.get("hora_inicio").getAsString());
                                                                        }
                                                                        if (!(obj5.get("hora_fin").isJsonNull())) {
                                                                            item5.setFin(obj5.get("hora_fin").getAsString());
                                                                        }
                                                                        if (!(obj5.get("tipo_turno_id").isJsonNull())) {
                                                                            item5.setTipo(obj5.get("tipo_turno_id").getAsInt());
                                                                        }
                                                                        if (!(obj5.get("dia_id").isJsonNull())) {
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
                    DAOApp daoApp = new DAOApp();
                    CategoriaServicioDao catedao = daoApp.getCategoriaServicioDao();
                    catedao.deleteAll();
                    catedao.insertInTx(lista);
                    //showAlertDialog(this,"Categoria",String.valueOf(catedao.count()),true);

                    TipoServicioDao tsdao = daoApp.getTipoServicioDao();
                    tsdao.deleteAll();
                    tsdao.insertInTx(items2);
                    //showAlertDialog(this,"tipo servicios",String.valueOf(tsdao.count()),true);

                    ServicioDao sdao = daoApp.getServicioDao();
                    sdao.deleteAll();
                    sdao.insertInTx(items3);
                    //showAlertDialog(this,"servicios",String.valueOf(sdao.count()),true);

                    HorarioDao hdao = daoApp.getHorarioDao();
                    hdao.deleteAll();
                    hdao.insertInTx(items4);
                    //showAlertDialog(this,"Horarios",String.valueOf(hdao.count()),true);


                    TurnoDao tdao = daoApp.getTurnoDao();
                    tdao.deleteAll();
                    tdao.insertInTx(items5);
                    //showAlertDialog(this,"Turnos",String.valueOf(tdao.count()),true);


                    CriterioDao cdao = daoApp.getCriterioDao();
                    cdao.deleteAll();
                    cdao.insertInTx(items6);
                    //showAlertDialog(this,"Criterios",String.valueOf(cdao.count()),true);


                }
            } catch (Exception e) {
                showAlertDialog(this, "Error", e.toString(), false);
            }
        }else {
            DAOApp daoApp = new DAOApp();
            CategoriaServicioDao catedao = daoApp.getCategoriaServicioDao();
            catedao.deleteAll();

            TipoServicioDao tsdao = daoApp.getTipoServicioDao();
            tsdao.deleteAll();

            ServicioDao sdao = daoApp.getServicioDao();
            sdao.deleteAll();

            HorarioDao hdao = daoApp.getHorarioDao();
            hdao.deleteAll();

            TurnoDao tdao = daoApp.getTurnoDao();
            tdao.deleteAll();

            CriterioDao cdao = daoApp.getCriterioDao();
            cdao.deleteAll();
        }
            control=4;
            DAOApp daoApp=new DAOApp();
            UsuarioDao usuarioDao=daoApp.getUsuarioDao();
            List<Usuario> usuarios= usuarioDao.loadAll();
            if (usuarios.size()>0){
                new MiTareaGet(globals.getServer()+"citas.json?persona="+String.valueOf(usuarios.get(0).getId()),"").execute();
            }else{
                control=5;
                new MiTareaGet(globals.getServer()+"tipo_opiniones.json","").execute();
            }


    }

    public void cargarPerfil(String datos){

    }

    public void cargarCitas(String datos){
        //showAlertDialog(this,"Citas",datos,true);
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
                            item.setFecha(globals.fechaFormat(objO.get("fecha").getAsString()));
                        }if (!(objO.get("persona").isJsonNull())) {
                            JsonObject obj1 = objO.get("persona").getAsJsonObject();
                            if (!(obj1.get("nombre").isJsonNull())) {
                                item.setNombre(obj1.get("nombre").getAsString());
                            }if (!(obj1.get("apellido").isJsonNull())) {
                                item.setApellido(obj1.get("apellido").getAsString());
                            }
                        }
                        if (!(objO.get("estatus").isJsonNull())) {
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
                        DAOApp daoApp=new DAOApp();
                        CitaDao dao=daoApp.getCitaDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this,"Cita",String.valueOf(dao.count()),true);

                }
            }catch (Exception e){
                showAlertDialog(this, "Citas", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            CitaDao dao=daoApp.getCitaDao();
            dao.deleteAll();
        }
        control=5;
        new MiTareaGet(globals.getServer()+"tipo_opiniones.json","").execute();
        //finish();
    }

    public void cargarTipoOpinion(String datos){
        //showAlertDialog(this,"opinio",datos,true);
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    List<TipoOpinio> lista=new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        TipoOpinio item = new TipoOpinio();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }if (!(objO.get("estatus").isJsonNull())) {
                            item.setEstatus(objO.get("estatus").getAsInt());
                        }
                        lista.add(item);
                    }
                        DAOApp daoApp=new DAOApp();
                        TipoOpinioDao dao=daoApp.getTipoOpinioDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this,"Tipo opinion",String.valueOf(dao.count()),true);


                }
            }catch (Exception e){
                showAlertDialog(this, "tipo opinion error", e.toString(),false);
            }
        }else{
            DAOApp daoApp=new DAOApp();
            TipoOpinioDao dao=daoApp.getTipoOpinioDao();
            dao.deleteAll();
        }


        control=6;
        new MiTareaGet(globals.getServer()+"preguntas.json","").execute();
        //finish();
    }

    public void cargarAlertas(String datos){

    }

    public void preguntasRespuestas(String datos) {
//        showAlertDialog(this,"Preguntas",datos,true);
        if (datos.length() > 2) {
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array = (JsonArray) obje;
                if (!array.isJsonNull()) {
                    List<Pregunta> lista = new ArrayList<>();
                    for (int x = 0; x < array.size(); x++) {
                        JsonObject objO = array.get(x).getAsJsonObject();
                        Pregunta item = new Pregunta();
                        if (!(objO.get("id").isJsonNull())) {
                            item.setId(objO.get("id").getAsLong());
                        }
                        if (!(objO.get("descripcion").isJsonNull())) {
                            item.setDescripcion(objO.get("descripcion").getAsString());
                        }
                        if (!(objO.get("respuesta").isJsonNull())) {
                            item.setRespuesta(objO.get("respuesta").getAsString());
                        }
                        if (!(objO.get("tipo_pregunta").isJsonNull())) {
                            JsonObject obj1 = objO.get("tipo_pregunta").getAsJsonObject();
                            if (!(obj1.get("descripcion").isJsonNull())) {
                                item.setTipoPregunta(obj1.get("descripcion").getAsString());
                            }

                        }
                        lista.add(item);
                    }
                        DAOApp daoApp = new DAOApp();
                        PreguntaDao dao = daoApp.getPreguntaDao();
                        dao.deleteAll();
                        dao.insertInTx(lista);
                        //showAlertDialog(this, "Preguntas", String.valueOf(dao.count()), true);


                }
            } catch (Exception e) {
                showAlertDialog(this, "Preguntas", e.toString(), false);
            }
        }else{
            DAOApp daoApp = new DAOApp();
            PreguntaDao dao = daoApp.getPreguntaDao();
            dao.deleteAll();
        }
        //showAlertDialog(this,"Servicio",datos,true);
        progressBarInicio.setVisibility(View.INVISIBLE);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    //Clase para consumir servicio por get
    private class MiTareaGet extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTareaGet(String url,String x){
            Log.d("url",url);
            this.ur=url+x;

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

            try
            {
                HttpResponse resp = httpClient.execute(del);
                responce = EntityUtils.toString(resp.getEntity());
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return responce;
        }

        @Override
        protected void onProgressUpdate (Float... valores) {

        }

        @Override//Acción a realizar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {
            switch (control){
                case 1:
                    cargarEventos(tiraJson);
                    break;
                case 2:
                    cargarNoticias(tiraJson);
                    break;
                case 3:
                    cargarServicios(tiraJson);
                    break;
                case 4:
                    cargarCitas(tiraJson);
                    break;
                case 5:
                    cargarTipoOpinion(tiraJson);
                    break;
                case 6:
                    preguntasRespuestas(tiraJson);
                    break;
            }
        }
    }
}
