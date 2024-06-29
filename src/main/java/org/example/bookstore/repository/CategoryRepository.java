package org.example.bookstore.repository;

import java.util.Set;
import org.example.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c.id FROM Category c WHERE c.id IN :ids")
    Set<Long> findExistingIdsByIdIn(@Param("ids") Set<Long> ids);
}
