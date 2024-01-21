package com.exxeta.bookservice;

import com.exxeta.bookservice.controller.BookController;
import com.exxeta.bookservice.entities.Book;
import com.exxeta.bookservice.repositories.BookRepository;
import com.exxeta.bookservice.service.BookService;
import lombok.Setter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Setter
class BookServiceApplicationTests {

    @Autowired
    private BookService service;

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookController bookController;


    @BeforeEach
    void populateDatabase() {
        repository.save(new Book("Algorithms", "Robert Sedgewick", new BigDecimal("20.10")));
        repository.save(new Book("Test-Driven Development", "Kent Beck", new BigDecimal("15.60")));
        repository.save(new Book("Clean Code", "Robert C. Martin", new BigDecimal("10.90")));
        repository.save(new Book("Serious Cryptography", "Jean-Phillipe Aumasson", new BigDecimal("20.10")));
        repository.save(new Book("Domain Driven Design", "Eric Evans", new BigDecimal("9.99")));
        repository.save(new Book("Generative AI", "Sharad Gandhi", new BigDecimal("18.04")));
        repository.save(new Book("Genetics", "Some Author", new BigDecimal("10000.00")));

        System.out.println("Setup Database:");
        repository.findAll().forEach(System.out::println);
    }

    @AfterEach
    void deleteDatabaseEntries() {
        repository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(service);
        assertNotNull(repository);
        assertNotEquals(0L, repository.count());
        assertNotNull(bookController);
    }

    @Test
    void findAllServiceTest() {
        assertFalse(service.getAllBooks().isEmpty());
        assertEquals(7, service.getAllBooks().size());
    }

    @Test
    void findByIDServiceTest() {
        var book = repository.findBookByAuthor("Kent Beck");
        assertTrue(book.isPresent());
        var bookReference = book.get();
        Optional<Book> foundBook = service.getBookById(bookReference.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(bookReference.getTitle(), foundBook.get().getTitle());
    }

    @Test
    void saveBookServiceTest() {
        int initialSize = service.getAllBooks().size();
        service.saveNewBook(new Book("Design Patterns", "Gang of Four", new BigDecimal("12.99")));
        assertEquals(initialSize + 1, service.getAllBooks().size());
    }

    @Test
    void saveBookServiceOnlyAcceptsNonNullableTest() {
        int initialSize = service.getAllBooks().size();
        service.saveNewBook(new Book(null, "Gang of Four", new BigDecimal("12.99")));
        service.saveNewBook(new Book("Design Patterns", null, new BigDecimal("12.99")));
        service.saveNewBook(new Book("Design Patterns", "Gang of Four", null));
        assertEquals(initialSize, service.getAllBooks().size());
    }


    @Test
    void deleteServiceTest() {
        int initialSize = service.getAllBooks().size();
        var book = repository.findBookByAuthor("Kent Beck");
        assertTrue(book.isPresent());
        long id = book.get().getId();
        service.deleteBook(id);
        assertEquals(initialSize - 1, service.getAllBooks().size());
    }

    @Test
    void findByTitleContainsTest() {
        List<Book> booksContainingWord = service.getBooksWithTitleContaining("Gen");
        assertEquals(2, booksContainingWord.size());
        Set<String> bookTitleSet = booksContainingWord.stream().map(Book::getTitle).collect(Collectors.toSet());
        assertTrue(bookTitleSet.contains("Genetics"));
        assertTrue(bookTitleSet.contains("Generative AI"));
        assertFalse(bookTitleSet.contains("Algorithms"));
    }

    @Test
    void getAllApiTest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(7)));
    }

    @Test
    void getSingleBookApiTest() throws Exception {
        Book reference = service.getAllBooks().get(0);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + reference.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is(reference.getTitle())));
    }

    @Test
    void deleteBookApiTest() throws Exception {
        Book reference = service.getAllBooks().get(0);
        int size = service.getAllBooks().size();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + reference.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(size - 1, service.getAllBooks().size());
    }

    @Test
    void findBooksByTitleApiTest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/books/title?title=Gen"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }
}
