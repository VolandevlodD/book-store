package org.example.bookstore.mapper;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoriesIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategoriesByIds(CreateBookRequestDto bookDto, @MappingTarget Book book) {
        Set<Category> categories = bookDto.getCategoryIds()
                .stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }

    @Named("bookIdExtractor")
    default Long extractBookId(Book book) {
        return book.getId();
    }

    @Named("bookTitleExtractor")
    default String extractBookTitle(Book book) {
        return book.getTitle();
    }

    @Named("bookPriceExtractor")
    default BigDecimal extractBookPrice(Book book) {
        return book.getPrice();
    }

}
