package org.app.alice.modelo;

import java.util.List;
import org.app.alice.modelo.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table CATEGORIA_SERVICIO.
 */
public class CategoriaServicio {

    private Long id;
    private Integer codigo;
    private String descripcion;
    private Integer estatus;
    private String foto;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CategoriaServicioDao myDao;

    private List<TipoServicio> TipoServicio;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public CategoriaServicio() {
    }

    public CategoriaServicio(Long id) {
        this.id = id;
    }

    public CategoriaServicio(Long id, Integer codigo, String descripcion, Integer estatus, String foto) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.foto = foto;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoriaServicioDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<TipoServicio> getTipoServicio() {
        if (TipoServicio == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TipoServicioDao targetDao = daoSession.getTipoServicioDao();
            List<TipoServicio> TipoServicioNew = targetDao._queryCategoriaServicio_TipoServicio(id);
            synchronized (this) {
                if(TipoServicio == null) {
                    TipoServicio = TipoServicioNew;
                }
            }
        }
        return TipoServicio;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTipoServicio() {
        TipoServicio = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
