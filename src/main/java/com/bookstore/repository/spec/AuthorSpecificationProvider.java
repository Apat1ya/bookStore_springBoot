package com.bookstore.repository.spec;

import com.bookstore.model.Book;
import com.bookstore.repository.book.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder) -> root.get("author")
                .in(Arrays.stream(param).toArray());
    }
}
