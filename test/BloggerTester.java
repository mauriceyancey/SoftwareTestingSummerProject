import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;


/**
 * Created by tiffanytillett on 7/31/18.
 */

public class BloggerTester {
    String client_id = "886460868094-fmvs3vvo0ocdgsto1thf5ochuq2aa3jc.apps.googleusercontent.com";
    String client_secret = "ihxw3iNTZWc-s7kUrTSREFDK";

    String baseURI = "https://www.googleapis.com/blogger/v3/blogs/";
    String apiKey = "AIzaSyDhurflllMQlg80YJZoC9EKG3qmvkxwKBY";
    String blogID = "54850391780151973";
    String username = "softtesteew382c@gmail.com";
    String password = "hLP8B%F5oYU8kEM";

    String access_token = "ya29.Glv2BbBCnKYnn6GTtwGTC8IM8Ddw08Of7gukVa1g87OI7bP1Ma9p2NVxxUl4TpNroK0aqSybHqKAELaS1rfn16fP9BrVChxOIv-mgPsvGq0HIHn8yWGwlZL9X6mv";

    //GET https://www.googleapis.com/blogger/v3/blogs/54850391780151973?key=AIzaSyDhurflllMQlg80YJZoC9EKG3qmvkxwKBY

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

        String postId = "5032169774910920020";
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

    /*{
        "access_token" : "ya29.Glv2BbBCnKYnn6GTtwGTC8IM8Ddw08Of7gukVa1g87OI7bP1Ma9p2NVxxUl4TpNroK0aqSybHqKAELaS1rfn16fP9BrVChxOIv-mgPsvGq0HIHn8yWGwlZL9X6mv",
            "expires_in" : 3600,
            "refresh_token" : "1/gA21Mu_2qciM9-z5CP88y4PhIiSXrQDZMVyuY3BpJP8",
            "scope" : "https://www.googleapis.com/auth/blogger",
            "token_type" : "Bearer"
    }
*/

    @Test public void addBlogPost() throws IOException {
        RestAssured.baseURI = baseURI + blogID + "/posts/";

        String json = "{\n" +
                " \"kind\": \"blogger#post\",\n" +
                " \"blog\": {\n" +
                "  \"id\": \"54850391780151973\"\n" +
                " },\n" +
                " \"title\": \"test\",\n" +
                " \"content\": \"some test\",\n" +
                "}";

        Response response =
                given()
                        .auth()
                        .oauth2(access_token)
                        //.param("key", apiKey)
                        //.param("access_token", access_token)
                        .param("kind", "blogger#post")
                        //.param("content_type", "application/json")
                        .param("title", "some random title")
                        .param("content", "test content 2")
                        .contentType("application/json")
                        .content(json)
                        .when()
                        .post()
                        .then()
                        //.assertThat()
                        //.statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
    }


    @Test
    public void deleteBlogPost() throws IOException {
        //https://www.googleapis.com/blogger/v3/blogs/blogId/posts/postId

        //String accessToken = authenticateUser(username, password);
        //String accessToken = requestAccessToken();
        String postId = "566850703217322142";
        RestAssured.baseURI = baseURI + blogID + "/posts/" + postId;

                given()
                        .param("key", apiKey)
                        .param("access_token", access_token)
                        //.auth()
                        //.oauth2("client_id")
                        .when()
                        .delete()
                        .then()
                        .assertThat()
                        .statusCode(204);
        //System.out.println(response.asString());
    }

    // we need to get the oauth token before we can perform the request
    private String authenticateUser(String username, String password) {

        String loginURI = baseURI + "login";
        RestAssured.baseURI =  "https://accounts.google.com/o/oauth2/token";

//https://accounts.google.com/o/oauth2/token?
// grant_type=authorization_code&
// code=4/NgA0wYsUx_1B1MITmatqR7BZAfbTF60kmwbxb2QzexFezhNNBIVSYjnh6zRe16ZAnRlHO0-MtqzpiGRUzUQwsGI#&
// client_id=886460868094-fmvs3vvo0ocdgsto1thf5ochuq2aa3jc.apps.googleusercontent.com&
// client_secret=ihxw3iNTZWc-s7kUrTSREFDK

        String code = "4/OABthcap_rWi0jt3KSYhkM-RrOE1JtLxJ0llsyRyi-tw5obJqs2m0G5iGiUBGUsRkXmlxMqBt2vHyp1KIlKKQvU";

        String response =
                given()
                        //.param("username", username)
                        //.param("password", password)
                        .param("grant_type", "authorization_code")
                        .param("code", code)
                        .param("client_id", client_id)
//                        .param("scope", "https://www.googleapis.com/auth/blogger.readonly")
                        //.param("scope", "https://www.googleapis.com/auth/drive")
                        .param("client_secret", client_secret)
                        //.param("response_type", "code")
                        .param("redirect_uri", "http://localhost:8080")
                        //.auth()
                        //.preemptive()
                        //.basic(username,password)
                        //.when()
                        .post()
                        .asString();

        JsonPath jsonPath = new JsonPath(response);
        System.out.println(response);
        String accessToken = jsonPath.getString("access_token");
        return accessToken;
    }

    String requestAccessToken() throws IOException {

        String code = "4/NgA0wYsUx_1B1MITmatqR7BZAfbTF60kmwbxb2QzexFezhNNBIVSYjnh6zRe16ZAnRlHO0-MtqzpiGRUzUQwsGI#";
        try {
            GoogleTokenResponse response =
                    //new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(), "{client_secret}",
                     //       "4/P7q7W91a-oMsCeLvIaQm6bTrgtp7", "https://oauth2-login-demo.appspot.com/code")
                      //      .execute();
                    new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(), client_id,
                            client_secret, "code", "https://oauth2-login-demo.appspot.com/code").execute();
            return response.getAccessToken();
            //System.out.println("Access token: " + response.getAccessToken());
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                System.err.println("Error: " + e.getDetails().getError());
                if (e.getDetails().getErrorDescription() != null) {
                    System.err.println(e.getDetails().getErrorDescription());
                }
                if (e.getDetails().getErrorUri() != null) {
                    System.err.println(e.getDetails().getErrorUri());
                }
            } else {
                System.err.println(e.getMessage());
            }
        }
        return "";
    }
}
