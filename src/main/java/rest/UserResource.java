package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import errorhandling.API_Exception;
import facades.UserFacade;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade facade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("all")
//    public String allUsers() {
//        EntityManager em = EMF.createEntityManager();
//        try {
//            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
//            List<User> users = query.getResultList();
//            return "[" + users.size() + "]";
//        } finally {
//            em.close();
//        }
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "otheruser";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("all")
    public Response allUsers() {
        return Response.ok().entity(GSON.toJson(facade.getAllUsers())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user/{id}")
    public String getUserById(@PathParam("id") Long id) {
        UserDTO userDTO = facade.getUserById(id);
        return GSON.toJson(userDTO);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String createUser(String content) {
        UserDTO userDTO = facade.createUser(GSON.fromJson(content, UserDTO.class));
        return GSON.toJson(userDTO);
    }

    @PUT
    @Path("user/update")
    @Produces({MediaType.APPLICATION_JSON})
    public String UpdateUser(String content) {
        UserDTO userDTO = GSON.fromJson(content, UserDTO.class);
        userDTO = facade.updateUser(userDTO);
        return GSON.toJson(userDTO);
    }


    @DELETE
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteUser(@PathParam("id") long id) throws API_Exception {
        UserDTO userDeleted = facade.deleteUser(id);
        return Response.ok().entity(GSON.toJson(userDeleted)).build();
    }
}