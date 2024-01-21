package com.exxeta.bookservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id", nullable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="author", nullable = false)
    private String author;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal price;

    public Book(String title, String author, BigDecimal price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String toString(){
        return "Book[Id : " + id + ", Title : " + title + ", Author : " + author + ", Price : " + price + "]";
    }

}
