package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, url.getName());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        String sql = """
        SELECT
            u.id,
            u.name,
            c.status_code,
            c.created_at AS last_check
        FROM urls u
        LEFT JOIN url_checks c
            ON c.url_id = u.id
           AND c.created_at = (
                SELECT MAX(created_at)
                FROM url_checks
                WHERE url_id = u.id
           )
        ORDER BY u.id DESC;
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            var result = new ArrayList<Url>();
            while (rs.next()) {
                Url url = new Url(rs.getString("name"));
                url.setId(rs.getLong("id"));
                var timestamp = rs.getTimestamp("last_check");
                url.setLastCheck(timestamp != null ? timestamp.toLocalDateTime() : null);
                url.setStatusCode(rs.getInt("status_code"));
                result.add(url);
            }
            return result;
        }
    }
    public static boolean search(String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String inputName = resultSet.getString("name");
                return inputName.equals(name);
            }
            return false;
        }
    }
    public static void deleteAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM url_checks");
            statement.executeUpdate("DELETE FROM urls");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке базы данных", e);
        }
    }
}
