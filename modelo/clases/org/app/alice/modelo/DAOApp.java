package org.app.alice.modelo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by dervis on 08/12/16.
 */
public class DAOApp extends Application {

    static UsuarioDao usuarioDao;
    static EventoDao eventoDao;
    static NoticiaDao noticiaDao;
    static ServicioDao servicioDao;
    static CategoriaServicioDao categoriaServicioDao;
    static TipoServicioDao tipoServicioDao;
    static HorarioDao horarioDao;
    static TurnoDao turnoDao;
    static AlertaDao alertaDao;
    static CitaDao citaDao;
    static TipoOpinioDao tipoOpinioDao;
    static CriterioDao criterioDao;
    static PreguntaDao preguntaDao;

    public UsuarioDao getUsuarioDao(){
        return usuarioDao;
    }
    public EventoDao getEventoDao() { return eventoDao; }
    public NoticiaDao getNoticiaDao(){ return noticiaDao; }
    public ServicioDao getServicioDao() { return servicioDao; }
    public CategoriaServicioDao getCategoriaServicioDao() { return categoriaServicioDao; }
    public TipoServicioDao getTipoServicioDao() { return tipoServicioDao; }
    public HorarioDao getHorarioDao(){ return horarioDao; }
    public TurnoDao getTurnoDao() { return turnoDao; }
    public AlertaDao getAlertaDao() { return alertaDao; }
    public CitaDao getCitaDao() { return citaDao; }
    public TipoOpinioDao getTipoOpinioDao() { return tipoOpinioDao; }
    public CriterioDao getCriterioDao() { return  criterioDao; }
    public PreguntaDao getPreguntaDao() {return preguntaDao; }


    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "alice", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        usuarioDao = daoSession.getUsuarioDao();
        eventoDao = daoSession.getEventoDao();
        noticiaDao = daoSession.getNoticiaDao();
        servicioDao = daoSession.getServicioDao();
        categoriaServicioDao = daoSession.getCategoriaServicioDao();
        tipoServicioDao = daoSession.getTipoServicioDao();
        horarioDao= daoSession.getHorarioDao();
        turnoDao=daoSession.getTurnoDao();
        alertaDao=daoSession.getAlertaDao();
        citaDao=daoSession.getCitaDao();
        tipoOpinioDao=daoSession.getTipoOpinioDao();
        criterioDao=daoSession.getCriterioDao();
        preguntaDao=daoSession.getPreguntaDao();
    }
}
