
package com.aston.restaurant.repository;

import com.aston.restaurant.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByRestaurant_Id(Long restaurantId);

    @Query("""
            SELECT AVG(v.rating)
            FROM Vote v
            WHERE v.restaurant.id = :restaurantId
            """)
    Double findAverageRatingByRestaurantId(
            @Param("restaurantId") Long restaurantId
    );
}
