import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import main.java.AuthClient.OAuth2Client;
import main.java.AuthClient.OAuth2ClientCredentials;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by tiffanytillett on 7/31/18.
 */

public class BloggerTester {

    String baseURI = "https://www.googleapis.com/blogger/v3/blogs/";
    String baseUserURI = "https://www.googleapis.com/blogger/v3/users/";
    String apiKey = "AIzaSyDhurflllMQlg80YJZoC9EKG3qmvkxwKBY";
    String blogID = "54850391780151973";
    final String SCOPE = "https://www.googleapis.com/auth/blogger";

    String getPostId = "6662126603753950905";
    String postIDToDelete = "6662126603753950905";
    String updatePostId = "6662126603753950905";

    //GET https://www.googleapis.com/blogger/v3/blogs/54850391780151973?key=AIzaSyDhurflllMQlg80YJZoC9EKG3qmvkxwKBY

    /**
     * https://www.googleapis.com/blogger/v3/users/userId
     * https://www.googleapis.com/blogger/v3/users/self
     * https://www.googleapis.com/blogger/v3/users/userId/blogs
     * https://www.googleapis.com/blogger/v3/users/self/blogs
     * https://www.googleapis.com/blogger/v3/blogs/blogId
     * https://www.googleapis.com/blogger/v3/blogs/byurl
     * https://www.googleapis.com/blogger/v3/blogs/blogId/posts
     * https://www.googleapis.com/blogger/v3/blogs/blogId/posts/bypath
     * https://www.googleapis.com/blogger/v3/blogs/blogId/posts/search
     * https://www.googleapis.com/blogger/v3/blogs/blogId/posts/postId
     * https://www.googleapis.com/blogger/v3/blogs/blogId/posts/postId/comments
     * https://www.googleapis.com/blogger/v3/blogs/blogId/posts/postId/comments/commentId
     * https://www.googleapis.com/blogger/v3/blogs/blogId/pages
     * https://www.googleapis.com/blogger/v3/blogs/blogId/pages/pageId
     */

    @Test
    public void getUserBlogs() {
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
        System.out.println(response.asString());
    }

    @Test
    public void getBlogID() {
        RestAssured.baseURI = baseURI;
        given()
                .param("key", apiKey)
                .when()
                .get(blogID)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(blogID));
    }

    @Test
    public void getBlogIDResponse() {
        RestAssured.baseURI = baseURI;

        Response response =
                given()
                        .param("key", apiKey)
                        .when()
                        .get(blogID)
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .body("id", equalTo(blogID))
                        .extract().response();
        System.out.println(response.body().asString());

    }

    @Test
    public void getBlogPostByID() {
        RestAssured.baseURI = baseURI + blogID + "/posts/" + getPostId;

        given()
                .param("key", apiKey)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(getPostId));
        System.out.println();
    }

    @Test
    public void getBlogPostResponse() {

        RestAssured.baseURI = baseURI + blogID + "/posts/" + getPostId;

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
    public void createBlogPost() {
        OAuth2Client.authorize();

        RestAssured.baseURI = baseURI + blogID + "/posts/";

        Map<String, String> blog = new HashMap<>();
        blog.put("id", blogID);

        Map<String, String> content = new HashMap<>();
        content.put("kind", "blogger#post");
        content.put("blog", blog.toString());
        content.put("title", "Tiffany Test");
        content.put("content", "test meee");
        Gson gson = new Gson();
        String json = gson.toJson(content);

        given()
                .auth()
                .oauth2(OAuth2ClientCredentials.AccessToken)
                .contentType("application/json")
                .body(json)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void deleteBlogPost() {
        /**
         * Does not behave as Google documentation indicates
         * Actually returns a 204 instead of 200
         */

        OAuth2Client.authorize();

        RestAssured.baseURI = baseURI + blogID + "/posts/" + postIDToDelete;

        given()
                .auth()
                .oauth2(OAuth2ClientCredentials.AccessToken)
                .when()
                .delete()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void updateBlogPost() {

        OAuth2Client.authorize();

        RestAssured.baseURI = baseURI + blogID + "/posts/" + updatePostId;

        // must provide both title and content with put. otherwise, the one I don't provide will be empty
        // error prone
        Map<String, String> content = new HashMap<>();
        content.put("title", "Tiffany Test upated");
        content.put("content", "updated");
        Gson gson = new Gson();
        String json = gson.toJson(content);

        RestAssured.baseURI = baseURI + blogID + "/posts/" + updatePostId;


        Response response =
                given()
                .auth()
                .oauth2(OAuth2ClientCredentials.AccessToken)
                .contentType("application/json")
                .body(json)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(200).extract().response();
        System.out.println(response.asString());

    }

    @Test
    public void patchBlogPost() {
        /**
         * Will update the JSON key specified when using PATCH verb
         */
        RestAssured.baseURI = baseURI + blogID + "/posts/" + updatePostId;
        OAuth2Client.authorize();

        Map<String, String> content = new HashMap<>();
        content.put("content", "Patching this post");
//        content.put("author.displayName", "Samuel L Jackson");
        Gson gson = new Gson();
        String json = gson.toJson(content);

        given()
                .auth()
                .oauth2(OAuth2ClientCredentials.AccessToken)
                .contentType("application/json")
                .body(json)
                .when()
                .patch()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void getPostComments() {
        OAuth2Client.authorize();

        RestAssured.baseURI = baseURI + blogID + "/posts/" + getPostId + "/comments";

        Response response =
                given()
                        .auth()
                        .oauth2(OAuth2ClientCredentials.AccessToken)
                        .contentType("application/json")
                        .when()
                        .get()
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract().response();
        System.out.println(response.asString());
    }

    @Test
    public void getBlogPostBySearch() {
        RestAssured.baseURI = baseURI + blogID + "posts/search";

        given()
                .queryParam("q", "Reese Test")
                .param("key", apiKey)
//                .auth()
//                .oauth2(access_token)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200);
    }




//    @Test public void updateBlogPost() throws IOException {
//
//        OAuth2Client.authorize();
//        RestAssured.baseURI = baseURI + blogID + "/posts/" + postId;
//
//        Map<String, String> blog = new HashMap<>();
//        blog.put("id","54850391780151973");
//
//        Map<String, String> content = new HashMap<>();
//        content.put("kind", "blogger#post");
//        content.put("id", postId);
//        content.put("blog", blog.toString());
//        content.put("url", "https://utsummersoftwaretesting.blogspot.com/2018/08/some-title2.html");
//        content.put("selflink", "https://www.googleapis.com/blogger/v3/blogs/54850391780151973/posts/6777269951563715084");
//        content.put("title", "some title2");
//        content.put("content", "some updated content");
//        Gson gson = new Gson();
//        String json = gson.toJson(content);
//
//        Response response =
//                given()
//                        .auth()
//                        .oauth2(OAuth2ClientCredentials.AccessToken)
//                        .contentType("application/json")
//                        .body(json)
//                        .when()
//                        .put()
//                        .then()
//                        .assertThat()
//                        .statusCode(200)
//                        .extract().response();
//        System.out.println(response.asString());
//    }





}
