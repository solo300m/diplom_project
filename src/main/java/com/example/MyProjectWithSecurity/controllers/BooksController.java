package com.example.MyProjectWithSecurity.controllers;

import com.example.MyProjectWithSecurity.Repositories.*;
import com.example.MyProjectWithSecurity.Service.BookService;
import com.example.MyProjectWithSecurity.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
//@RequestMapping("/root")
public class BooksController {

    public final BookRepository bookRepository;
    private final ResorceStorage storage;
    private String oldSlug;
    private BookService bookService;
    private UserRepository userRepository;
    private Book2UserRepository book2UserRepository;
    private BookReviewRepository bookReviewRepository;
    private BookReviewLikeRepository bookReviewLikeRepository;
    private BookRatingRepository bookRatingRepository;



    @Autowired
    public BooksController(BookRepository bookRepository, ResorceStorage storage,
                           BookService bookService, UserRepository userRepository,
                           Book2UserRepository book2UserRepository, BookReviewRepository bookReviewRepository,
                           BookReviewLikeRepository bookReviewLikeRepository, BookRatingRepository bookRatingRepository) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.storage = storage;
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    @ModelAttribute("postponedCount")
    public Integer postponedCound(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User>filteredList = list.stream()
                    .filter(c->c.getBook_file_type().getCode().equals("KEPT"))
                    .collect(Collectors.toList());
            List<Book> booksPost = new ArrayList<>();
            for(Book2User book : filteredList)
                booksPost.add(book.getBook());
            return booksPost.size();
        }
        else{
            return 0;
        }
    }
    @ModelAttribute("catCount")
    public Integer catCound(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            List<Book2User>list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User>filteredList = list.stream()
                    .filter(c->c.getBook_file_type().getId() == 2)
                    .collect(Collectors.toList());
            List<Book> booksPost = new ArrayList<>();
            for(Book2User book : filteredList)
                booksPost.add(book.getBook());
            return booksPost.size();
        }
        else{
            return 0;
        }
    }

    @ModelAttribute("userCustom")
    public String userCustom(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);

            return user.getUser_name();
        }
        else{
            return "не определен";
        }
    }

    public Integer getLikeCount(Integer likeId, Integer filter){
        List<Book_Review_Like> list = bookReviewLikeRepository.findBook_Review_LikesById(likeId)
                .stream().filter(c->c.getValue()==filter).collect(Collectors.toList());
        return list.size();
    }


    @GetMapping("/root/{sl}")
    public String getBookSlug(@PathVariable("sl") String sl, Model model, HttpServletResponse response){
        oldSlug = sl;
        Logger.getLogger(BooksController.class.getName()).info("SlugBook Page open  "+oldSlug);
        Book book = bookRepository.findBookBySlug(sl);
        model.addAttribute("slugBook", book);
        model.addAttribute("reviewBook", bookReviewRepository.findBook_ReviewsByBookIs(book));
        //Cookie cookie3 = WebUtils.getCookie(request,"statusRating");
        Logger.getLogger(BooksController.class.getSimpleName()).info("Первичный контроллер");
        model.addAttribute("Rating1",bookRatingRepository.findBookRatingsByBookIdAndRating(bookRepository.findBookBySlug(sl).getId(),1).size());
        model.addAttribute("Rating2",bookRatingRepository.findBookRatingsByBookIdAndRating(bookRepository.findBookBySlug(sl).getId(),2).size());
        model.addAttribute("Rating3",bookRatingRepository.findBookRatingsByBookIdAndRating(bookRepository.findBookBySlug(sl).getId(),3).size());
        model.addAttribute("Rating4",bookRatingRepository.findBookRatingsByBookIdAndRating(bookRepository.findBookBySlug(sl).getId(),4).size());
        model.addAttribute("Rating5",bookRatingRepository.findBookRatingsByBookIdAndRating(bookRepository.findBookBySlug(sl).getId(),5).size());
        model.addAttribute("allRating", book.solverBookRating());
        model.addAttribute("allCountRating",book.getBookRatings().size());
        Integer r = book.solverBookRating();
        Cookie cookie = new Cookie("allRating", ""+r+"");
        cookie.setPath("/");
        //cookie.setMaxAge(60*60);
        response.addCookie(cookie);
        return "/books/slug";
    }

    @PostMapping("/root/{sl}/img/main")
    public String saveNewBookImage  (@RequestParam("file") MultipartFile file, @PathVariable("sl") String sl) throws IOException {
        Logger.getLogger(BooksController.class.getName()).info("Вторичный контроллер Загружена страница со slug = "+sl);
        String savePath = storage.saveNewBookImage(file,sl);
        Book bookToUpdate = bookRepository.findBookBySlug(sl);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);

        return("redirect:/root/"+sl);
    }

    @GetMapping("/root/slug/{down}")
    public String downloadFilePage(@PathVariable("down") Integer down, Model model){
        model.addAttribute("bookDown", bookService.getBookOfId(down));
        model.addAttribute("fileDown", bookService.getBookOfId(down).getBook_files());
        Logger.getLogger(BooksController.class.getSimpleName()).info("Третий контроллер загрузки файлов");
        return "/books/fileDownload.html";
    }
    @GetMapping("/root/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: "+path);
        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: "+mediaType);
        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: "+data.length);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/root/comment/{newC}")
    public String handlerNewComment(@PathVariable("newC") String newC, Model model){
        model.addAttribute("bookComment",bookRepository.findBookBySlug(newC));
        Logger.getLogger(BooksController.class.getSimpleName()).info("Четвертый контроллер комментарий");
        return "/books/commentNew";
    }

    @PostMapping("/books/comment/{sl}")
    public String handleAddNewComment(Model model, @PathVariable("sl") String sl, @RequestParam String comment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Logger.getLogger(BooksController.class.getSimpleName()).info("Пятый контроллер комментарий");
        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            Book book = bookRepository.findBookBySlug(sl);
            Book_Review book_review = new Book_Review();
            String comment_mini = "";
            String comment_tail = "";
            String test = "";
            if(comment.length()>254){
                test = comment.substring(0,254);
                int position = test.lastIndexOf(".");
                if ((position)>=0) {
                    comment_mini = comment.substring(0, position + 1);
                    comment_tail = comment.substring(++position).trim();
                }
                else {
                    position =  test.lastIndexOf(",");
                    if(position >=0){
                        comment_mini = comment.substring(0, position + 1);
                        comment_tail = comment.substring(++position).trim();
                    }
                    else{
                        position =  test.lastIndexOf(" ");
                        if(position>=0) {
                            comment_mini = comment.substring(0, position + 1);
                            comment_tail = comment.substring(++position).trim();
                        }
                        else {
                            comment_mini = comment.substring(0, 254);
                            comment_tail = comment.substring(254);
                        }
                    }
                }
            }
            else{
                comment_mini = comment;
            }
            book_review.setText(comment_mini);
            book_review.setTextTail(comment_tail);
            //book_review.setBook_review_likes(new ArrayList<>());
            book_review.setTime(LocalDateTime.now());
            book_review.setBook(book);
            book_review.setUser(user);
            bookReviewRepository.save(book_review);
            return "redirect:/root/"+sl;
        }
        else{
            Logger.getLogger(BooksController.class.getSimpleName()).info("Open Signin page");
            return "/signin";
        }
    }

    @PostMapping("/books/like/{sl}/{comSlug}")
    public String handleLakeReview(@PathVariable String sl, @PathVariable Integer comSlug){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Logger.getLogger(BooksController.class.getSimpleName()).info("Шестой контроллер лайки");
        if(!username.equals("anonymousUser")) {
            Book_Review_Like book_review_like = new Book_Review_Like();
            bookReviewLikeRepository.save(book_review_like);
            book_review_like.setBook_review(bookReviewRepository.findBook_ReviewById(comSlug));
            book_review_like.setTime(LocalDateTime.now());
            book_review_like.setValue(1);
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            book_review_like.setUser(user);
            //book_review_like.setId(2);
            bookReviewLikeRepository.save(book_review_like);

            return "redirect:/root/"+sl;
        }else
            return "/signin";
    }

    @PostMapping("/books/dizlike/{sl}/{comSlug}")
    public String handleDizLakeReview(@PathVariable String sl, @PathVariable Integer comSlug,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Logger.getLogger(BooksController.class.getSimpleName()).info("Седьмой контроллер дизлайки");
        if(!username.equals("anonymousUser")) {
            Book_Review_Like book_review_like = new Book_Review_Like();
            bookReviewLikeRepository.save(book_review_like);
            book_review_like.setBook_review(bookReviewRepository.findBook_ReviewById(comSlug));
            book_review_like.setTime(LocalDateTime.now());
            book_review_like.setValue(0);
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            book_review_like.setUser(user);
            bookReviewLikeRepository.save(book_review_like);

            return "redirect:/root/" + sl;
        }else
            return "/books/signin";
    }

    @PostMapping("/root/books/changeBookStatus/{bookId}/{value}")
    public String handleRatingSet(@PathVariable("bookId") Integer bookId, @PathVariable("value") Integer value,
                                  HttpServletResponse response, HttpServletRequest request, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Logger.getLogger(BooksController.class.getSimpleName()).info("Девятый контроллер рейтинг");
        if(!userName.equals("anonymousUser")) {
            BookRating bookRating = new BookRating();
            bookRating.setBook(bookRepository.findBookById(bookId));
            bookRating.setRating(value);
            User user = userRepository.findUserByEmailContains(userName);
            bookRating.setUser(user);
            bookRatingRepository.save(bookRating);
            model.addAttribute("isStatusRating", 1);
            Logger.getLogger(BooksController.class.getSimpleName()).info("Start controller changeBookStatus" + bookId + " " + value);
            Cookie cookie = new Cookie("statusRating", "1");
            cookie.setPath("/");
            //cookie.setMaxAge(60*60);
            response.addCookie(cookie);
//______Эксперементы с cookie_______________________________________________________________________________________________
            Cookie cookie1 = WebUtils.getCookie(request,"statusRating");//Получение cookie из хранилища
            Logger.getLogger(BooksController.class.getSimpleName()).info("Используем значение cookie - "+cookie1.getName()+" "+cookie1.getValue());
            List<Cookie> arr = Arrays.stream(request.getCookies()).collect(Collectors.toList());//получение массива cookie из хранилища

            for(Cookie s : arr)
                Logger.getLogger(BooksController.class.getSimpleName()).info("Используем значение cookie - "+s.getName()+" "+s.getValue());
            return "redirect:/root/"+bookRepository.findBookById(bookId).getSlug();
        }
        else {
            Cookie cookie = new Cookie("statusRating", "0");
            cookie.setPath("/");
            //cookie.setMaxAge(60*60);
            response.addCookie(cookie);
            return "redirect:/root/" + bookRepository.findBookById(bookId).getSlug();
        }
    }
}
