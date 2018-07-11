import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class OMDBTester
{
    String baseURI = "http://www.omdbapi.com/";
    String apiKey = "34efc8d9";

    @Test
    public void getMovieByTitle()
    {
        String movieTitle = "Gladiator";

        RestAssured.baseURI = baseURI;

        given()
                .param("apikey", apiKey)
                .param("t", movieTitle)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200);
    }
}
