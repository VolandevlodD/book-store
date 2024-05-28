package org.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    public static final String DELETE_DATA_FROM_DB = "classpath:sql/delete-data-from-db.sql";
    public static final String INSERT_DATA_INTO_DB = "classpath:sql/insert-data-into-db.sql";
    private static final Long CATEGORY_ID = 1L;
    private static final Long NON_EXISTENT_CATEGORY_ID = 999L;
    private static final Set<Long> CATEGORY_IDS = Set.of(CATEGORY_ID, NON_EXISTENT_CATEGORY_ID);

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find existing category IDs by a set of IDs")
    @Sql(scripts = {DELETE_DATA_FROM_DB, INSERT_DATA_INTO_DB},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findExistingIdsByIdIn() {
        Set<Long> existingIds = categoryRepository.findExistingIdsByIdIn(CATEGORY_IDS);

        assertNotNull(existingIds);
        assertTrue(existingIds.contains(CATEGORY_ID));
        assertFalse(existingIds.contains(NON_EXISTENT_CATEGORY_ID));
    }
}
