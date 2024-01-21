package com.exxeta.bookservice.service;

import com.exxeta.bookservice.entities.Book;
import com.exxeta.bookservice.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service to hold the business logic for everything concerning books.
 */

@Service
@RequiredArgsConstructor
@Setter
public class BookService {

    private final BookRepository bookRepository;
    private final Logger logger = LoggerFactory.getLogger(BookService.class);


    /**
     * Creates a new Book Entry in the Database with a new Id.
     * If a Id is given it is ignored and a Autogenerated one is used.
     * title, price and author are required (=> can not be null).
     *
     * @param book the book to save
     */
    public Optional<Book> saveNewBook(Book book) {
        if (!hasOnlyNullableColumns(book)) return Optional.empty();
        // Copy values to new instance to always make sure a new instance is created
        Book b = new Book(book.getTitle(), book.getAuthor(), book.getPrice());
        Book savedBook = bookRepository.save(b);
        logger.info("New Book created : " + b);
        return Optional.of(savedBook);
    }
    private static boolean hasOnlyNullableColumns(Book book) {
        return book.getTitle() != null && book.getPrice() != null && book.getAuthor() != null;
    }

    /**
     * Returns all book entries
     * @return the book entries
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Returns the Book with the given Id
     * @param id the id
     * @return the book
     */

    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    /**
     * Deletes the book with the given id
     * @param id the id
     */
    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Returns the Books whose title contains the given word
     * @param word the word
     * @return the title
     */
    public List<Book> getBooksWithTitleContaining(String word) {
        return bookRepository.findBooksByTitleContains(word);
    }
}
