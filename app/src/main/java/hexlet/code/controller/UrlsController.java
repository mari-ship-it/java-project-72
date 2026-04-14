package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
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

import static io.javalin.rendering.template.TemplateUtil.model;

@Slf4j
public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        UrlsPage page = new UrlsPage(urls);

        String flashMessage = ctx.sessionAttribute("flash");
        if (flashMessage != null) {
            page.setFlash(flashMessage);
            ctx.sessionAttribute("flash", null);
        }
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        List<UrlCheck> check = UrlCheckRepository.find(id);
        String flashMessage = ctx.consumeSessionAttribute("flash");
        UrlPage page = new UrlPage(url,check);

        if (flashMessage != null) {
            page.setFlash(flashMessage);
        }
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

            if (UrlRepository.search(name)) {
                ctx.sessionAttribute("flash", "Страница уже существует!");
            } else {
                Url url = new Url(name);
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена!");
            }
            ctx.redirect("/urls");

        } catch (IllegalArgumentException | MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect("/");
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
            String h1 = null;
            String description = null;

            Element h1Element;
            h1Element = document.selectFirst("h1");

            if (h1Element != null) {
                h1 = h1Element.text();
            }

            Element meta = document.selectFirst("meta[name=description]");
            if (meta != null) {
                description = meta.attr("content");
            }

            UrlCheck check = new UrlCheck();
            check.setUrlId(id);
            check.setStatusCode(statusCode);
            check.setTitle(title.isEmpty() ? null : title);
            check.setH1(h1);
            check.setDescription(description);

            UrlCheckRepository.save(check);

            ctx.sessionAttribute("flash", "Страница успешно проверена");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Ошибка при проверке страницы");
        }
        String referer = ctx.header("Referer");
        if (referer != null && !referer.isEmpty()) {
            ctx.redirect(referer);
        } else {
            ctx.redirect("/urls");
        }
    }
}
