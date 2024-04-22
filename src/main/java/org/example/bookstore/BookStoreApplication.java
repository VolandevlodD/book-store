package org.example.bookstore;

import java.math.BigDecimal;
import org.example.bookstore.model.Book;
import org.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setTitle("Book 1");
            book1.setAuthor("Author 1");
            book1.setIsbn("ISBN 1");
            book1.setPrice(new BigDecimal("100.00"));
            book1.setCoverImage("cover.jpg");

            bookService.save(book1);

            System.out.println(bookService.findAll());
        };
    }
}
