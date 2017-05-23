package org.app.alice.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.alice.modelo.Pregunta;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PREGUNTA.
*/
public class PreguntaDao extends AbstractDao<Pregunta, Long> {

    public static final String TABLENAME = "PREGUNTA";

    /**
     * Properties of entity Pregunta.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Descripcion = new Property(1, String.class, "descripcion", false, "DESCRIPCION");
        public final static Property Respuesta = new Property(2, String.class, "respuesta", false, "RESPUESTA");
        public final static Property TipoPregunta = new Property(3, String.class, "tipoPregunta", false, "TIPO_PREGUNTA");
        public final static Property Estatus = new Property(4, Integer.class, "estatus", false, "ESTATUS");
    };


    public PreguntaDao(DaoConfig config) {
        super(config);
    }
    
    public PreguntaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PREGUNTA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DESCRIPCION' TEXT," + // 1: descripcion
                "'RESPUESTA' TEXT," + // 2: respuesta
                "'TIPO_PREGUNTA' TEXT," + // 3: tipoPregunta
                "'ESTATUS' INTEGER);"); // 4: estatus
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PREGUNTA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Pregunta entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String descripcion = entity.getDescripcion();
        if (descripcion != null) {
            stmt.bindString(2, descripcion);
        }
 
        String respuesta = entity.getRespuesta();
        if (respuesta != null) {
            stmt.bindString(3, respuesta);
        }
 
        String tipoPregunta = entity.getTipoPregunta();
        if (tipoPregunta != null) {
            stmt.bindString(4, tipoPregunta);
        }
 
        Integer estatus = entity.getEstatus();
        if (estatus != null) {
            stmt.bindLong(5, estatus);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Pregunta readEntity(Cursor cursor, int offset) {
        Pregunta entity = new Pregunta( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // descripcion
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // respuesta
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // tipoPregunta
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // estatus
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Pregunta entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescripcion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRespuesta(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTipoPregunta(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setEstatus(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Pregunta entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Pregunta entity) {
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
