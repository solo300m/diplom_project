package com.example.MyProjectWithSecurity.Service;

import com.example.MyProjectWithSecurity.Repositories.AuthorsRepository;
import com.example.MyProjectWithSecurity.Repositories.BookRepository;
import com.example.MyProjectWithSecurity.data.*;
import com.example.MyProjectWithSecurity.data.Authors;
import com.example.MyProjectWithSecurity.data.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final JdbcTemplate jdbcTemplate;
    private BookRepository bookRepository;
    private AuthorsRepository authorsRepository;
    private BookService bookService;

    public AuthorService(JdbcTemplate jdbcTemplate, BookRepository bookRepository, AuthorsRepository authorsRepository, BookService bookService) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookRepository = bookRepository;
        this.authorsRepository = authorsRepository;
        this.bookService = bookService;
    }

    public List<Book> getIdAuthorBook(Integer id){
        List<Book>books = bookRepository.selectBookIdAuthor(id);
        for(Book b: books)
            Logger.getLogger(AuthorService.class.getName()).info(b.toString());
        return new ArrayList<>(books);
    }

    public List<Authors> getAuthorsList(){
        return authorsRepository.customAuthorsAll();
    }
//    public List<Authors> getAuthorsList() {
//        List<Authors> authors = jdbcTemplate.query("SELECT * FROM authors ", (ResultSet rs, int rowNum) -> {
//            Authors auth = new Authors();
//            auth.setId(rs.getInt("id"));
//            auth.setAuthor(rs.getString("author"));
//            auth.setBiography(rs.getString("biography"));
//            auth.setPhoto(rs.getString("photo"));
//            return auth;
//        });
//        return new ArrayList<>(authors);
//    }
    public Authors getAuthorId(Integer id){
        List<Authors>list = getAuthorsList();
//        if(list.size() == 0) {
//            setAuthorsData(getBookData());
//            list = getAuthorsList();
//        }
        List<Authors>temp = list.stream().filter(w->w.getId()==id).collect(Collectors.toList());
        return (temp.get(0));
    }
    public Map<String,List<Book>> getAuthorName(String name){
        List<Authors>list = getAuthorsList();
//        if(list.size() == 0) {
//            setAuthorsData(getBookData());
//            list = getAuthorsList();
//        }
        List<Book>tempBooks = bookService.getBookData();
        List<Book>tBooks = new ArrayList<>();
        List<Authors>temp = list.stream().filter(w->w.getAuthor().contains(name)).collect(Collectors.toList());
        for(Authors a: temp){
            for (Book b: tempBooks){
                if(b.getAuthor().getAuthor().equals(a.getAuthor())){
                    tBooks.add(b);
                }
            }
        }
        Map<String,List<Book>>books = tBooks.stream()
                .collect(Collectors.groupingBy(w->w.getAuthor().getAuthor()));
        return books;
    }
    public Map<String,List<Authors>> getMapAuthors(List<Authors> listA){
        Map<String,List<Authors>> treeMap;
        treeMap = listA.stream()
                .sorted(Comparator.comparing(Authors::getAuthor))
                .collect(Collectors.groupingBy(authors -> authors.getAuthor().substring(0,1)));
        /*for(Map.Entry<String,List<Authors>> at: treeMap.entrySet()){
            Logger.getLogger(BookService.class.getName()).info(at.getKey());
            for(Authors authors: at.getValue()){
                Logger.getLogger(BookService.class.getName()).info(authors.getAuthor());
            }
        }*/
        return treeMap;
    }
    public TreeMap<Integer,List<Authors>> getMapId(List<Authors> listA){
        Map<Integer,List<Authors>> treeMap = new TreeMap<>();
        treeMap = listA.stream()
                .sorted(Comparator.comparing(Authors::getAuthor))
                .collect(Collectors.groupingBy(authors -> authors.getId()));
        /*for(Map.Entry<String,List<Authors>> at: treeMap.entrySet()){
            Logger.getLogger(BookService.class.getName()).info(at.getKey());
            for(Authors authors: at.getValue()){
                Logger.getLogger(BookService.class.getName()).info(authors.getAuthor());
            }
        }*/
        return new TreeMap<>(treeMap);
    }
    public void setAuthorsData(List<Book> list){
        HashSet<String> auth = new HashSet<>();
        for(int i = 0; i<list.size();i++){
            auth.add(list.get(i).getAuthor().getAuthor());
        }
        //MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        List<Authors> test = authorsRepository.customAuthorsAll();
//        List<Authors> test = jdbcTemplate.query("SELECT * FROM authors",(ResultSet rs, int rowNum)->{
//            Authors aut = new Authors();
//            aut.setId(rs.getInt("id"));
//            aut.setAuthor(rs.getString("author"));
//            return aut;
//        });
        if(test.size()==0) {
            Iterator<String> s = auth.stream().iterator();
            for (Iterator<String> it = s; it.hasNext(); ) {
                Authors author = new Authors();
                String s1 = it.next();
                //parameterSource.addValue("aut",auth.get(i));
                jdbcTemplate.update("INSERT INTO authors(author,biography,photo) VALUES (?,?,?)", s1,author.getBiography(),author.getPhoto());
                //Logger.getLogger(BookService.class.getName()).info("Updated author " + s1);
            }
        }
    }
//    public void updateBookIdAuthors(){
//        List<Authors>list = new ArrayList<>();
//        list = jdbcTemplate.query("SELECT * FROM authors",(ResultSet rs, int rowNum)->{
//            Authors authors = new Authors();
//            authors.setId(rs.getInt("id"));
//            authors.setAuthor(rs.getString("author"));
//            authors.setBiography(rs.getString("biography"));
//            authors.setPhoto(rs.getString("photo"));
//            return authors;
//        });
//        for(Authors a: list){
//            jdbcTemplate.update("UPDATE books SET id_author = ? WHERE author = ? AND id_author = 0",a.getId(),a.getAuthor());
//        }
//    }
}
