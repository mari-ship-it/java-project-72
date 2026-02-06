package hexlet.code.controller;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

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
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException{
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
            ctx.redirect(NamedRoutes.urlsPath());

        } catch (IllegalArgumentException | MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect("/");
        }
    }
}
