package mate.academy.intro.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-books-to-books-table.sql"));
        }
    }

    @AfterEach
    void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/delete-books-from-books-table.sql"));
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books from database")
    void getAll_givenAllBooksFromDatabase_shouldReturnListOfBooks() throws Exception {
        List<BookDto> bookDtoListExpected = createListOfBooksDto();

        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] bookDtoListActual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookDto[].class);
        Assertions.assertEquals(6L, bookDtoListActual.length);
        Assertions.assertEquals(bookDtoListExpected, Arrays.stream(bookDtoListActual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book by id from database")
    void getBookById_validRequestDto_returnBookDto() throws Exception {
        BookDto expected = new BookDto();
        expected.setTitle("Book Title 1");
        expected.setAuthor("Author 1");
        expected.setPrice(BigDecimal.valueOf(19.99));
        expected.setCoverImage("9781234567891");
        expected.setCoverImage("https://example.com/cover1.jpg");
        expected.setCategoryIds(Set.of(1L));
        Long id = 1L;
        MvcResult result = mockMvc.perform(get("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto bookDtoActual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(bookDtoActual);
        EqualsBuilder.reflectionEquals(expected, bookDtoActual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new book")
    void createBook_validRequestDto_Success() throws Exception {
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        BookDto bookDtoExpected = createBookDto(bookRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto bookDtoActual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(bookDtoActual);
        EqualsBuilder.reflectionEquals(bookDtoExpected, bookDtoActual, "id","description");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book by id")
    void updateBookById_validRequestDto_returnBookDto() throws Exception {
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        Long id = 1L;
        BookDto bookDtoExpected = toBookDto(bookRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult mvcResult = mockMvc.perform(put("/books/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BookDto bookDtoActual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(bookDtoActual);
        EqualsBuilder.reflectionEquals(bookDtoExpected, bookDtoActual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete book by id")
    void deleteBookById_validRequestDto_Success() throws Exception {
        long id = 1;
        mockMvc.perform(delete("/books/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Search book by id from database")
    void searchBooks_validParameters_returnListOfBooks() throws Exception {
        List<BookDto> listOfBooksByParametersExpected = getListOfBooksByParameters();

        MvcResult mvcResult = mockMvc.perform(get("/books/search")
                        .param("authors", "Author 1, Author 2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] bookDtos = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookDto[].class);
        Assertions.assertNotNull(bookDtos);
        Assertions.assertEquals(listOfBooksByParametersExpected.size(), bookDtos.length);
        Assertions.assertEquals(listOfBooksByParametersExpected, Arrays.stream(bookDtos).toList());
    }

    private CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book Title 1");
        requestDto.setAuthor("Author 1");
        requestDto.setIsbn("9781234567897");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setCoverImage("https://example.com/cover1.jpg");
        requestDto.setCategoryIds(Set.of(1L));
        return requestDto;
    }

    private BookDto createBookDto(CreateBookRequestDto requestDto) {
        BookDto bookDto = new BookDto();
        bookDto.setId(2L);
        bookDto.setTitle(requestDto.getTitle());
        bookDto.setAuthor(requestDto.getAuthor());
        bookDto.setIsbn(requestDto.getIsbn());
        bookDto.setPrice(requestDto.getPrice());
        bookDto.setCoverImage(requestDto.getCoverImage());
        return bookDto;
    }

    private BookDto toBookDto(CreateBookRequestDto requestDto) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(requestDto.getTitle());
        bookDto.setAuthor(requestDto.getAuthor());
        bookDto.setPrice(requestDto.getPrice());
        bookDto.setCoverImage(requestDto.getCoverImage());
        bookDto.setCategoryIds(requestDto.getCategoryIds());
        return bookDto;
    }

    private List<BookDto> getListOfBooksByParameters() {
        List<BookDto> bookDtoList = new ArrayList<>();

        bookDtoList.add(new BookDto().setId(1L).setTitle("Book Title 1")
                .setAuthor("Author 1").setIsbn("9781234567891")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("https://example.com/cover1.jpg").setCategoryIds(Set.of(1L)));

        bookDtoList.add(new BookDto().setId(2L).setTitle("Book Title 2")
                .setAuthor("Author 2").setIsbn("9781234567892")
                .setPrice(BigDecimal.valueOf(29.99))
                .setCoverImage("https://example.com/cover2.jpg").setCategoryIds(Set.of(2L)));

        return bookDtoList;
    }

    private List<BookDto> createListOfBooksDto() {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto().setId(1L).setTitle("Book Title 1")
                .setAuthor("Author 1").setIsbn("9781234567891")
                .setPrice(BigDecimal.valueOf(19.99))
                .setCoverImage("https://example.com/cover1.jpg").setCategoryIds(Set.of(1L)));

        bookDtoList.add(new BookDto().setId(2L).setTitle("Book Title 2")
                .setAuthor("Author 2").setIsbn("9781234567892")
                .setPrice(BigDecimal.valueOf(29.99))
                .setCoverImage("https://example.com/cover2.jpg").setCategoryIds(Set.of(2L)));

        bookDtoList.add(new BookDto().setId(3L).setTitle("Book Title 3")
                .setAuthor("Author 3").setIsbn("9781234567893")
                .setPrice(BigDecimal.valueOf(39.99))
                .setCoverImage("https://example.com/cover3.jpg").setCategoryIds(Set.of(3L)));

        bookDtoList.add(new BookDto().setId(4L).setTitle("Book Title 4")
                .setAuthor("Author 4").setIsbn("9781234567894")
                .setPrice(BigDecimal.valueOf(49.99))
                .setCoverImage("https://example.com/cover4.jpg").setCategoryIds(Set.of(1L)));

        bookDtoList.add(new BookDto().setId(5L).setTitle("Book Title 5")
                .setAuthor("Author 5").setIsbn("9781234567895")
                .setPrice(BigDecimal.valueOf(59.99))
                .setCoverImage("https://example.com/cover5.jpg").setCategoryIds(Set.of(2L)));

        bookDtoList.add(new BookDto().setId(6L).setTitle("Book Title 6")
                .setAuthor("Author 6").setIsbn("9781234567896")
                .setPrice(BigDecimal.valueOf(69.99))
                .setCoverImage("https://example.com/cover6.jpg").setCategoryIds(Set.of(3L)));
        return bookDtoList;
    }
}
