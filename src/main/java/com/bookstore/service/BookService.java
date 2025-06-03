package com.bookstore.service;

import com.bookstore.dto.BookDto;
import com.bookstore.dto.BookSearchParametersDto;
import com.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto bookSearchParametersDto);
}
