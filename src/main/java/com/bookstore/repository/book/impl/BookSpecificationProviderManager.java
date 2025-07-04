package com.bookstore.repository.book.impl;

import com.bookstore.model.Book;
import com.bookstore.repository.book.SpecificationProvider;
import com.bookstore.repository.book.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Can`t find correct specification provider for key"
                                + key));
    }
}
