package com.bookstore.controller;

import static com.bookstore.util.TestUtil.createBookDtoValidRequest;
import static com.bookstore.util.TestUtil.createFirstBookDto;
import static com.bookstore.util.TestUtil.createSecondBookDto;
import static com.bookstore.util.TestUtil.createThirdBookDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.dto.book.BookDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Sql(scripts = "classpath:database/categories/add-two-category.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:database/books/delete-three-books.sql",
        "classpath:database/categories/delete-categories.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("return all books")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = {"classpath:database/books/add-three-books-with-same-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ThreeBooks_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size","10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> expected = List.of(
                createFirstBookDto(),createSecondBookDto(),createThirdBookDto()
        );
        String json = result.getResponse().getContentAsString();
        JsonNode content = objectMapper.readTree(json).get("content");
        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BookDto.class);
        List<BookDto> actual = objectMapper.readValue(content.toString(),type);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("find book by valid id")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = {"classpath:database/books/add-three-books-with-same-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_WithValidId_Ok() throws Exception {
        BookDto expected = createSecondBookDto();
        MvcResult result = mockMvc.perform(get("/books/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        BookDto actual = objectMapper.readValue(json, BookDto.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("search book by title")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = {"classpath:database/books/add-three-books-with-same-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBook_WithTitle_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                .param("page","0")
                .param("size","10")
                .param("titles", "Catching Fire")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        JsonNode content = objectMapper.readTree(json).get("content");
        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BookDto.class);
        List<BookDto> actual = objectMapper.readValue(content.toString(),type);
        List<BookDto> expected = List.of(createFirstBookDto());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("create book with valid request")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/books/delete-one-book-after-create-test.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequest_Ok() throws Exception {
        String bookRequest = objectMapper.writeValueAsString(createBookDtoValidRequest());
        BookDto expected = createBookDtoValidRequest();
        MvcResult result = mockMvc.perform(post("/books")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("update book with valid request")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/books/add-three-books-with-same-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBookById_ValidRequest_Ok() throws Exception {
        String bookRequest = objectMapper.writeValueAsString(createBookDtoValidRequest());
        BookDto expected = createBookDtoValidRequest();
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("delete book by id")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/books/add-three-books-with-same-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBookById_ValidRequest_Ok() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> expected = List.of(
                createSecondBookDto(),createThirdBookDto()
        );
        String json = result.getResponse().getContentAsString();
        JsonNode content = objectMapper.readTree(json).get("content");
        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BookDto.class);
        List<BookDto> actual = objectMapper.readValue(content.toString(),type);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }
}
