package mate.academy.intro.controller;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.category.CategoryDto;
import mate.academy.intro.dto.category.CreateCategoryRequestDto;
import org.junit.jupiter.api.AfterAll;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
     void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-books-to-books-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-books-from-books-table.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new product")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Fiction", "Fictional books");
        CategoryDto expected = new CategoryDto(
                1L, requestDto.name(),requestDto.description());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected,actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all categories")
    void getAll_ValidCategories_ReturnListofCategories()throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto(1L, "Fiction", "Fictional books"));
        expected.add(new CategoryDto(2L, "Non-Fiction", "Non-fictional books"));
        expected.add(new CategoryDto(3L, "Science Fiction", "Books about science fiction"));

        MvcResult result = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by id")
    void getCategoryById_ValidCategories_ReturnCategoryDto() throws Exception {
        CategoryDto expected = new CategoryDto(1L, "Fiction", "Fictional books");
        Long id = 1L;
        MvcResult result = mockMvc.perform(get("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertEquals(expected,actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category")
    void updateCategory_ValidRequestDto_ReturnCategoryDto() throws Exception {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Fiction", "Fictional books");
        CategoryDto expected = new CategoryDto(
                id, requestDto.name(),requestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected,actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by id")
    void deleteCategory_ValidCategories_Success() throws Exception {
        Long id = 2L;
        mockMvc.perform(
                delete("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get books by category id")
    void getBooksByCategory_validCategories_returnListOfBooks() throws Exception {
        List<BookDtoWithoutCategoryIds> listOfBooksDtoExpected =
                createListOfBooksDtoWithoutCategoryId();
        Long categoryId = 2L;

        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] bookDtoWithoutCategoryIdsActual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);
        Assertions.assertNotNull(bookDtoWithoutCategoryIdsActual);
        Assertions.assertEquals(listOfBooksDtoExpected.size(),
                bookDtoWithoutCategoryIdsActual.length);
        Assertions.assertEquals(listOfBooksDtoExpected,
                Arrays.stream(bookDtoWithoutCategoryIdsActual).toList());
    }

    private List<BookDtoWithoutCategoryIds> createListOfBooksDtoWithoutCategoryId() {
        List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIds = new ArrayList<>();
        bookDtoWithoutCategoryIds.add(new BookDtoWithoutCategoryIds(
                2L,
                "Book Title 2",
                "Author 2",
                "ISBN0002",
                new BigDecimal("29.99"),
                "example.com/cover2.jpg"));
        bookDtoWithoutCategoryIds.add(new BookDtoWithoutCategoryIds(
                5L,
                "Book Title 5",
                "Author 5",
                "ISBN0005",
                BigDecimal.valueOf(59.99),
                "example.com/cover5.jpg"));
        return bookDtoWithoutCategoryIds;
    }
}
