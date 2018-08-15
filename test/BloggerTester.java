import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import AuthClient.OAuth2Client;
import AuthClient.OAuth2ClientCredentials;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    String access_token = "ya29.Glv5BTvWnApeoBb_R5bzjF49JDEyBWSEabp19LoucogklcBRdtUuPVx5UbQ4-F_Q8H8iYDgRYrd3ln03jN0iMEUAFiQuYSVNiH6mkrLY82haa9CgJmTq1SqVHFxf";
    final String SCOPE = "https://www.googleapis.com/auth/blogger";

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
        //https://www.googleapis.com/blogger/v3/blogs/54850391780151973/posts/5223310518537267936


        String postId = "5223310518537267936";
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

       /* String json = "{\n" +
                " \"kind\": \"blogger#post\",\n" +
                " \"blog\": {\n" +
                "  \"id\": \"54850391780151973\"\n" +
                " },\n" +
                " \"title\": \"test\",\n" +
                " \"content\": \"some test\",\n" +
                "}";*/


        Map<String, String> blog = new HashMap<>();
        blog.put("id","54850391780151973");

        Map<String, String> content = new HashMap<>();
        content.put("kind", "blogger#post");
        content.put("blog", blog.toString());
        content.put("title", "some title2");
        content.put("content", "some test content2");
        Gson gson = new Gson();
        String json = gson.toJson(content);

        //String token = authenticateUser(username, password);
       /* given()
                .auth()
                .oauth2(access_token)
                .contentType("application/json")
                .body(json)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(200);*/

        Response response =
                given()
                        .auth()
                        .oauth2(access_token)
                        .contentType("application/json")
                        .body(json)
                        .when()
                        .post()
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
    }

    @Test public void updateBlogPost() throws IOException {
        String postId = "6777269951563715084";

        RestAssured.baseURI = baseURI + blogID + "/posts/" + postId;

        Map<String, String> blog = new HashMap<>();
        blog.put("id","54850391780151973");

        Map<String, String> content = new HashMap<>();
        content.put("kind", "blogger#post");
        content.put("id", postId);
        content.put("blog", blog.toString());
        content.put("url", "https://utsummersoftwaretesting.blogspot.com/2018/08/some-title2.html");
        content.put("selflink", "https://www.googleapis.com/blogger/v3/blogs/54850391780151973/posts/6777269951563715084");
        content.put("title", "some title2");
        content.put("content", "some updated content");
        Gson gson = new Gson();
        String json = gson.toJson(content);

        Response response =
                given()
                        .auth()
                        .oauth2(access_token)
                        .contentType("application/json")
                        .body(json)
                        .when()
                        .put()
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
    }


    @Test
    public void deleteBlogPost() throws IOException {
        //https://www.googleapis.com/blogger/v3/blogs/blogId/posts/postId

        //String accessToken = authenticateUser(username, password);
        //String accessToken = requestAccessToken();
        String postId = "7717003079704783155";
        RestAssured.baseURI = baseURI + blogID + "/posts/" + postId;

                given()
                        //.param("key", apiKey)
                        //.param("access_token", access_token)
                        .auth()
                        .oauth2(access_token)
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

        String code = "4/OwD6k8kHgQxc_UfONQql3VQCXGOVpNcsKJ5Fb_-dAmd9Ii2v0GtRObfo0NYdLK6ue90pQEDPPORfprUQSLLrXgI";

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
        access_token = jsonPath.getString("access_token");
        return access_token;
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
