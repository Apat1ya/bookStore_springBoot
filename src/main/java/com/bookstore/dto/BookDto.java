package com.bookstore.dto;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String isbn;
    @Column(nullable = false)
    private BigDecimal price;
    @Column
    private String description;
    @Column
    private String coverImage;
}
