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

        <section class="container">

            <div class="">
                <h1 class="mt-3 fw-light">Сайты</h1>
                <table class="table table-bordered mt-4" data-test="urls">
                    <thead>
                    <tr>
                        <th scope="col" class="fw-bold col-1">ID</th>
                        <th scope="col" class="fw-bold">Название</th>
                        <th scope="col" class="fw-bold col-2">Последняя проверка</th>
                        <th scope="col" class="fw-bold col-1">Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var url: page.getUrls())
                        <tr>
                            <td>${url.getId()}</td>
                            <td><a href="/urls/${url.getId()}">
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

        </section>
    </main>
`)
