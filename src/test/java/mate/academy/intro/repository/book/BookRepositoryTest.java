package mate.academy.intro.repository.book;

import java.util.List;
import mate.academy.intro.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = "classpath:database/add-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/delete-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find all books by category id")
    void findBooksByCategoryId_ValidCategoryId_ShouldReturnListOfBooks() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 30);
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId,pageable);

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("Author 1", actual.get(0).getAuthor());
        Assertions.assertEquals("Author 4", actual.get(1).getAuthor());
    }
}
