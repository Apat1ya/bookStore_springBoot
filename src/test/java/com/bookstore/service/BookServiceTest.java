package com.bookstore.service;

import static com.bookstore.util.TestUtil.createBook;
import static com.bookstore.util.TestUtil.createBookDto;
import static com.bookstore.util.TestUtil.createFirstBook;
import static com.bookstore.util.TestUtil.createFirstBookDto;
import static com.bookstore.util.TestUtil.createSecondBook;
import static com.bookstore.util.TestUtil.createSecondBookDto;
import static com.bookstore.util.TestUtil.createThirdBook;
import static com.bookstore.util.TestUtil.createThirdBookDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.book.CreateBookRequestDto;
import com.bookstore.exception.DataProcessingException;
import com.bookstore.mapper.book.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Category;
import com.bookstore.repository.book.BookRepository;
import com.bookstore.repository.category.CategoryRepository;
import com.bookstore.service.book.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Get book by valid id")
    void getBookById_WithValidId_Ok() {
        Long bookId = 1L;
        Book book = createBook();
        BookDto expectedDto = createBookDto();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.findById(bookId);

        assertEquals(expectedDto,actual);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Get book by invalid id should throw DataProcessingException")
    void getBookById_WithInvalidId_NotOk() {
        Long bookId = 89L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        DataProcessingException exception = assertThrows(
                DataProcessingException.class,
                () -> bookService.findById(bookId)
        );
        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository).findById(bookId);

    }

    @Test
    @DisplayName("findAll should return all books")
    void findAll_ThreeBooks_Ok() {
        List<BookDto> bookDtos = List.of(createFirstBookDto(),
                createSecondBookDto(),
                createThirdBookDto());
        List<Book> books = List.of(createFirstBook(),
                createSecondBook(),
                createThirdBook()
        );
        Pageable pageable = PageRequest.of(0,10);
        Page<Book> bookPage = new PageImpl<>(books,pageable,3);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(books.get(0))).thenReturn(bookDtos.get(0));
        when(bookMapper.toDto(books.get(1))).thenReturn(bookDtos.get(1));
        when(bookMapper.toDto(books.get(2))).thenReturn(bookDtos.get(2));
        Page<BookDto> result = bookService.findAll(pageable);

        Page<BookDto> expected = new PageImpl<>(bookDtos,pageable,3);
        assertEquals(expected,result);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(books.get(0));
        verify(bookMapper).toDto(books.get(1));
        verify(bookMapper).toDto(books.get(2));
    }

    @Test
    @DisplayName("Create and save book")
    void createBook_WithValidRequest_Ok() {
        Category category = new Category();
        category.setId(1L);
        Set<Category> categorySet = Set.of(category);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("test title");
        requestDto.setAuthor("test author");
        requestDto.setCategoryIds(Set.of(1L));

        Book bookSave = new Book();
        bookSave.setId(1L);
        bookSave.setTitle("test title");
        bookSave.setAuthor("test author");
        bookSave.setCategories(Set.of());

        BookDto expectedDto = new BookDto();
        expectedDto.setId(1L);
        expectedDto.setTitle("test title");
        expectedDto.setAuthor("test author");
        expectedDto.setCategoryIds(Set.of(1L));

        when(bookMapper.toEntity(requestDto)).thenReturn(bookSave);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(bookSave)).thenReturn(bookSave);
        when(bookMapper.toDto(bookSave)).thenReturn(expectedDto);

        BookDto actualDto = bookService.save(requestDto);

        assertEquals(expectedDto,actualDto);
        verify(bookMapper).toEntity(requestDto);
        verify(categoryRepository).getReferenceById(1L);
        verify(bookRepository).save(bookSave);
        verify(bookMapper).toDto(bookSave);
    }

    @Test
    @DisplayName("Update books")
    void updateBook_RequestToChangePrice_Ok() {
        Category category = new Category();
        category.setId(1L);
        Set<Category> categorySet = Set.of(category);

        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(Set.of());

        Book changedBook = new Book();
        changedBook.setId(bookId);
        changedBook.setTitle("test title");
        changedBook.setAuthor("test author");
        changedBook.setPrice(BigDecimal.valueOf(22.99));
        changedBook.setCategories(Set.of());

        BookDto expect = new BookDto();
        expect.setId(bookId);
        expect.setTitle("test title");
        expect.setAuthor("test author");
        expect.setPrice(BigDecimal.valueOf(22.99));
        expect.setCategoryIds(Set.of(1L));

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("test title");
        requestDto.setAuthor("test author");
        requestDto.setPrice(BigDecimal.valueOf(22.99));
        requestDto.setCategoryIds(Set.of(1L));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateFromDto(book,requestDto);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(book)).thenReturn(changedBook);
        when(bookMapper.toDto(book)).thenReturn(expect);

        BookDto actual = bookService.update(bookId, requestDto);

        assertEquals(expect,actual);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateFromDto(book,requestDto);
        verify(categoryRepository).getReferenceById(1L);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Delete should remove book")
    void deleteBook_WithValidId_Ok() {
        Long bookId = 1L;

        bookService.deleteById(bookId);
        verify(bookRepository).deleteById(bookId);
    }
}
