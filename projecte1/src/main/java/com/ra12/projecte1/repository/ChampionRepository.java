package com.ra12.projecte1.repository;

import com.ra12.projecte1.model.Champion;
import com.ra12.projecte1.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

@Repository
public class ChampionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Convierte filas SQL en objetos Champion
    private final class ChampionRowMapper implements RowMapper<Champion> {
        @Override
        public Champion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Champion champion = new Champion();

            champion.setId(rs.getLong("id"));
            champion.setName(rs.getString("name"));
            champion.setTitle(rs.getString("title"));

            // String -> Enum
            champion.setRole(Role.valueOf(rs.getString("role")));

            champion.setDifficulty(rs.getString("difficulty"));

            Date releaseDate = rs.getDate("release_date");
            if (releaseDate != null) {
                champion.setReleaseDate(releaseDate.toLocalDate());
            }

            champion.setImageUrl(rs.getString("image_url"));
            champion.setBio(rs.getString("bio"));

            return champion;
        }
    }

    // Obtener todos los champions
    public List<Champion> findAll() {
        return jdbcTemplate.query("SELECT * FROM champions", new ChampionRowMapper());
    }

    // Buscar por id
    public Champion findById(long id) {
        String sql = "SELECT * FROM champions WHERE id = ?";
        List<Champion> result = jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                new ChampionRowMapper()
        );
        return result.isEmpty() ? null : result.get(0);
    }

    // Guardar nuevo champion
    public void save(Champion champion) {
        String sql = """
            INSERT INTO champions
            (name, title, role, difficulty, release_date, image_url, bio)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                champion.getName(),
                champion.getTitle(),
                champion.getRole().name(),
                champion.getDifficulty(),
                champion.getReleaseDate(),
                champion.getImageUrl(),
                champion.getBio()
        );
    }

    // Actualizar champion existente
    public void update(Champion champion) {
        String sql = """
            UPDATE champions
            SET name = ?,
                title = ?,
                role = ?,
                difficulty = ?,
                release_date = ?,
                image_url = ?,
                bio = ?
            WHERE id = ?
        """;

        jdbcTemplate.update(
                sql,
                champion.getName(),
                champion.getTitle(),
                champion.getRole().name(),
                champion.getDifficulty(),
                champion.getReleaseDate(),
                champion.getImageUrl(),
                champion.getBio(),
                champion.getId()
        );
    }

    // [a2] Actualizar solo la imagen
    public void updateImage(long id, String imageUrl) {
        String sql = "UPDATE champions SET image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, imageUrl, id);
    }

    // [d2] Eliminar por id
    public boolean deleteById(long id) {
        String sql = "DELETE FROM champions WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    // [c2] Eliminar TODOS
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM champions");
    }
}
