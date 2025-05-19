package com.bookstore;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setAuthor("Taras");
            book1.setIsbn("1235");
            book1.setTitle("Title");
            book1.setPrice(BigDecimal.valueOf(299));
            bookService.save(book1);
            System.out.println(bookService.findAll());
        };
    }
}
