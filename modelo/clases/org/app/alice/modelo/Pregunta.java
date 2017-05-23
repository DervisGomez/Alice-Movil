package org.app.alice.modelo;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table PREGUNTA.
 */
public class Pregunta {

    private Long id;
    private String descripcion;
    private String respuesta;
    private String tipoPregunta;
    private Integer estatus;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Pregunta() {
    }

    public Pregunta(Long id) {
        this.id = id;
    }

    public Pregunta(Long id, String descripcion, String respuesta, String tipoPregunta, Integer estatus) {
        this.id = id;
        this.descripcion = descripcion;
        this.respuesta = respuesta;
        this.tipoPregunta = tipoPregunta;
        this.estatus = estatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}