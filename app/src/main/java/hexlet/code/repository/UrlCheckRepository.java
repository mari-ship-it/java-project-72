package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {

    public static void save(UrlCheck check) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, check.getUrlId());
            stmt.setInt(2, check.getStatusCode());
            stmt.setString(3, check.getTitle());
            stmt.setString(4, check.getH1());
            stmt.setString(5, check.getDescription());
            stmt.executeUpdate();
        }
    }

    public static List<UrlCheck> find(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        List<UrlCheck> urlChecks = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, urlId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                UrlCheck urlCheck = new UrlCheck();
                urlCheck.setId(resultSet.getLong("id"));
                urlCheck.setUrlId(resultSet.getLong("url_id"));
                urlCheck.setStatusCode(resultSet.getInt("status_code"));
                urlCheck.setTitle(resultSet.getString("title"));
                urlCheck.setH1(resultSet.getString("h1"));
                urlCheck.setDescription(resultSet.getString("description"));
                urlCheck.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                urlChecks.add(urlCheck);
            }
        }
        return urlChecks;
    }

    public static void deleteAll() {
        UrlRepository.deleteAll();
    }
}
