import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.model.Post;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class PostBlog {

    private static final String APPLICATION_NAME = "Blogger Poster";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/blogger");
    private static final String CREDENTIALS_FILE_PATH = "client_secret_224579602995-3s2cpn0flv2tbv6mis27tmrhhmos4c01.apps.googleusercontent.com.json";

    public static Blogger getBloggerService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new FileReader(CREDENTIALS_FILE_PATH));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        com.google.api.client.auth.oauth2.Credential credential = new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(
                flow, new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver())
                .authorize("psv.bec@gmail.com");

        return new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) {
        try {
            Blogger service = getBloggerService();

            // Replace this with your blog ID (get from Blogger.com > Settings > Blog ID)
            String blogId = "8853728855810407613";

            // Example posts
            String[][] posts = {
                {"Post Title 1", "This is the body of post 1"},
                {"Post Title 2", "This is the body of post 2"}
            };

            for (int i = 0; i < posts.length; i++) {
                Post post = new Post();
                post.setTitle(posts[i][0]);
                post.setContent(posts[i][1]);

                post = service.posts().insert(blogId, post).execute();
                System.out.println("Post published: " + post.getUrl());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
