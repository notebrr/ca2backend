package rest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import facades.UserFacade;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseOptions;
import io.restassured.response.ValidatableResponse;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import entities.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;

class UserResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User u1, u2;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static UserFacade facade;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        u1 = new User("Rehman","test");
        u2 = new User("Abdi", "test");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(u1);
            em.persist(u2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    void allUsers() {
                List<UserDTO> userDTOList =given().contentType("application/json").when()
                        .get("/info/all")
                        .then().extract().body().jsonPath()
                        .getList("", UserDTO.class);
        System.out.println(userDTOList);
        System.out.println(new UserDTO(u1));

//        assertThat(userDTOList, containsInAnyOrder(new UserDTO(u1)));
        assertEquals(2, userDTOList.size());
    }


    @Test
    void getUserById() {
        UserDTO userDTO = given().contentType("application/json").when()
                .get("/info/user/" + u1.getId()).as(UserDTO.class);

        assertThat(userDTO, equalTo(new UserDTO(u1)));
    }


    @Test
    void createUser() {
        List<String> roles = new ArrayList<>();
        String requestBody = GSON.toJson(new UserDTO("Rehman","test",roles));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/info")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("userName", equalTo("Rehman"));
    }

    @Test
    void updateUser() {
        UserDTO userDTO = new UserDTO(u1);
        userDTO.setUserName("nytnavn");
        userDTO.setRoles(new ArrayList<>());
        given()
                .header("Content-type", ContentType.JSON)
                .body(GSON.toJson(userDTO))
                .when()
                .put("/info/user/update")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(u1.getId().intValue()))
                .body("userName", equalTo("nytnavn"));
    }

    @Test
    void deleteUser() {
        int userIdToDelete = 2;
        given()
                .header("Content-type", ContentType.JSON)
                .when()
                .delete("/info/user/" +userIdToDelete)
                .then()
                .statusCode(200);
    }
}