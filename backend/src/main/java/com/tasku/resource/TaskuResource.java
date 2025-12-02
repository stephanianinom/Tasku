package com.tasku.resource;

import com.tasku.model.Tasku;
import com.tasku.service.TaskuService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/taskus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskuResource {

    @Inject
    TaskuService service;

    @GET
    public Response obtenerTodas(@QueryParam("completada") Boolean completada,
                                  @QueryParam("titulo") String titulo) {
        try {
            List<Tasku> taskus;
            
            if (completada != null) {
                taskus = service.obtenerPorCompletada(completada);
            } else if (titulo != null && !titulo.trim().isEmpty()) {
                taskus = service.buscarPorTitulo(titulo);
            } else {
                taskus = service.obtenerTodas();
            }
            
            return Response.ok(taskus).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener las tareas: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        try {
            Optional<Tasku> tasku = service.obtenerPorId(id);
            if (tasku.isPresent()) {
                return Response.ok(tasku.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarea no encontrada con id: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener la tarea: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response crear(Tasku tasku) {
        try {
            Tasku nuevaTasku = service.crear(tasku);
            return Response.status(Response.Status.CREATED)
                    .entity(nuevaTasku)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error de validación: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear la tarea: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Tasku tasku) {
        try {
            Optional<Tasku> taskuActualizada = service.actualizar(id, tasku);
            if (taskuActualizada.isPresent()) {
                return Response.ok(taskuActualizada.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarea no encontrada con id: " + id)
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error de validación: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar la tarea: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            boolean eliminada = service.eliminar(id);
            if (eliminada) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarea no encontrada con id: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar la tarea: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/completar")
    public Response marcarCompletada(@PathParam("id") Long id,
                                     @QueryParam("completada") @DefaultValue("true") Boolean completada) {
        try {
            Optional<Tasku> tasku = service.marcarCompletada(id, completada);
            if (tasku.isPresent()) {
                return Response.ok(tasku.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarea no encontrada con id: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el estado: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/estadisticas")
    public Response obtenerEstadisticas() {
        try {
            long completadas = service.contarCompletadas();
            long pendientes = service.contarPendientes();
            
            return Response.ok()
                    .entity("{\"completadas\":" + completadas + ",\"pendientes\":" + pendientes + "}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener estadísticas: " + e.getMessage())
                    .build();
        }
    }
}
