package app.alice;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Main {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1,"org.app.alice.modelo");
        schema.enableKeepSectionsByDefault();
        createDataBase(schema);
        DaoGenerator generator = new DaoGenerator();
        generator.generateAll(schema,args[0]);
    }
    public static void createDataBase(Schema schema){
        Entity usuario =schema.addEntity("Usuario");
        usuario.addIdProperty();
        usuario.addLongProperty("persona_id");
        usuario.addStringProperty("correo");
        usuario.addStringProperty("clave");
        usuario.addStringProperty("cedula");
        usuario.addStringProperty("nombre");
        usuario.addStringProperty("apellido");
        usuario.addStringProperty("telefono");
        usuario.addStringProperty("fecha");
        usuario.addStringProperty("sexo");
        usuario.addStringProperty("direccion");

        Entity evento =schema.addEntity("Evento");
        evento.addIdProperty();
        evento.addIntProperty("codigo");
        evento.addStringProperty("titulo");
        evento.addStringProperty("descripcion");
        evento.addStringProperty("contenido");
        evento.addIntProperty("estatus");
        evento.addStringProperty("tipoEvento");
        evento.addStringProperty("tipoServicio");
        evento.addStringProperty("foto");
        evento.addStringProperty("fecha");
        evento.addStringProperty("lugar");

        Entity noticia =schema.addEntity("Noticia");
        noticia.addIdProperty();
        noticia.addIntProperty("codigo");
        noticia.addStringProperty("titulo");
        noticia.addStringProperty("descripcion");
        noticia.addIntProperty("estatus");
        noticia.addStringProperty("tipoNoticia");
        noticia.addStringProperty("contenido");
        noticia.addStringProperty("fecha");

        Entity categoria =schema.addEntity("CategoriaServicio");
        categoria.addIdProperty();
        categoria.addIntProperty("codigo");
        categoria.addStringProperty("descripcion");
        categoria.addIntProperty("estatus");
        categoria.addStringProperty("foto");

        Entity tipo =schema.addEntity("TipoServicio");
        tipo.addIdProperty();
        tipo.addIntProperty("codigo");
        tipo.addStringProperty("descripcion");
        tipo.addStringProperty("texto");
        tipo.addIntProperty("estatus");
        tipo.addStringProperty("foto");

        Entity servicio =schema.addEntity("Servicio");
        servicio.addIdProperty();
        servicio.addIntProperty("codigo");
        servicio.addStringProperty("descripcion");
        servicio.addIntProperty("estatus");
        servicio.addStringProperty("ubicacion");
        servicio.addStringProperty("foto");
        servicio.addDoubleProperty("precio");
        servicio.addStringProperty("Persona");

        Entity horario = schema.addEntity("Horario");
        horario.addIdProperty();
        horario.addIntProperty("estatus");

        Entity turno = schema.addEntity("Turno");
        turno.addIdProperty();
        turno.addStringProperty("inicio");
        turno.addStringProperty("fin");
        turno.addIntProperty("tipo");
        turno.addIntProperty("dia");
        turno.addIntProperty("estatus");

        Entity alerta = schema.addEntity("Alerta");
        alerta.addIdProperty();
        alerta.addStringProperty("descripcion");
        alerta.addIntProperty("estatus");
        alerta.addIntProperty("tipo_notificacion_id");
        alerta.addStringProperty("mensaje");
        alerta.addStringProperty("url");
        alerta.addStringProperty("fecha");
        alerta.addLongProperty("entidad_id");

        Entity cita=schema.addEntity("Cita");
        cita.addIdProperty();
        cita.addStringProperty("fecha");
        cita.addStringProperty("descripcion");
        cita.addStringProperty("ubicacion");
        cita.addIntProperty("estatus");
        cita.addDoubleProperty("precio");
        cita.addStringProperty("nombre");
        cita.addStringProperty("apellido");

        Entity tipoOpinio=schema.addEntity("TipoOpinio");
        tipoOpinio.addIdProperty();
        tipoOpinio.addStringProperty("descripcion");
        tipoOpinio.addIntProperty("estatus");

        Entity criterio=schema.addEntity("Criterio");
        criterio.addIdProperty();
        criterio.addLongProperty("codigo");
        criterio.addStringProperty("descripcion");
        criterio.addIntProperty("estatus");
        criterio.addStringProperty("tipoCriterio");
        criterio.addIntProperty("valor");

        Entity pregunta=schema.addEntity("Pregunta");
        pregunta.addIdProperty();
        pregunta.addStringProperty("descripcion");
        pregunta.addStringProperty("respuesta");
        pregunta.addStringProperty("tipoPregunta");
        pregunta.addIntProperty("estatus");

        Property idCategoriaTipo = tipo.addLongProperty("idCategoriaServicio").getProperty();
        ToMany categoriaTipo = categoria.addToMany(tipo, idCategoriaTipo);
        categoriaTipo.setName("TipoServicio");

        Property idTipoServicioServicio = servicio.addLongProperty("idTipoServicio").getProperty();
        ToMany tipoServicioServicio = tipo.addToMany(servicio, idTipoServicioServicio);
        tipoServicioServicio.setName("Servicio");

        Property idServicioHorario = horario.addLongProperty("idServicioHorario").getProperty();
        ToMany servicioHorario = servicio.addToMany(horario, idServicioHorario);
        servicioHorario.setName("Horario");

        Property idHorarioTurno = turno.addLongProperty("idHorarioTurno").getProperty();
        ToMany horarioTurno = horario.addToMany(turno, idHorarioTurno);
        horarioTurno.setName("Turno");

        Property idUsuarioAlerta = alerta.addLongProperty("idUsuarioAlerta").getProperty();
        ToMany usuarioAlerta = horario.addToMany(alerta, idUsuarioAlerta);
        usuarioAlerta.setName("Alerta");

        Property idServicioCita = cita.addLongProperty("idServicioCita").getProperty();
        ToMany servicioCita = servicio.addToMany(cita, idServicioCita);
        servicioCita.setName("Cita");

        Property idTipoServicioCriterio = criterio.addLongProperty("idTipoServicioCriterio").getProperty();
        ToMany tipoServicioCriterio = tipo.addToMany(criterio, idTipoServicioCriterio);
        tipoServicioCriterio.setName("Criterio");
    }
}
