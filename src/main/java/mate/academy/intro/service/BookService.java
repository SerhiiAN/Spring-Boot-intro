package mate.academy.intro.service;

import mate.academy.intro.model.Book;
import org.hibernate.mapping.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
