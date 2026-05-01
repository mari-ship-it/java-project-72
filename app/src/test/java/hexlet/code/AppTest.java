package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {

    private Javalin app;

    @BeforeEach
    public final void setUp() throws Exception {
        app = App.getApp();
        UrlCheckRepository.deleteAll();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Бесплатно проверяйте сайты на SEO пригодность");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url("https://www.example.com");
            UrlRepository.save(url);
            Response response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat((response.body()).string()).contains("https://www.example.com");
        });
    }

    @Test
    public void testCreateUrls() {
        JavalinTest.test(app, (server, client) -> {
            String requestBody = "url=https://www.example.com";
            Response response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

//    @Test
//    void testRunUrlCheck() throws Exception {
//        JavalinTest.test(app, (server, client) -> {
//            Url url = new Url("https://www.example.com");
//            UrlRepository.save(url);
//
//            Response response = client.post("/urls/" + url.getId() + "/checks");
//            assertThat(response.code()).isIn(302, 303);
//
//            String redirectLocation = response.header("Location");
//            Request getRequest = new Request.Builder()
//                    .url(server.getURL() + redirectLocation)
//                    .build();
//
//            Response getResponse = client.newCall(getRequest).execute();
//            assertThat(getResponse.code()).isEqualTo(200);
//        });
//    }

    @Test
    void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    void testUrlCheckNotFound() {
        JavalinTest.test(app, (server, client) -> {
                Response response = client.get("/urls/999999/checks");
                assertThat(response.code()).isEqualTo(404);
        });
    }

}
