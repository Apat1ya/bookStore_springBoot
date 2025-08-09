package com.bookstore.controller;

import static com.bookstore.util.TestUtil.createCategoryValidRequest;
import static com.bookstore.util.TestUtil.createListOfFiveCategories;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CategoryResponseDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("return all categories")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/categories/add-five-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_FiveCategories_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                .param("page","0")
                .param("size","10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryResponseDto> expected = createListOfFiveCategories();
        String json = result.getResponse().getContentAsString();
        JsonNode content = objectMapper.readTree(json).get("content");
        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class,CategoryResponseDto.class);
        List<CategoryResponseDto> actual = objectMapper.readValue(content.toString(),type);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("Should return correct category by id")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/categories/add-three-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_WithValidId_Ok() throws Exception {
        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(5L);
        expected.setName("Roman");
        MvcResult result = mockMvc.perform(get("/categories/5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        CategoryResponseDto actual = objectMapper.readValue(json, CategoryResponseDto.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Get category by invalid id should return 404")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_WithInvalidId_NotOk() throws Exception {
        Long id = 999L;
        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("create category with valid request")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/delete-one-category-after-create.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_WithValidRequest_Ok() throws Exception {
        String request = objectMapper.writeValueAsString(createCategoryValidRequest());
        CategoryDto expect = createCategoryValidRequest();
        MvcResult result = mockMvc.perform(post("/categories")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("update category with valid request")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/add-three-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategoryById_WithValidRequest_Ok() throws Exception {
        CategoryDto requestDto = new CategoryDto();
        requestDto.setName("Documental");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(5L);
        expected.setName("Documental");

        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/categories/5")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

}
