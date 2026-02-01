@import hexlet.code.dto.UrlsPage;

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Анализатор страниц</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<%--  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>--%>
</head>

<body>

  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">

      <a class="navbar-brand" href="#">Анализатор страниц</a>

      <div class="navbar-collapse">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link" href="#">Главная</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Сайты</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <main class="navbar navbar-dark bg-dark">
    <div class="container">
      <div class="col align-self-center mt-5 mb-5">

        <div class="text-light">
            <h1 class="display-3">Анализатор страниц</h1>
            <p>Бесплатно проверяйте сайты на SEO пригодность</p>
        </div>

        <div class="container-fluid p-0">
          <form class="d-flex gap-2 justify-content-center" action="/urls" method="get">
            <div class="form-floating flex-grow-1">
              <input type="url" class="form-control" id="floatingInput" placeholder="https://example.com" required name="term" />
              <input type="submit" value="Search" />
              <label for="floatingInput">Ссылка</label>
            </div>
            <button type="submit" class="btn btn-primary  btn-lg">Проверить</button>
          </form>
        </div>

        <div class="mt-2 text-muted">
          Пример: https://www.example.com
        </div>
      </div>
    </div>
  </main>

</body>
</html>
