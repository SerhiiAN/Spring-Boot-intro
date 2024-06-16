package mate.academy.intro.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String author;
    @Column(nullable = false, unique = true)
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
