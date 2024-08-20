package org.arvind.commerce.api.repo;

import org.arvind.commerce.api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i.name, sum(i.quantity) FROM Item i where i.createdAt <= :endDate and i.createdAt >= :startDate and i.ordered = :ordered " +
            "GROUP BY i.name ORDER BY sum(i.quantity) DESC LIMIT :limit")
    List<Object[]> findTopSellingProductForADateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                        @Param("ordered") boolean ordered, @Param("limit") int limit);

    @Query("SELECT i.name, sum(i.quantity) FROM Item i where i.createdAt <= :endDate and i.createdAt >= :startDate and i.ordered = :ordered " +
            "GROUP BY i.name ORDER BY sum(i.quantity) ASC LIMIT :limit")
    List<Object[]> findBottomSellingProductForADateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                                      @Param("ordered") boolean ordered, @Param("limit") int limit);

    @Query("SELECT i.name, cast(i.createdAt as LocalDate) as soldAt, sum(i.quantity) FROM Item i where i.createdAt <= :endDate and i.createdAt >= :startDate and i.ordered = true " +
            "GROUP BY i.name, cast(i.createdAt as LocalDate) ORDER BY cast(i.createdAt as LocalDate) ASC")
    List<Object[]> findProductQuantityForADateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
