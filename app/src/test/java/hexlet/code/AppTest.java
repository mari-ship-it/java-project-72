package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import io.javalin.testtools.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import java.util.List;

public class AppTest {

    private Javalin app;
    private TestConfig configWithRedirects;

    @BeforeEach
    public final void setUp() throws Exception {
        app = App.getApp();
        UrlCheckRepository.deleteAll();
        UrlRepository.deleteAll();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .followRedirects(true)
                .build();
        configWithRedirects = new TestConfig(true, true, okHttpClient);
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            String body = response.body().string();
            assertThat(body).contains("Анализатор страниц");
            assertThat(body).contains("Бесплатно проверяйте сайты на SEO пригодность");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Сайты");
        });
    }

    @Test
    public void testUrlPage() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            Url url = new Url("https://example.com");
            UrlRepository.save(url);
            Response response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(app, configWithRedirects, (server, client) -> {
            Response response = client.post(NamedRoutes.urlsPath(), "url=http://example.com");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com");
            var savedUrl = UrlRepository.findByName("http://example.com");
            assertThat(savedUrl).isPresent();
            assertThat(savedUrl.get().getName()).isEqualTo("http://example.com");
        });
    }

    @Test
    void testCreateDuplicateUrl() throws Exception {
        Url existingUrl = new Url("http://example.com");
        UrlRepository.save(existingUrl);
        JavalinTest.test(app, configWithRedirects, (server, client) -> {
            Response response = client.post(NamedRoutes.urlsPath(), "url=http://example.com");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com");
        });
    }

    @Test
    public void testCreateInvalidUrl() {
        JavalinTest.test(app, configWithRedirects, (server, client) -> {
            Response response = client.post(NamedRoutes.urlsPath(), "url=not-a-valid-url");
            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.getEntities()).isEmpty();
        });
    }

    @Test
    public void testRunUrlCheck() throws Exception {
        try (MockWebServer mockServer = new MockWebServer()) {
            String htmlBody = """
                    <html>
                        <head>
                            <title>Test Title</title>
                            <meta name="description" content="Test Description">
                        </head>
                        <body>
                            <h1>Test H1</h1>
                        </body>
                    </html>
                    """;

            MockResponse mockResponse = new MockResponse.Builder()
                    .body(htmlBody)
                    .code(200)
                    .build();
            mockServer.enqueue(mockResponse);
            mockServer.start();
            String mockUrlString = mockServer.url("").toString();
            Url url = new Url(mockUrlString);
            UrlRepository.save(url);
            JavalinTest.test(app, configWithRedirects, (server, client) -> {
                Response response = client.post(NamedRoutes.urlChecksPath(url.getId()));
                assertThat(response.code()).isEqualTo(200);

                String body = response.body().string();
                assertThat(body).contains("data-test=\"checks\"");
                assertThat(body).contains("200");
                assertThat(body).contains("Test Title");
                assertThat(body).contains("Test H1");
                assertThat(body).contains("Test Description");

                List<UrlCheck> checks = UrlCheckRepository.find(url.getId());
                assertThat(checks).hasSize(1);

                UrlCheck actualCheck = checks.get(0);
                assertThat(actualCheck.getStatusCode()).isEqualTo(200);
                assertThat(actualCheck.getTitle()).isEqualTo("Test Title");
                assertThat(actualCheck.getH1()).isEqualTo("Test H1");
                assertThat(actualCheck.getDescription()).isEqualTo("Test Description");
            });
        }
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPath(999999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }
}
