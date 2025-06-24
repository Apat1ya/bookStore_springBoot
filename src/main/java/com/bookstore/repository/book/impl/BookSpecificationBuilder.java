package com.bookstore.repository.book.impl;

import com.bookstore.dto.book.BookSearchParametersDto;
import com.bookstore.model.Book;
import com.bookstore.repository.book.SpecificationBuilder;
import com.bookstore.repository.book.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String titleKey = "title";
    public static final String authorKey = "author";

    private final SpecificationProviderManager<Book> bookSpecificationProvider;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(bookSpecificationProvider
                    .getSpecificationProvider(titleKey)
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(bookSpecificationProvider
                    .getSpecificationProvider(authorKey)
                    .getSpecification(searchParameters.authors()));
        }
        return spec;
    }
}
