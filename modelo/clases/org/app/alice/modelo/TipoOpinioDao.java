package org.app.alice.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.alice.modelo.TipoOpinio;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TIPO_OPINIO.
*/
public class TipoOpinioDao extends AbstractDao<TipoOpinio, Long> {

    public static final String TABLENAME = "TIPO_OPINIO";

    /**
     * Properties of entity TipoOpinio.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Descripcion = new Property(1, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Estatus = new Property(2, Integer.class, "estatus", false, "ESTATUS");
    };


    public TipoOpinioDao(DaoConfig config) {
        super(config);
    }
    
    public TipoOpinioDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TIPO_OPINIO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DESCRIPCION' TEXT," + // 1: descripcion
                "'ESTATUS' INTEGER);"); // 2: estatus
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TIPO_OPINIO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TipoOpinio entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(2, descripcion);
        }
 
        Integer estatus = entity.getEstatus();
        if (estatus != null) {
            stmt.bindLong(3, estatus);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TipoOpinio readEntity(Cursor cursor, int offset) {
        TipoOpinio entity = new TipoOpinio( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // descripcion
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2) // estatus
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TipoOpinio entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescripcion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEstatus(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TipoOpinio entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TipoOpinio entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}