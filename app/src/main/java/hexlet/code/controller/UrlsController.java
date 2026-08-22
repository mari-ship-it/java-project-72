package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        UrlsPage page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        List<UrlCheck> checks = UrlCheckRepository.find(id);
        UrlPage page = new UrlPage(url,checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        String inputUrl = ctx.formParam("url");

        try {
            URI uri = URI.create(inputUrl);
            URL tempUrl = uri.toURL();

            String name = new StringBuilder()
                    .append(tempUrl.getProtocol())
                    .append("://")
                    .append(tempUrl.getHost())
                    .append(tempUrl.getPort() != -1 ? ":" + tempUrl.getPort() : "")
                    .toString();

            Optional<Url> foundUrl = UrlRepository.findByName(name);

            if (foundUrl.isPresent()) {
                Url url = foundUrl.get();
                ctx.sessionAttribute("flash", "Страница уже существует!");
                ctx.sessionAttribute("flashType", "info");
                ctx.redirect(NamedRoutes.urlPath(url.getId()));
            } else {
                Url newUrl = new Url(name);
                UrlRepository.save(newUrl);
                ctx.sessionAttribute("flash", "Страница успешно добавлена!");
                ctx.sessionAttribute("flashType", "success");
                ctx.redirect(NamedRoutes.urlPath(newUrl.getId()));
            }

        } catch (IllegalArgumentException | MalformedURLException e) {

            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }

    public static void checks(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("URL not found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            int statusCode = response.getStatus();
            String body = response.getBody();

            Document document = org.jsoup.Jsoup.parse(body);
            String title = document.title();
            title = (title == null || title.isBlank()) ? null : title;

            Element h1Element = document.selectFirst("h1");
            String h1 = (h1Element != null && !h1Element.text().isBlank()) ? h1Element.text() : null;

            Element metaDescription = document.selectFirst("meta[name=description]");
            String description = (metaDescription != null && !metaDescription.attr("content").isBlank())
                    ? metaDescription.attr("content")
                    : null;

            UrlCheck check = new UrlCheck();
            check.setUrlId(id);
            check.setStatusCode(statusCode);
            check.setTitle(title);
            check.setH1(h1);
            check.setDescription(description);
            UrlCheckRepository.save(check);
            ctx.sessionAttribute("flash", "Страница успешно проверена!");
            ctx.sessionAttribute("flashType", "success");

        } catch (Exception e) {
            log.error("Ошибка верификации URL: {}", e.getMessage(), e);
            ctx.sessionAttribute("flash", "Ошибка при проверке страницы");
            ctx.sessionAttribute("flashType", "danger");
        }
        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
