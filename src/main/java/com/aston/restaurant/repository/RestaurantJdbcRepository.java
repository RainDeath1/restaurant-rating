package com.aston.restaurant.repository;

import com.aston.restaurant.dto.RestaurantResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RestaurantJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RestaurantResponse> findByCityOrderByRating(Long cityId) {

        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.city_id,
                    c.name AS city_name,
                    r.average_rating
                FROM restaurants r
                JOIN cities c ON r.city_id = c.id
                WHERE r.city_id = ?
                ORDER BY r.average_rating DESC, r.id ASC
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new RestaurantResponse(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("city_id"),
                        rs.getString("city_name"),
                        rs.getDouble("average_rating")
                ),
                cityId
        );
    }
}
