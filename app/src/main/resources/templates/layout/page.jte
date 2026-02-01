@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Анализатор страниц</title>

  <link rel="stylesheet" href="bootstrap/css/bootstrap-grid.min.css">
  <link rel="stylesheet" href="bootstrap/css/bootstrap-utilities.min.css">
</head>
<body>
 <!-- Основная разметка страницы -->
    <p>
        <a href="${NamedRoutes.urlsPath()}">Urls</a>
    </p>
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif
    ${content}
</body>
</html>