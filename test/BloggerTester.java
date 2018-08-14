import AuthClient.OAuth2Client;
import AuthClient.OAuth2ClientCredentials;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;



/**
 * Created by tiffanytillett on 7/31/18.
 */

public class BloggerTester {
    String client_id = "989638276769-p9vrt6a1vn9athl7vhcno54t5b2tm08o.apps.googleusercontent.com";
    String client_secret = "5N4H4OAViTpxWb-MT-MMqlbM";

    String baseURI = "https://www.googleapis.com/blogger/v3/blogs/";
    String apiKey = "AIzaSyAYHQw2hPRA5yAdz7A3I7qUo43qa-aZUCM";
    String blogID = "54850391780151973";

    final String SCOPE = "https://www.googleapis.com/auth/blogger";


    //GET https://www.googleapis.com/blogger/v3/blogs/54850391780151973?key=AIzaSyAYHQw2hPRA5yAdz7A3I7qUo43qa-aZUCM

    @Test
    public void getBlog()
    {
        RestAssured.baseURI = baseURI + blogID;

        Response response =
                given()
                .param("key", apiKey)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void getBlogPost()
    {
        //https://www.googleapis.com/blogger/v3/blogs/blogId/posts/postId

        String postId = "1018378491826793697";
        RestAssured.baseURI = baseURI + blogID + "/posts/" + postId;

        Response response =
                given()
                        .param("key", apiKey)
                        .when()
                        .get()
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void getUserBlogs()
    {
        OAuth2Client.authorize();

        RestAssured.baseURI = "https://www.googleapis.com/blogger/v3/users/self/blogs";
        Response response =
                given()
                    .auth().oauth2(OAuth2ClientCredentials.AccessToken)
                    .contentType(ContentType.JSON)
                .expect()
                    .log().all()
                    .statusCode(200)
                    .when()
                .get().then().extract().response();
        System.out.println(response.toString());
    }
}
