package rest;

import com.google.gson.Gson;
import dtos.ChuckDTO;
import dtos.CombinedJokesDTO;
import dtos.DadDTO;
import utils.HttpUtils;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("jokes")
public class JokeResource {
    @Context
    private UriInfo context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokes() throws IOException {
        Gson gson = new Gson();

        String chuckJoke = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        String dadJoke = HttpUtils.fetchData("https://icanhazdadjoke.com");
        ChuckDTO chuckDTO = gson.fromJson(chuckJoke, ChuckDTO.class);
        DadDTO dadDTO = gson.fromJson(dadJoke, DadDTO.class);
        CombinedJokesDTO combinedJokesDTO = new CombinedJokesDTO(chuckDTO, dadDTO);
        return gson.toJson(combinedJokesDTO, CombinedJokesDTO.class);
    }
}
