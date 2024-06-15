package mate.academy.intro;

import java.math.BigDecimal;
import mate.academy.intro.model.Book;
import mate.academy.intro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntroApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Spring tutorial");
                book.setAuthor("Me");
                book.setIsbn("idk what it is");
                book.setPrice(BigDecimal.TEN);
                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }

}
