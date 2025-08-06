package com.bookstore.util;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CategoryResponseDto;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestUtil {

    public static BookDto createFirstBookDto() {
        BookDto firstBook = new BookDto();
        firstBook.setId(1L);
        firstBook.setTitle("Catching Fire");
        firstBook.setAuthor("Suzanne Collins");
        firstBook.setIsbn("9826307743657");
        firstBook.setPrice(BigDecimal.valueOf(19.99));
        firstBook.setDescription("The second book of “The Hunger Games” trilogy");
        firstBook.setCoverImage(null);
        firstBook.setCategoryIds(Set.of(1L));
        return firstBook;
    }

    public static BookDto createSecondBookDto() {
        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setTitle("Harry Potter and the Deathly Hallows");
        secondBook.setAuthor("J.K. Rowling");
        secondBook.setIsbn("9826063743657");
        secondBook.setPrice(BigDecimal.valueOf(17.99));
        secondBook.setDescription("The story of Harry Potters last battle against Voldemort");
        secondBook.setCoverImage(null);
        secondBook.setCategoryIds(Set.of(1L));
        return secondBook;
    }

    public static BookDto createThirdBookDto() {
        BookDto thirdBook = new BookDto();
        thirdBook.setId(3L);
        thirdBook.setTitle("Glass Girls");
        thirdBook.setAuthor("Danie Shokoohi");
        thirdBook.setIsbn("98260326543657");
        thirdBook.setPrice(BigDecimal.valueOf(14.99));
        thirdBook.setDescription("Long estranged from her family,"
                + " Alice is haunted by memories of her own abusive mother");
        thirdBook.setCoverImage(null);
        thirdBook.setCategoryIds(Set.of(2L));
        return thirdBook;
    }

    public static Book createFirstBook() {
        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("Catching Fire");
        firstBook.setAuthor("Suzanne Collins");
        firstBook.setIsbn("9826307743657");
        firstBook.setPrice(BigDecimal.valueOf(19.99));
        firstBook.setDescription("The second book of “The Hunger Games” trilogy");
        firstBook.setCoverImage(null);
        return firstBook;
    }

    public static Book createSecondBook() {
        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("Harry Potter and the Deathly Hallows");
        secondBook.setAuthor("J.K. Rowling");
        secondBook.setIsbn("9826063743657");
        secondBook.setPrice(BigDecimal.valueOf(17.99));
        secondBook.setDescription("The story of Harry Potters last battle against Voldemort");
        secondBook.setCoverImage(null);
        return secondBook;
    }

    public static Book createThirdBook() {
        Book thirdBook = new Book();
        thirdBook.setId(3L);
        thirdBook.setTitle("Glass Girls");
        thirdBook.setAuthor("Danie Shokoohi");
        thirdBook.setIsbn("98260326543657");
        thirdBook.setPrice(BigDecimal.valueOf(14.99));
        thirdBook.setDescription("Long estranged from her family,"
                + " Alice is haunted by memories of her own abusive mother");
        thirdBook.setCoverImage(null);
        return thirdBook;
    }

    public static BookDto createBookDtoValidRequest() {
        BookDto book = new BookDto();
        book.setTitle("1408");
        book.setAuthor("Stephen King");
        book.setIsbn("9780307860388");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("horror novel");
        book.setCategoryIds(Set.of(2L));
        return book;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("That");
        book.setAuthor("Stephen King");
        book.setIsbn("9780307860388");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("horror ");
        return book;
    }

    public static BookDto createBookDto() {
        BookDto book = new BookDto();
        book.setId(1L);
        book.setTitle("That");
        book.setAuthor("Stephen King");
        book.setIsbn("9780307860388");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("horror ");
        return book;
    }

    public static List<Category> createListOfTwoCategories() {
        Category firstCategory = new Category();
        firstCategory.setId(1L);
        firstCategory.setName("Fantasy");
        Category secondCategory = new Category();
        secondCategory.setId(2L);
        secondCategory.setName("Horror");
        List<Category> categories = List.of(
                firstCategory,
                secondCategory
        );
        return categories;
    }

    public static List<CategoryResponseDto> createListOfTwoCategoriesDtos() {
        CategoryResponseDto firstCategory = new CategoryResponseDto();
        firstCategory.setId(1L);
        firstCategory.setName("Fantasy");
        CategoryResponseDto secondCategory = new CategoryResponseDto();
        secondCategory.setId(2L);
        secondCategory.setName("Horror");
        List<CategoryResponseDto> categories = List.of(
                firstCategory,
                secondCategory
        );
        return categories;
    }

    public static List<CategoryResponseDto> createListOfCategoriesDto() {
        CategoryResponseDto firstCategory = new CategoryResponseDto();
        firstCategory.setId(4L);
        firstCategory.setName("Comedy");
        CategoryResponseDto secondCategory = new CategoryResponseDto();
        secondCategory.setId(5L);
        secondCategory.setName("Roman");
        CategoryResponseDto thirdCategory = new CategoryResponseDto();
        thirdCategory.setId(6L);
        thirdCategory.setName("Title");
        List<CategoryResponseDto> categories = List.of(
                firstCategory,
                secondCategory,
                thirdCategory
        );
        return categories;
    }

    public static List<CategoryResponseDto> createListOfFiveCategories() {
        CategoryResponseDto first = new CategoryResponseDto();
        first.setId(7L);
        first.setName("Biography");
        CategoryResponseDto second = new CategoryResponseDto();
        second.setId(8L);
        second.setName("Thriller");
        CategoryResponseDto third = new CategoryResponseDto();
        third.setId(9L);
        third.setName("Roman");
        CategoryResponseDto fourth = new CategoryResponseDto();
        fourth.setId(10L);
        fourth.setName("Documental");
        CategoryResponseDto fifth = new CategoryResponseDto();
        fifth.setId(11L);
        fifth.setName("Historical");
        List<CategoryResponseDto> categories = List.of(
                first,
                second,
                third,
                fourth,
                fifth
        );
        return categories;
    }

    public static CategoryDto createCategoryValidRequest() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Horror");
        return categoryDto;
    }

}
