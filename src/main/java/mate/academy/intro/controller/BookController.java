package mate.academy.intro.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;
import mate.academy.intro.model.Book;
import mate.academy.intro.service.BookService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @PostMapping
    public Book save(@RequestBody Book book) {
        return bookService.save(book);
    }
}
