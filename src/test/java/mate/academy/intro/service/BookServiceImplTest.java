package mate.academy.intro.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.book.BookSearchParameters;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.book.BookRepository;
import mate.academy.intro.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto requestDto;
    private Book bookOne;
    private Book bookTwo;
    private BookDto bookDtoTwo;
    private BookDto bookDtoOne;
    private final Set<Long> categoryIds = new HashSet<>();
    private Category categoryOne;
    private Category categoryTwo;
    private BookSearchParameters bookSearchParameters;
    private Pageable pageable;
    private Long secondId;
    private Long firstId;

    @BeforeEach
    void setUp() {
        firstId = 1L;
        secondId = 2L;

        requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book Title 1");
        requestDto.setAuthor("Author 1");
        requestDto.setIsbn("9780987654321");
        requestDto.setPrice(new BigDecimal("19.99"));
        requestDto.setCoverImage("cover1.jpg");
        categoryIds.add(firstId);
        categoryIds.add(secondId);
        requestDto.setCategoryIds(categoryIds);

        bookOne = new Book();
        bookOne.setId(firstId);
        bookOne.setTitle(requestDto.getTitle());
        bookOne.setAuthor(requestDto.getAuthor());
        bookOne.setIsbn(requestDto.getIsbn());
        bookOne.setPrice(requestDto.getPrice());
        bookOne.setCoverImage(requestDto.getCoverImage());

        categoryOne = new Category();
        categoryOne.setId(firstId);
        categoryOne.setName("Text book");
        categoryOne.setDescription("Text book");
        categoryTwo = new Category();
        categoryTwo.setId(secondId);
        categoryTwo.setName("Baby book");
        categoryTwo.setDescription("Baby book");
        bookOne.setCategories(Set.of(categoryOne, categoryTwo));

        bookTwo = new Book();
        bookTwo.setId(secondId);
        bookTwo.setTitle("Sample Book 2");
        bookTwo.setAuthor("Author B");
        bookTwo.setIsbn("9780987654322");
        bookTwo.setPrice(BigDecimal.valueOf(29.99));
        bookTwo.setDescription("Sample book description 2");
        bookTwo.setCoverImage("http://example.com/cover2.jpg");
        bookTwo.setCategories(Set.of(categoryOne, categoryTwo));

        bookDtoOne = new BookDto();
        bookDtoOne.setId(bookOne.getId());
        bookDtoOne.setTitle(bookOne.getTitle());
        bookDtoOne.setAuthor(bookOne.getAuthor());
        bookDtoOne.setIsbn(bookOne.getIsbn());
        bookDtoOne.setPrice(bookOne.getPrice());
        bookDtoOne.setCoverImage(bookOne.getCoverImage());
        bookDtoOne.setCategoryIds(categoryIds);

        bookDtoTwo = new BookDto();
        bookDtoTwo.setId(bookTwo.getId());
        bookDtoTwo.setTitle(bookTwo.getTitle());
        bookDtoTwo.setAuthor(bookTwo.getAuthor());
        bookDtoTwo.setIsbn(bookTwo.getIsbn());
        bookDtoTwo.setPrice(bookTwo.getPrice());
        bookDtoTwo.setCoverImage(bookTwo.getCoverImage());
        bookDtoTwo.setCategoryIds(categoryIds);

        pageable = PageRequest.of(0, 30);
    }

    @Test
    @DisplayName("Save book with categories in database")
    public void save_ValidCreateBookRequestDto_ShouldReturnBookDto() {
        BookDto bookDto = bookMapper.toDto(bookOne);
        when(bookMapper.toModel(requestDto)).thenReturn(bookOne);
        when(bookRepository.save(bookOne)).thenReturn(bookOne);
        when(bookMapper.toDto(bookOne)).thenReturn(bookDto);
        BookDto saveBookDtoExpected = bookService.save(requestDto);

        Assertions.assertEquals(saveBookDtoExpected,bookDto);
    }

    @Test
    @DisplayName("Find all books")
    public void getAll_ValidBooks_ShouldReturnListOfBookDto() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(bookOne, bookTwo));
        when(bookMapper.toDto(bookOne)).thenReturn(bookDtoOne);
        when(bookMapper.toDto(bookTwo)).thenReturn(bookDtoTwo);

        List<BookDto> actualBookDtos = bookService.findAll(pageable);
        List<BookDto> expectedBookDtos = Arrays.asList(bookDtoOne, bookDtoTwo);
        Assertions.assertEquals(expectedBookDtos, actualBookDtos);
    }

    @Test
    @DisplayName("Get book by id")
    public void getBookById_ValidBook_ShouldReturnBookDto() {
        when(bookRepository.findById(firstId)).thenReturn(Optional.of(bookOne));
        when(bookMapper.toDto(bookOne)).thenReturn(bookDtoOne);
        BookDto actualBookDto = bookService.getBookById(firstId);
        Assertions.assertEquals(bookDtoOne, actualBookDto);
    }

    @Test
    @DisplayName("Update book by book id")
    public void updateBookById_ValidBook_ShouldUpdateBookDto() {
        when(bookRepository.findById(firstId)).thenReturn(Optional.of(bookOne));
        when(bookRepository.save(bookOne)).thenReturn(bookOne);
        when(bookMapper.toDto(bookOne)).thenReturn(bookDtoOne);
        BookDto bookDtoActual = bookService.updateById(firstId, requestDto);
        Assertions.assertEquals(bookDtoOne, bookDtoActual);
    }

    @Test
    @DisplayName("Delete book by id")
    public void deleteBookById_ValidBook_ShouldSuccess() {
        bookService.deleteById(firstId);
        verify(bookRepository, times(1)).deleteById(firstId);
    }

    @Test
    @DisplayName("Search books with parameters")
    public void searchBooks_WithParameters_ShouldReturnListOfBookDto() {
        String[] authors = {"Author 1", "Author 2"};
        String[] titles = {"Book Title 1", "Book Title 2"};
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(bookOne, bookTwo), pageable, 2);
        bookSearchParameters = new BookSearchParameters(authors, titles);
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(bookSearchParameters);
        when(bookSpecificationBuilder.build(bookSearchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(bookOne)).thenReturn(bookDtoOne);
        when(bookMapper.toDto(bookTwo)).thenReturn(bookDtoTwo);

        List<BookDto> actualBookDtos = bookService.search(bookSearchParameters, pageable);
        List<BookDto> expectedBookDtos = Arrays.asList(bookDtoOne,bookDtoTwo);
        Assertions.assertEquals(expectedBookDtos, actualBookDtos);
    }

    @Test
    @DisplayName("Find books by category id")
    public void findBooksByCategoryId_ValidBook_ShouldReturnListOfBookDto() {
        BookDtoWithoutCategoryIds bookDtoWithoutCatOne = new BookDtoWithoutCategoryIds(
                bookOne.getId(), bookOne.getTitle(), bookOne.getAuthor(), bookOne.getIsbn(),
                bookOne.getPrice(), bookOne.getCoverImage());
        BookDtoWithoutCategoryIds bookDtoWithoutCatTwo = new BookDtoWithoutCategoryIds(
                bookTwo.getId(), bookTwo.getTitle(), bookTwo.getAuthor(), bookTwo.getIsbn(),
                bookTwo.getPrice(), bookTwo.getCoverImage());
        when(bookRepository.findAllByCategoryId(firstId, pageable))
                .thenReturn(Arrays.asList(bookOne,bookTwo));
        when(bookMapper.toDtoWithoutCategories(bookOne)).thenReturn(bookDtoWithoutCatOne);
        when(bookMapper.toDtoWithoutCategories(bookTwo)).thenReturn(bookDtoWithoutCatTwo);

        List<BookDtoWithoutCategoryIds> actualBookDto = bookService
                .findByCategoryId(firstId,pageable);
        List<BookDtoWithoutCategoryIds> expectedBookDtos = Arrays.asList(
                bookDtoWithoutCatOne, bookDtoWithoutCatTwo);
        Assertions.assertEquals(expectedBookDtos, actualBookDto);
    }
}
