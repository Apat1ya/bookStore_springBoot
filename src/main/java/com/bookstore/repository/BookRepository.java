package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
//    Book save(Book book);
//
//    List<Book> findAll();
//
//    Optional<Book> findById(Long id);
//
//    void deleteById(Long id);
}
