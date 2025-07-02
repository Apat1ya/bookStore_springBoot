package com.bookstore.controller;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.book.BookSearchParametersDto;
import com.bookstore.dto.book.CreateBookRequestDto;
import com.bookstore.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Books", description = "Books: create, view, update and delete")
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Get all books",
            description = "Returns a list of all books in the store"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @Operation(
            summary = "Get book by ID",
            description = "Returns the book found by ID"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(
            summary = "Create book",
            description = "Create a new book and save it to the DB"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(
            summary = "Update book",
            description = " Modifies the book information by its ID."
            + "Fields that are not passed in the body of the query remain unchanged"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.update(id,bookDto);
    }

    @Operation(
            summary = "Delete book",
            description = "Delete one book from DB according to its ID"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(
            summary = "Search book",
            description = "Returns a list of books that satisfy one or more filters."
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParametersDto parameters) {
        return bookService.search(parameters);
    }
}
