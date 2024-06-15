package mate.academy.intro.repository;

import mate.academy.intro.model.Book;
import org.hibernate.mapping.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
