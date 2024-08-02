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
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.intro.dto.shoppingcart.CartItemDto;
import mate.academy.intro.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.ShoppingCartDto;
import mate.academy.intro.dto.shoppingcart.UpdateCartItemRequestDto;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach(
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
                    new ClassPathResource("database/shoppingcart/create-shopping-cart.sql"));
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
                    new ClassPathResource("database/shoppingcart/delete-shopping-cart.sql")
            );
        }
    }

    @Test
    @WithUserDetails("jane.doe@example.com")
    @DisplayName("Add a new cart item")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-shopping-cart.sql",
            "classpath:database/shoppingcart/create-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldAddNewCartItem() throws Exception {
        CreateCartItemRequestDto createCartItemRequestDto =
                new CreateCartItemRequestDto(1L, 3);
        CartItemDto cartItemDto = new CartItemDto(1L, 5L, "", 5);
        String jsonRequest = objectMapper.writeValueAsString(createCartItemRequestDto);

        MvcResult result = mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ShoppingCartDto actualResponse = objectMapper.readValue(
                jsonResponse, ShoppingCartDto.class);

        Assertions.assertNotNull(actualResponse);
        Assertions.assertNotNull(actualResponse.id());
        EqualsBuilder.reflectionEquals(cartItemDto, actualResponse, "id");
    }

    @Test
    @WithUserDetails("jane.doe@example.com")
    @DisplayName("Get all cart items in shopping cart")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-shopping-cart.sql",
            "classpath:database/shoppingcart/create-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldGetAllCartItemsInShoppingCart() throws Exception {
        List<CartItemDto> expected = new ArrayList<>();
        expected.add(new CartItemDto(1L, 1L, "Book Title 1", 5));
        MvcResult result = mockMvc.perform(get("/api/cart")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        ShoppingCartDto actual = objectMapper.readValue(jsonResponse, ShoppingCartDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.cartItems().size());
        Assertions.assertEquals(expected, actual.cartItems());
    }

    @Test
    @WithUserDetails("jane.doe@example.com")
    @DisplayName("Update cart item quantity")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-shopping-cart.sql",
            "classpath:database/shoppingcart/create-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldUpdateCartItemQuantity() throws Exception {
        CartItemDto expected = new CartItemDto(1L, 1L, "Book Title 1", 10);
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(10);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/api/cart/items/{cartItemId}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CartItemDto actual = objectMapper.readValue(jsonResponse, CartItemDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.quantity(), actual.quantity());
    }

    @Test
    @WithUserDetails("jane.doe@example.com")
    @DisplayName("Delete cart item")
    @Sql(scripts = {
            "classpath:database/shoppingcart/delete-shopping-cart.sql",
            "classpath:database/shoppingcart/create-shopping-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/api/cart/items/{cartItemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}
