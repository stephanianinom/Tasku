package com.tasku.resource;

import com.tasku.H2TestProfile;
import com.tasku.model.Tasku;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestProfile(H2TestProfile.class)
class TaskuResourceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testObtenerTodas() {
        given()
                .when().get("/api/taskus")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void testCrear_Exitoso() {
        Tasku nuevaTasku = new Tasku("Tarea de Prueba", "Descripción de prueba");
        
        given()
                .contentType(ContentType.JSON)
                .body(nuevaTasku)
                .when().post("/api/taskus")
                .then()
                .statusCode(201)
                .body("titulo", is("Tarea de Prueba"))
                .body("id", notNullValue());
    }

    @Test
    void testCrear_TituloVacio() {
        Tasku taskuInvalida = new Tasku("", "Descripción");
        
        given()
                .contentType(ContentType.JSON)
                .body(taskuInvalida)
                .when().post("/api/taskus")
                .then()
                .statusCode(400);
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        given()
                .when().get("/api/taskus/99999")
                .then()
                .statusCode(404);
    }

    @Test
    void testObtenerEstadisticas() {
        given()
                .when().get("/api/taskus/estadisticas")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }
}
