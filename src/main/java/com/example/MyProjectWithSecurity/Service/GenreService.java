package com.example.MyProjectWithSecurity.Service;

import com.example.MyProjectWithSecurity.Repositories.BookTwoGenreRepository;
import com.example.MyProjectWithSecurity.Repositories.GenreRepository;
import com.example.MyProjectWithSecurity.data.Book;
import com.example.MyProjectWithSecurity.data.Book2Genre;
import com.example.MyProjectWithSecurity.data.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class GenreService {
    private GenreRepository genreRepository;
    private BookTwoGenreRepository bookTwoGenreRepository;

    public GenreService(GenreRepository genreRepository, BookTwoGenreRepository bookTwoGenreRepository) {
        this.genreRepository = genreRepository;
        this.bookTwoGenreRepository = bookTwoGenreRepository;
    }

    public Map<Genre, List<Book>> getMapBook2Genre(){
        List<Book2Genre>b2g = bookTwoGenreRepository.findAll();
        List<Genre>genres = genreRepository.findAll();
        Map<Genre,List<Book>> genreListMap = new TreeMap<>();
        for(Genre g:genres){
            for(Book2Genre b:b2g){
                if(g.getId()==(b.getGenre().getId())){
                    if(genreListMap.get(g)!=null){
                        genreListMap.get(b.getGenre()).add(b.getBook());
                    }
                    else{
                        List<Book>list = new ArrayList<>();
                        genreListMap.put(b.getGenre(),list);
                        genreListMap.get(b.getGenre()).add(b.getBook());
                    }
                }
            }
        }
//        for(Genre g2:genres ){
//            int count = genreListMap.get(g2).size();
//            Logger.getLogger(GenreService.class.getName()).info(g2.getName()+" "+count);
//        }
        return genreListMap;
    }

    public Genre getGenreOfId(Integer id){
        return genreRepository.selectGenreBy(id);
    }

    public List<Book> getListBookOfGenre(Map<Genre,List<Book>>listMap, Integer genre){
        Genre gen = getGenreOfId(genre);
        return listMap.get(gen);
    }

    public Page<Book2Genre> getPageBookOfGenre(Integer genre,Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookTwoGenreRepository.findBook2GenreByGenreId(genre,nextPage);
    }
}
