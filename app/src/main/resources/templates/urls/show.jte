@import hexlet.code.dto.UrlPage
@import java.time.format.DateTimeFormatter
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
page = page,
content = @`

    <main class="flex-grow-1">
        <section>
            <div class="container-lg bg-light mt-5">
                <h1 class="mb-3 fw-light">Сайт: ${page.getUrl().getName()}</h1>

                <table class="table mt-3 align-middle" data-test="url">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>
                            ${page.getUrl().getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}
                        </td>
                    </tr>
                    </tbody>
                </table>

                <div class="d-flex justify-content-between align-items-center mt-5 mb-3">
                    <h2 class="mb-0 fw-light">Проверки</h2>
                    <form method="post" action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" class="m-0">
                        <button type="submit" class="btn btn-primary rounded-pill">Запустить проверку</button>
                    </form>
                </div>

                <table class="table table-striped table-hover align-middle mb-0 mt-3" data-test="checks">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th class="col-1">Код ответа</th>
                        <th>title</th>
                        <th>h1</th>
                        <th>description</th>
                        <th class="col-2">Дата проверки</th>
                    </tr>
                    </thead>

                    <tbody>
                    @for(var check : page.getChecks())
                        <tr>
                            <td>${check.getId()}</td>
                            <td>${check.getStatusCode()}</td>
                            <td>${check.getTitle()}</td>
                            <td>${check.getH1()}</td>
                            <td>${check.getDescription()}</td>
                            <td>
                                ${check.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}
                            </td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </section>
    </main>
`
)
