@import hexlet.code.dto.UrlsPage
@param UrlsPage page

@template.layout.page(
content = @`
    <main class="flex-grow-1">

        @if(page != null && page.getFlash() != null)
            @if(page.getFlash().contains("добавлена"))
            <div class="rounded-0 m-0 alert alert-dismissible fade show alert-success" role="alert">
                <p class="m-0">${page.getFlash()}</p>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            @else
                <div class="rounded-0 m-0 alert alert-dismissible fade show alert-info" role="alert">
                    <p class="m-0">${page.getFlash()}</p>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            @endif
        @endif

        <section>
            <div class="container-lg mt-5 p-4 bg-light rounded shadow-sm">
                <h1 class="mb-3 fw-light">Сайты</h1>
                <div class="table-responsive">
                    <table class="table table-striped table-hover mb-0">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var url: page.getUrls())
                        <tr>
                            <td>${url.getId()}</td>
                            <td><a href="/urls/${url.getId()}" class="text-decoration-none text-dark">
                                    ${url.getName()}</a></td>
                            <td>
                                @if(url.getLastCheck() != null)
                                    ${url.getLastCheck().format(java.time.format.DateTimeFormatter.
                                        ofPattern("dd/MM/yyyy HH:mm"))}
                                @endif
                            </td>
                            <td>
                                @if(url.getLastCheck() != null)
                                    ${url.getStatusCode()}
                                @endif
                            </td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
                </div>
            </div>
        </section>
    </main>
`)
