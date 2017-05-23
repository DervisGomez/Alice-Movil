package org.app.alice.modelo;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.alice.modelo.Usuario;
import org.app.alice.modelo.Evento;
import org.app.alice.modelo.Noticia;
import org.app.alice.modelo.CategoriaServicio;
import org.app.alice.modelo.TipoServicio;
import org.app.alice.modelo.Servicio;
import org.app.alice.modelo.Horario;
import org.app.alice.modelo.Turno;
import org.app.alice.modelo.Alerta;
import org.app.alice.modelo.Cita;
import org.app.alice.modelo.TipoOpinio;
import org.app.alice.modelo.Criterio;
import org.app.alice.modelo.Pregunta;

import org.app.alice.modelo.UsuarioDao;
import org.app.alice.modelo.EventoDao;
import org.app.alice.modelo.NoticiaDao;
import org.app.alice.modelo.CategoriaServicioDao;
import org.app.alice.modelo.TipoServicioDao;
import org.app.alice.modelo.ServicioDao;
import org.app.alice.modelo.HorarioDao;
import org.app.alice.modelo.TurnoDao;
import org.app.alice.modelo.AlertaDao;
import org.app.alice.modelo.CitaDao;
import org.app.alice.modelo.TipoOpinioDao;
import org.app.alice.modelo.CriterioDao;
import org.app.alice.modelo.PreguntaDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig usuarioDaoConfig;
    private final DaoConfig eventoDaoConfig;
    private final DaoConfig noticiaDaoConfig;
    private final DaoConfig categoriaServicioDaoConfig;
    private final DaoConfig tipoServicioDaoConfig;
    private final DaoConfig servicioDaoConfig;
    private final DaoConfig horarioDaoConfig;
    private final DaoConfig turnoDaoConfig;
    private final DaoConfig alertaDaoConfig;
    private final DaoConfig citaDaoConfig;
    private final DaoConfig tipoOpinioDaoConfig;
    private final DaoConfig criterioDaoConfig;
    private final DaoConfig preguntaDaoConfig;

    private final UsuarioDao usuarioDao;
    private final EventoDao eventoDao;
    private final NoticiaDao noticiaDao;
    private final CategoriaServicioDao categoriaServicioDao;
    private final TipoServicioDao tipoServicioDao;
    private final ServicioDao servicioDao;
    private final HorarioDao horarioDao;
    private final TurnoDao turnoDao;
    private final AlertaDao alertaDao;
    private final CitaDao citaDao;
    private final TipoOpinioDao tipoOpinioDao;
    private final CriterioDao criterioDao;
    private final PreguntaDao preguntaDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        usuarioDaoConfig = daoConfigMap.get(UsuarioDao.class).clone();
        usuarioDaoConfig.initIdentityScope(type);

        eventoDaoConfig = daoConfigMap.get(EventoDao.class).clone();
        eventoDaoConfig.initIdentityScope(type);

        noticiaDaoConfig = daoConfigMap.get(NoticiaDao.class).clone();
        noticiaDaoConfig.initIdentityScope(type);

        categoriaServicioDaoConfig = daoConfigMap.get(CategoriaServicioDao.class).clone();
        categoriaServicioDaoConfig.initIdentityScope(type);

        tipoServicioDaoConfig = daoConfigMap.get(TipoServicioDao.class).clone();
        tipoServicioDaoConfig.initIdentityScope(type);

        servicioDaoConfig = daoConfigMap.get(ServicioDao.class).clone();
        servicioDaoConfig.initIdentityScope(type);

        horarioDaoConfig = daoConfigMap.get(HorarioDao.class).clone();
        horarioDaoConfig.initIdentityScope(type);

        turnoDaoConfig = daoConfigMap.get(TurnoDao.class).clone();
        turnoDaoConfig.initIdentityScope(type);

        alertaDaoConfig = daoConfigMap.get(AlertaDao.class).clone();
        alertaDaoConfig.initIdentityScope(type);

        citaDaoConfig = daoConfigMap.get(CitaDao.class).clone();
        citaDaoConfig.initIdentityScope(type);

        tipoOpinioDaoConfig = daoConfigMap.get(TipoOpinioDao.class).clone();
        tipoOpinioDaoConfig.initIdentityScope(type);

        criterioDaoConfig = daoConfigMap.get(CriterioDao.class).clone();
        criterioDaoConfig.initIdentityScope(type);

        preguntaDaoConfig = daoConfigMap.get(PreguntaDao.class).clone();
        preguntaDaoConfig.initIdentityScope(type);

        usuarioDao = new UsuarioDao(usuarioDaoConfig, this);
        eventoDao = new EventoDao(eventoDaoConfig, this);
        noticiaDao = new NoticiaDao(noticiaDaoConfig, this);
        categoriaServicioDao = new CategoriaServicioDao(categoriaServicioDaoConfig, this);
        tipoServicioDao = new TipoServicioDao(tipoServicioDaoConfig, this);
        servicioDao = new ServicioDao(servicioDaoConfig, this);
        horarioDao = new HorarioDao(horarioDaoConfig, this);
        turnoDao = new TurnoDao(turnoDaoConfig, this);
        alertaDao = new AlertaDao(alertaDaoConfig, this);
        citaDao = new CitaDao(citaDaoConfig, this);
        tipoOpinioDao = new TipoOpinioDao(tipoOpinioDaoConfig, this);
        criterioDao = new CriterioDao(criterioDaoConfig, this);
        preguntaDao = new PreguntaDao(preguntaDaoConfig, this);

        registerDao(Usuario.class, usuarioDao);
        registerDao(Evento.class, eventoDao);
        registerDao(Noticia.class, noticiaDao);
        registerDao(CategoriaServicio.class, categoriaServicioDao);
        registerDao(TipoServicio.class, tipoServicioDao);
        registerDao(Servicio.class, servicioDao);
        registerDao(Horario.class, horarioDao);
        registerDao(Turno.class, turnoDao);
        registerDao(Alerta.class, alertaDao);
        registerDao(Cita.class, citaDao);
        registerDao(TipoOpinio.class, tipoOpinioDao);
        registerDao(Criterio.class, criterioDao);
        registerDao(Pregunta.class, preguntaDao);
    }
    
    public void clear() {
        usuarioDaoConfig.getIdentityScope().clear();
        eventoDaoConfig.getIdentityScope().clear();
        noticiaDaoConfig.getIdentityScope().clear();
        categoriaServicioDaoConfig.getIdentityScope().clear();
        tipoServicioDaoConfig.getIdentityScope().clear();
        servicioDaoConfig.getIdentityScope().clear();
        horarioDaoConfig.getIdentityScope().clear();
        turnoDaoConfig.getIdentityScope().clear();
        alertaDaoConfig.getIdentityScope().clear();
        citaDaoConfig.getIdentityScope().clear();
        tipoOpinioDaoConfig.getIdentityScope().clear();
        criterioDaoConfig.getIdentityScope().clear();
        preguntaDaoConfig.getIdentityScope().clear();
    }

    public UsuarioDao getUsuarioDao() {
        return usuarioDao;
    }

    public EventoDao getEventoDao() {
        return eventoDao;
    }

    public NoticiaDao getNoticiaDao() {
        return noticiaDao;
    }

    public CategoriaServicioDao getCategoriaServicioDao() {
        return categoriaServicioDao;
    }

    public TipoServicioDao getTipoServicioDao() {
        return tipoServicioDao;
    }

    public ServicioDao getServicioDao() {
        return servicioDao;
    }

    public HorarioDao getHorarioDao() {
        return horarioDao;
    }

    public TurnoDao getTurnoDao() {
        return turnoDao;
    }

    public AlertaDao getAlertaDao() {
        return alertaDao;
    }

    public CitaDao getCitaDao() {
        return citaDao;
    }

    public TipoOpinioDao getTipoOpinioDao() {
        return tipoOpinioDao;
    }

    public CriterioDao getCriterioDao() {
        return criterioDao;
    }

    public PreguntaDao getPreguntaDao() {
        return preguntaDao;
    }

}