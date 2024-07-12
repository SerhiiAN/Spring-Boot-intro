package mate.academy.intro.repository.category;

import java.util.List;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Book> findAllByCategoryId(Long categoryId);

}
