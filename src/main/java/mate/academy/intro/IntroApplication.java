package mate.academy.intro;

import mate.academy.intro.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntroApplication {
    private BookRepository bookRepository;



    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return null;
    }
}
