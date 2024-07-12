package mate.academy.intro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Table(name = "categories")
@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE categories SET is_deleted = TRUE WHERE id=?")
@Where(clause = "is_deleted=FALSE")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false,
            columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
    @ManyToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();
}
