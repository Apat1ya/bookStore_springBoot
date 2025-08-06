package com.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bookstore.model.Book;
import com.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Returns two books with a common category")
    @Sql(scripts = {"classpath:database/categories/add-two-category.sql",
            "classpath:database/books/add-three-books-with-same-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/delete-three-books.sql",
            "classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_TwoBooksByCategory_Ok() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Book> books = bookRepository.findAllByCategoriesId(1L,pageable);
        assertThat(books.getContent())
                .extracting(Book::getTitle)
                .doesNotContain("Glass Girls")
                .containsExactlyInAnyOrder("Catching Fire","Harry Potter and the Deathly Hallows");
    }
}
