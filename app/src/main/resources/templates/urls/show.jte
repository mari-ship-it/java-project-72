@import hexlet.code.dto.UrlPage
@import java.time.format.DateTimeFormatter
@param UrlPage page

@template.layout.page(
content = @`

    <main class="flex-grow-1">

        @if(page != null && page.getFlash() != null)

            @if(page.getFlash().contains("добавлена") || page.getFlash().contains("проверена"))
                <div class="rounded-0 m-0 alert alert-dismissible fade show alert-success" role="alert">
                    <p class="m-0">${page.getFlash()}</p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            @elseif(page.getFlash().contains("существует"))
                <div class="rounded-0 m-0 alert alert-dismissible fade show alert-info" role="alert">
                    <p class="m-0">${page.getFlash()}</p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            @else
                <div class="rounded-0 m-0 alert alert-dismissible fade show alert-danger" role="alert">
                    <p class="m-0">${page.getFlash()}</p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            @endif
        @endif

        <section>
            <div class="container-lg bg-light mt-5">
                <h1 class="mb-3 fw-light">Сайт: ${page.getUrl().getName()}</h1>

                <table class="table mt-3 align-middle">
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
                    <form method="post" action="/urls/${page.getUrl().getId()}/checks" class="m-0">
                        <button type="submit" class="btn btn-primary rounded-pill">Запустить проверку</button>
                    </form>
                </div>

                <table class="table table-striped table-hover align-middle mb-0 mt-3">
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
`)