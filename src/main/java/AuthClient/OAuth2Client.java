package AuthClient;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

public class OAuth2Client {

    private static final String DATA_STORE_DIR = "credentials.store";
    private static final String SCOPE = "https://www.googleapis.com/auth/blogger";

    private static DataStoreFactory storeFactory;

    private static String accessToken = null;

    private static String CREDENTIAL_ID = "soft-test-user";

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static GoogleAuthorizationCodeFlow initializeFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY,
                OAuth2ClientCredentials.CLIENT_ID, OAuth2ClientCredentials.CLIENT_SECRET,
                Collections.singleton(SCOPE)).setDataStoreFactory(getStoreFactory()).build();
    }

    private static DataStoreFactory getStoreFactory() {
        if (storeFactory == null) {
            try {
                File credFile = new File(DATA_STORE_DIR);
                storeFactory = new FileDataStoreFactory(credFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return storeFactory;
    }

    // Based on: https://gitlab.tubit.tu-berlin.de/klems/googledrivejavaexample/blob/5688e4bc907424036a5b9857f33d3bd50b51b29e/src/com/google/drive/example/DriveCommandLine.java
    public static void authorize() {
        GoogleAuthorizationCodeFlow flow = null;
        try {
            flow = initializeFlow();
            Credential c = flow.loadCredential(CREDENTIAL_ID);

            if (c != null) {
                accessToken = c.getAccessToken();
                System.out.println("Valid credentials stored. No need to re-authorize.");
                System.out.printf("You can also delete the file %s to force a re-auth.\n", DATA_STORE_DIR);
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = flow.newAuthorizationUrl().setRedirectUri(OAuth2ClientCredentials.COPY_PASTE_REDIRECT_URI).build();
        System.out.println("Please open the following url in your browser:");
        System.out.println(url);
        System.out.println("Then type the authorization code:");

        Scanner input = new Scanner(System.in);
        String code = input.nextLine();

        GoogleTokenResponse response = null;
        try {
            response = flow.newTokenRequest(code).setRedirectUri(OAuth2ClientCredentials.COPY_PASTE_REDIRECT_URI).execute();
            System.out.println("Authenticated correctly! :D");
            System.out.println("Saving credentials to disk...");
            Credential c = flow.createAndStoreCredential(response, CREDENTIAL_ID);
            accessToken = c.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAccessToken() {
        if (accessToken == null) {
            authorize();
        }
        return accessToken;
    }

    public static void main(String[] args) {
        // Run this to get the access_token and refresh_token.
        // Follow instructions on console.
        OAuth2Client.authorize();
    }
}
