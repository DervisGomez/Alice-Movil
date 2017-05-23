package org.app.alice.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.alice.modelo.Usuario;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USUARIO.
*/
public class UsuarioDao extends AbstractDao<Usuario, Long> {

    public static final String TABLENAME = "USUARIO";

    /**
     * Properties of entity Usuario.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Persona_id = new Property(1, Long.class, "persona_id", false, "PERSONA_ID");
        public final static Property Correo = new Property(2, String.class, "correo", false, "CORREO");
        public final static Property Clave = new Property(3, String.class, "clave", false, "CLAVE");
        public final static Property Cedula = new Property(4, String.class, "cedula", false, "CEDULA");
        public final static Property Nombre = new Property(5, String.class, "nombre", false, "NOMBRE");
        public final static Property Apellido = new Property(6, String.class, "apellido", false, "APELLIDO");
        public final static Property Telefono = new Property(7, String.class, "telefono", false, "TELEFONO");
        public final static Property Fecha = new Property(8, String.class, "fecha", false, "FECHA");
        public final static Property Sexo = new Property(9, String.class, "sexo", false, "SEXO");
        public final static Property Direccion = new Property(10, String.class, "direccion", false, "DIRECCION");
    };


    public UsuarioDao(DaoConfig config) {
        super(config);
    }
    
    public UsuarioDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USUARIO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'PERSONA_ID' INTEGER," + // 1: persona_id
                "'CORREO' TEXT," + // 2: correo
                "'CLAVE' TEXT," + // 3: clave
                "'CEDULA' TEXT," + // 4: cedula
                "'NOMBRE' TEXT," + // 5: nombre
                "'APELLIDO' TEXT," + // 6: apellido
                "'TELEFONO' TEXT," + // 7: telefono
                "'FECHA' TEXT," + // 8: fecha
                "'SEXO' TEXT," + // 9: sexo
                "'DIRECCION' TEXT);"); // 10: direccion
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USUARIO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Usuario entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long persona_id = entity.getPersona_id();
        if (persona_id != null) {
            stmt.bindLong(2, persona_id);
        }
 
        String correo = entity.getCorreo();
        if (correo != null) {
            stmt.bindString(3, correo);
        }
 
        String clave = entity.getClave();
        if (clave != null) {
            stmt.bindString(4, clave);
        }
 
        String cedula = entity.getCedula();
        if (cedula != null) {
            stmt.bindString(5, cedula);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(6, nombre);
        }
 
        String apellido = entity.getApellido();
        if (apellido != null) {
            stmt.bindString(7, apellido);
        }
 
        String telefono = entity.getTelefono();
        if (telefono != null) {
            stmt.bindString(8, telefono);
        }
 
        String fecha = entity.getFecha();
        if (fecha != null) {
            stmt.bindString(9, fecha);
        }
 
        String sexo = entity.getSexo();
        if (sexo != null) {
            stmt.bindString(10, sexo);
        }
 
        String direccion = entity.getDireccion();
        if (direccion != null) {
            stmt.bindString(11, direccion);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Usuario readEntity(Cursor cursor, int offset) {
        Usuario entity = new Usuario( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // persona_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // correo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // clave
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // cedula
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // nombre
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // apellido
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // telefono
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // fecha
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // sexo
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // direccion
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Usuario entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPersona_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setCorreo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setClave(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCedula(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNombre(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setApellido(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTelefono(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFecha(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSexo(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setDireccion(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Usuario entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Usuario entity) {
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
