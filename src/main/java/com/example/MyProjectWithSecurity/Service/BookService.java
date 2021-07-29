package com.example.MyProjectWithSecurity.Service;

import com.example.MyProjectWithSecurity.Repositories.*;
import com.example.MyProjectWithSecurity.data.*;
import com.example.MyProjectWithSecurity.Repositories.*;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.errs.BookstoreApiWrongParameterException;
import com.example.MyProjectWithSecurity.data.Genre;
import com.example.MyProjectWithSecurity.data.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final JdbcTemplate jdbcTemplate;
    private BookRepository bookRepository;
    private AuthorsRepository authorsRepository;
    //private CustomerRepository customerRepository;
    private HibernateService hibernateService;
    private TegsRepository tegsRepository;
    private GenreRepository genreRepository;


    public BookService(JdbcTemplate jdbcTemplate, BookRepository bookRepository, AuthorsRepository authorsRepository, HibernateService hibernateService, TegsRepository tag, GenreRepository genreRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
       //this.customerRepository = customerRepository;
        this.hibernateService = hibernateService;
        this.tegsRepository = tag;
        this.genreRepository = genreRepository;
    }

    public List<Book> getBookData(){
        List<Book>books = bookRepository.findAll();

        return books;
    }

    public List<Book> getBooksByAuthor(String authorName){
        return bookRepository.findBooksByAuthorAuthorContaining(authorName);
    }
    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if(title.equals("") || title.length()<=1){
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        }
        else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if(data.size() > 0){
                return data;
            }
            else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }
    public List<Book> getBooksWithPriceBetween(Integer min, Integer max){
        return bookRepository.findBooksByPriceOldBetween(min,max);
    }
    public List<Book> getBooksWithPrice(Integer price){
        return bookRepository.findBooksByPriceOldIs(price);
    }
    public List<Book> getBooksWithMaxPrice(){
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers(){
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfNewBooks(Integer offset, Integer limit){
        LocalDate now = LocalDate.now();
        LocalDate min = now.minusDays(30L);
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findBooksByDateBetween(min,now,nextPage);
    }
    public List<Book> getListOfNewBooks(){
        LocalDate now = LocalDate.now();
        LocalDate min = now.minusDays(30L);
        return bookRepository.findBooksByDateBetween(min,now);
    }

    public Page<Book> getPageOfBestseller(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        Byte num = new Byte((byte) 1);
        return bookRepository.findBooksByIsbestsellerEquals(num,nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWorld, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findBooksByTitleContaining(searchWorld,nextPage);
    }

    public List<Tag> getTegs(){
        return tegsRepository.findAll();
    }

    public String getTegName(Integer teg){
        return tegsRepository.selectTag(teg).getTegName();
    }

    public List<Book> getBookOfTeg(Integer id){
        return bookRepository.selectBookIdTag(id);
    }

    public Page<Book> getPageOfSearchBooksTeg(Integer teg, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findBooksByTagIdteg(teg,nextPage);
    }

    public Integer averigMatTegListSize(Map<Tag,List<Book>>tagListMap){
        Integer rezult = 0;
        List<Tag>tagList = getTegs();
        for(Tag tg:tagList){
            rezult = rezult+tagListMap.get(tg).size();
        }
        rezult = (int)rezult / tagListMap.size();
        return rezult;
    }
    public Map<Tag,List<Book>> getMapTegs(List<Book>bookList){
        Map<Tag,List<Book>> treeMap;
        treeMap = bookList.stream().collect(Collectors.groupingBy(Book::getTag));
        /*List<Tag>tagList = getTegs();
        for(Tag t:tagList){
            int count = treeMap.get(t).size();
            Logger.getLogger(BookService.class.getName()).info("Ключ "+t.getTegName()+" количество "+treeMap.get(t).size());
        }*/
        return treeMap;
    }

    public List<Genre> getGenreAll(){
        return genreRepository.findAll();
    }

    public Book getBookOfId(Integer id){
        return bookRepository.findBookById(id);
    }


    //Замена на функцию из BookRepository
    /*public List<Book> getBookData() {
       List<Book> books = jdbcTemplate.query("SELECT books.id, id_author, price, price_old, title, author\n" +
               "\tFROM public.books, public.authors WHERE id_author = authors.id",(ResultSet rs, int rowNum)->{
           Book book = new Book();
           book.setId(rs.getInt("id"));
           book.setId_author(rs.getInt("id_author"));
           book.setAuthor(rs.getString("author"));
           book.setTitle(rs.getString("title"));
           book.setPriceOld(rs.getString("price_old"));
           book.setPrice(rs.getString("price"));
           return book;
       });
       return new ArrayList<>(books);
    }*/

    //Замена на функцию из BookRepository
    /*public List<Book> getIdAuthorBook(Integer id){
        List<Book> books = jdbcTemplate.query("SELECT books.id, id_author, price, price_old, title, author " +
                "FROM books, authors WHERE id_author="+id+"",(ResultSet rs, int rowNum)->{
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setId_author(rs.getInt("id_author"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setPriceOld(rs.getString("price_old"));
            book.setPrice(rs.getString("price"));
            return book;
        });
        return new ArrayList<>(books);
    }*/
//    public List<Authors> getAuthorsData(char a) {
//        List<Authors> authors = jdbcTemplate.query("SELECT * FROM authors WHERE author LIKE '"+a+"%'", (ResultSet rs, int rowNum) -> {
//            Authors auth = new Authors();
//            auth.setId(rs.getInt("id"));
//            auth.setAuthor(rs.getString("author"));
//            return auth;
//        });
//        if(authors.size()!=0)
//            return new ArrayList<>(authors);
//        else{
//            authors.add(new Authors("нет данных"));
//            return new ArrayList<>(authors);
//        }
//    }
    /*public List<Authors> getAuthorsList(){
        return authorsRepository.customAuthorsAll();
    }*/

}
