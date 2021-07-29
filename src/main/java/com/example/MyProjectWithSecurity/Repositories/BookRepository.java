package com.example.MyProjectWithSecurity.Repositories;

import com.example.MyProjectWithSecurity.data.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query("from Book where author.id=:id_author")
    List<Book> selectBookIdAuthor(@Param("id_author") Integer id_author);

    @Query("from Book where tag.idteg=:id_tag")
    List<Book> selectBookIdTag(@Param("id_tag") Integer id_tag);

    public Page<Book> findBooksByTagIdteg(Integer id, Pageable nextPage);

//    @Query("select b.id, b.id_author, a.author, b.title, b.priceOld, b.price from Book as b inner join Authors a on b.id_author=a.id")
//    List<AuthorsBook> selectAllBooks();
//NEW BOOK REST REPOSITORY COMMANDS
    public List<Book> findBooksByAuthorAuthorContaining(String nameAuthor);
    public List<Book> findBooksByTitleContaining(String bookTitle);
    public List<Book> findBooksByPriceOldBetween(Integer min, Integer max);
    public List<Book> findBooksByPriceOldIs(Integer price);
    @Query("from Book where isbestseller = 1")
    public List<Book> getBestsellers();
    @Query(value="SELECT * FROM books WHERE price_old = (SELECT MAX(price_old) FROM books)", nativeQuery=true)
    public List<Book> getBooksWithMaxDiscount();

    public Page<Book> findBooksByDateBetween(LocalDate now, LocalDate max, Pageable nextPage);
    //public Page<Book> findBooksBy(LocalDate date, Pageable nextPage);
    public List<Book> findBooksByDateBetween(LocalDate min, LocalDate now);

    public Page<Book> findBooksByTitleContaining(String bookTitle, Pageable nextPage);

    public Page<Book> findBooksByIsbestsellerEquals(Byte num, Pageable nextPage);

    public Book findBookBySlug(String slug);

    public  Book findBookById(Integer Id);

    public List<Book> findBooksBySlugIn(String[] slugs);
}
