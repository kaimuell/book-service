package com.exxeta.bookservice.controller;

import com.exxeta.bookservice.entities.Book;
import com.exxeta.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * RestController to offer the methods of a {@link BookService} through a REST-API.
 */
@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * @return all books
     */
    @GetMapping("")
    public Collection<Book> getAllBooks() {
        logger.info("all books requested");
        return bookService.getAllBooks();
    }

    /**
     * Returns the book with the given id, if in Database, or a null Object if not.
     *
     * @param id the id
     * @return the book | null
     */
    @GetMapping("/{id}")
    public Optional<Book> getBookWithId(@PathVariable long id) {
        logger.info("request for book with id : " + id);
        return bookService.getBookById(id);
    }

    /**
     * Saves a book as a new entry. if a id is send in the object it is ignored.
     * title, price and author are required (=> can not be null).
     *
     * @param book the book
     * @return the Book if successfull, null if not
     */
    @PostMapping(path = "")
    public Optional<Book> saveBook(@RequestBody Book book) {
        logger.info("saveBook request");
        return bookService.saveNewBook(book);
    }


    /**
     * deletes the book with the given id
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable long id) {
        logger.info("deleting book with id " + id);
        bookService.deleteBook(id);
    }

    /**
     * Returns all saved books with the given word anywhere in the title ( title LIKE %word% )
     *
     * @param title the title
     * @return all books whose title contain the word.
     */
    @GetMapping("title")
    public Collection<Book> getBooksWithTitleContainingWord(@RequestParam String title) {
        logger.info("request for books with title : " + title);
        return bookService.getBooksWithTitleContaining(title);
    }
}
