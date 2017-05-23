package org.app.alice.service;

import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.DAOApp;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.NoticiaDao;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.ServicioDao;

/**
 * Created by dervis on 18/03/17.
 */
public class CargarDatosPrueba {
    public void CargarEvento(){
        DAOApp daoApp=new DAOApp();
        EventoDao eventoDao=daoApp.getEventoDao();
        eventoDao.deleteAll();
        for (int x=0;x<3;x++){
            Evento evento=new Evento((long)x,x,"Titulo Evento"+String.valueOf(x),"Descripción","contenido",1,"tipo evento","tipo servicio","","01/02/2017","Barrquisimeto");
            eventoDao.insert(evento);
        }
    }
    public void CargarNoticia(){
        DAOApp daoApp=new DAOApp();
        NoticiaDao noticiaDao=daoApp.getNoticiaDao();
        noticiaDao.deleteAll();
        for (int x=0;x<3;x++){
            Noticia noticia=new Noticia((long)x,x,"Titulo Evento"+String.valueOf(x),"Descripción",1,"tipo noticia","contenido","01/02/2017");
            noticiaDao.insert(noticia);
        }
    }
    public void CargarNotificaciones1(){
        DAOApp daoApp=new DAOApp();
        AlertaDao alertaDao=daoApp.getAlertaDao();
        alertaDao.deleteAll();
        for (int x=0;x<4;x++) {
            int y=x+1;
            Alerta alerta = new Alerta((long) x,"Descripcion",1,y,"mensaje","url","01/02/2017",(long)1,(long)1);
            alertaDao.insert(alerta);
        }
    }
}


