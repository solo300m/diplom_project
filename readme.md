#Readme для домашнего задания 7.6 
##Задание 1 Добавление книг в категорию «Отложенное»
___
* контроллер кнопки Купить на странице <http://localhost:8085/root/{slug}>
1. Файл BookshopCartController.java
~~~
@GetMapping("/cart/{sl}")
    public String handleCartAdd(@PathVariable("sl") String sl, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if(!username.equals("anonymousUser")){
            Book book = bookRepository.findBookBySlug(sl);
            Logger.getLogger(BookPostponedController.class.getName()).info("SlugBookPostponed Page open  "+sl);
            Book2User book2User = new Book2User();
            book2User.setBook(book);
            book2User.setUser(userRepository.findUserByEmailContains(username));
            book2User.setBook_file_type(book2UserTypeRepository.findBook2User_typeByCode("CART"));
            book2User.setTime(LocalDateTime.now());
            book2UserRepository.save(book2User);
            model.addAttribute("slugBook", book);
            List<Book2User> listNull = new ArrayList<>();
            listNull = book2UserRepository.findBook2UsersByBookIs(bookRepository.findBookById(null));
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Null records in BD are "+listNull.size());
            if(listNull.size()!=0)
                book2UserRepository.deleteAll(listNull);

            return "/books/slug";
        }else{
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current is anonymousUser");
            return "/signin";
        }
    }
~~~
* контроллер кнопки Отложено на странице <http://localhost:8085/root/{slug}>
1. файл - BookPosponedController.java  
~~~
   @GetMapping("/postponed/{sl}")
    public String handlePostponed(@PathVariable("sl") String sl, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if(!username.equals("anonymousUser")){
            Book book = bookRepository.findBookBySlug(sl);
            Logger.getLogger(BookPostponedController.class.getName()).info("SlugBookPostponed Page open  "+sl);
            Book2User book2User = new Book2User();
            book2User.setBook(book);
            book2User.setUser(userRepository.findUserByEmailContains(username));
            book2User.setBook_file_type(book2UserTypeRepository.findBook2User_typeByCode("KEPT"));
            book2User.setTime(LocalDateTime.now());
            book2UserRepository.save(book2User);
            model.addAttribute("slugBook", book);
            List<Book2User> listNull = new ArrayList<>();
            listNull = book2UserRepository.findBook2UsersByBookIs(bookRepository.findBookById(null));
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Null records in BD are "+listNull.size());
            if(listNull.size()!=0)
                book2UserRepository.deleteAll(listNull);

            BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(username);
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current email is "+username);
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current name is "+bookstoreUser.toString());
            //book2UserRepository.deleteByBookIs(bookRepository.findBookById(null));
            return "/books/slug";
        }
        else{
            Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Current is anonymousUser");
            return"signin";
        }
    }

  ~~~
* переписаны html-теги кнопок Отложено и Купить

1. Отложить файл ../books/slug.html
~~~html
<div class="ProductCard-cartElement btn btn_primary btn_outline">
    <a href="#" th:href="@{/postponed/{sl}(sl = ${slugBook.slug})}"><span ><img
          src="/assets/img/icons/heart.svg" alt="heart.svg" style="width: 20px;
          height: 18px; margin: 5px; margin-left: 10px; margin-bottom: 0"/></span>
          <span >Отложить</span></a>
</div>
~~~
2. Купить ../books/slug.html
~~~html
<div class="ProductCard-cartElement btn btn_primary btn_outline">
    <a href="#" th:href="@{/books/cart/{sl}(sl = ${slugBook.slug})}"><span ><img
           src="/assets/img/icons/shopping-basket.svg" alt="heart.svg" style="width: 20px;
           height: 18px; margin: 5px; margin-left: 10px; margin-bottom: 0"/></span>
           <span >Купить</span></a>
</div>
~~~
* внесены изменения в html код страницы 
1. Отложить, файл ../cart.html
~~~html
<div class="Cart-btn btn btn_primary btn_outline">
    <a href="#" th:href="@{/books/cart/postponed/{sl}(sl = ${book.slug})}"><span ><img
        src="/assets/img/icons/shopping-basket.svg" alt="shopping-basket.svg" style="width: 20px;
        height: 18px; margin: 5px; margin-left: 10px; margin-bottom: 0" /></span>
<span class="btn-content">Отложить</span></a>
</div>
~~~
2. Удалить, файл ../cart.html
~~~html
<div class="Cart-btn btn btn_primary btn_outline">
    <a href="#" th:href="@{/books/cart/del/{sl}(sl = ${book.slug})}"><span ><img
        src="/assets/img/icons/trash.svg" alt="shopping-basket.svg" style="width: 20px;
        height: 18px; margin: 5px; margin-left: 10px; margin-bottom: 0" /></span>
    <span class="btn-content">Удалить</span></a>
</div>
~~~
* добавлен контроллер Отложить на странице <http://localhost:8085/books/cart/{sl}>
1. файл BookshopCartController.java
~~~
 @GetMapping("/cart/postponed/{sl}")
    public String handleCartToPostponed(@PathVariable("sl") String sl, Model model){
        Book book = bookRepository.findBookBySlug(sl);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = new User();
        user = userRepository.findUserByEmailContains(username);
        List<Book2User>allListUser = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User>filtListUser = allListUser.stream().filter(c->c.getBook().getId() == book.getId() && c.getBook_file_type().getId() == 2).collect(Collectors.toList());
        if(filtListUser.size() >= 1){
            Book2User book2User = new Book2User();
            book2User = filtListUser.get(0);
            Book2User_type book2User_type = new Book2User_type();
            book2User_type.setId(1);
            book2User.setBook_file_type(book2User_type);
            book2UserRepository.save(book2User);
        }

        List<Book2User> book2UserList = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User> filteredList = book2UserList.stream().filter(c->c.getBook_file_type().getId()==2).collect(Collectors.toList());

        List<Book> bookList = new ArrayList<>();
        for(Book2User btu : filteredList)
            bookList.add(btu.getBook());
        model.addAttribute("bookCart", bookList);
        return "/cart";
    }
~~~
* добавлен контроллер Удалить на странице <http://localhost:8085/books/cart/{sl}>
1. файл BookshopCartController.java
~~~
@GetMapping("/cart/del/{sl}")
    public String handleCartDel(@PathVariable("sl") String sl, Model model){
        Book book = bookRepository.findBookBySlug(sl);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = new User();
        user = userRepository.findUserByEmailContains(username);
        List<Book2User>allListUser = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User>filtListUser = allListUser.stream().filter(c->c.getBook().getId() == book.getId() && c.getBook_file_type().getId() == 2).collect(Collectors.toList());
        if(filtListUser.size() >= 1){
            Book2User book2User = new Book2User();
            book2User = filtListUser.get(0);

            book2UserRepository.delete(book2User);
        }

        List<Book2User> book2UserList = book2UserRepository.findBook2UsersByUser(user);
        List<Book2User> filteredList = book2UserList.stream().filter(c->c.getBook_file_type().getId()==2).collect(Collectors.toList());

        List<Book> bookList = new ArrayList<>();
        for(Book2User btu : filteredList)
            bookList.add(btu.getBook());
        model.addAttribute("bookCart", bookList);
        return "/cart";
    }
~~~
##Задание 2 Механизм оценок и рейтингов книг
* Доработан контроллер файла ../index.html

1. Фрагмет ../index.html где используется контроллер

~~~html
<div class="Card"><a class="Card-picture" th:href="@{/root/{sl}(sl=${book.getSlug()})}"><img
                        th:src="${book.getImage()}" alt="card.jpg"/>
~~~
2. файл BooksController.java
~~~
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

~~~
Реализована возможность работы с cookie
* Реализованы алгоритмя вывода в шаблон header (../template/fragments/header_fragment.html) расчета количества отложенных книг и карзины.

1. Отложенные, файл BooksController.java
~~~
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
~~~
2. В карзине, файл BooksController.java
~~~
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
~~~
* Фиксация оценки и установка рейтинга
1. Файл JavaScript ../resources/static/assets/js/my_script_test.js
~~~
$(function(){
    var $ProductAssesment = $('.ProductCard-assessment');
    $ProductAssesment.find('.Rating input').on('change', function (e) {
        var $this = $(this), check = $this.prop('checked');
        var $Book_Id = $this.closest('.Rating').data('bookid');
        $('.Book_Id').attr('bookId',$Book_Id);
        var $Book_Rating = $this.val();
        $('.Book_Rating').attr('bookRating',$Book_Rating);
        $('.Status_Rating').attr('statusRating', 1);
        //alert(document.cookie);
    });
    var $status = document.cookie.split('; ').find(row=>row.startsWith('allRating')).split('=')[1].trim();
    //alert("Значение cookie allRating = "+ $status)
    switch ($status){
        case '1':
            //alert("Значение cookie allRating = "+ $status);
            $('#s1').addClass('Rating-star_view');
            break;
        case '2':
           // alert("Значение cookie allRating = "+ $status);
            $('#s1').addClass('Rating-star_view');
            $('#s2').addClass('Rating-star_view');
            break;
        case '3':
           // alert("Значение cookie allRating = "+ $status);
            $('#s1').addClass('Rating-star_view');
            $('#s2').addClass('Rating-star_view');
            $('#s3').addClass('Rating-star_view');
            break;
        case '4':
           // alert("Значение cookie allRating = "+ $status);
            $('#s1').addClass('Rating-star_view');
            $('#s2').addClass('Rating-star_view');
            $('#s3').addClass('Rating-star_view');
            $('#s4').addClass('Rating-star_view');
            break;
        case '5':
            //alert("Значение cookie allRating = "+ $status);
            $('#s1').addClass('Rating-star_view');
            $('#s2').addClass('Rating-star_view');
            $('#s3').addClass('Rating-star_view');
            $('#s4').addClass('Rating-star_view');
            break;
        default:
            //alert("Значение cookie allRating = "+ $status);
            break;
    }
});
~~~
2. Контроллер, файл BooksController.java
~~~
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
~~~
##Задание 3. Функционал отзывов на книгу
* реализована страница ../templates/books/commentNew.html
* разработаны следующие контроллеры - файл BooksController.java:
1. Контроллер вызывающие commentNew.html
~~~
@GetMapping("/root/comment/{newC}")
    public String handlerNewComment(@PathVariable("newC") String newC, Model model){
        model.addAttribute("bookComment",bookRepository.findBookBySlug(newC));
        Logger.getLogger(BooksController.class.getSimpleName()).info("Четвертый контроллер комментарий");
        return "/books/commentNew";
    }
~~~
2. Контроллер управляющий форматированием и сохранением коммента в базе
~~~
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
~~~
3. Контроллеры устанавливающие лайки и дизлайки и сохраняющие их в таблице БД
~~~
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

~~~
* Добавлена таблица BookRating.java
~~~
package com.example.MyProjectWithSecurity.data;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="bookrating")
public class BookRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="book_id", referencedColumnName = "id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private Integer rating;

    public BookRating() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

}

~~~
## Описание процесса авторизации в базе
* В базе уже установлен плагин Spring Security
  
* Для теста использую две учетные записи login: solo300m@yandex.ru pass: 123456
login: olga@yandex.ru pass: 123456
  
* Порт базы данных изменен с 5434 на 5432 из-за особенностей поведения на моей локальной машине
## Изменения, внесенные в процессе доработки по замечаниям
* Проведено разделение серверной части от контроллеров

* Создан класс AnalitikService.java файл - /Service/AnalitikService.java
~~~
package com.example.MyProjectWithSecurity.Service;

import com.example.MyProjectWithSecurity.Repositories.*;
import com.example.MyProjectWithSecurity.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalitikService {

    private final UserRepository userRepository;
    private final Book2UserRepository book2UserRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;

    @Autowired
    public AnalitikService(UserRepository userRepository, Book2UserRepository book2UserRepository,
                           BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository,
                           BookRepository bookRepository, BookRatingRepository bookRatingRepository) {
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRepository = bookRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    /**
     * Метод autorizationGetUser проверяет авторизирован ли пользователь и возвращает его
     * в виде объекта User. В случае, если пользователь не авторизирован возвращает null
     * @return - объект User или null
     */
    public User autorizationGetUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if(!username.equals("anonymousUser")) {
            User user = new User();
            user = userRepository.findUserByEmailContains(username);
            return user;
        }
        else
            return null;
    }

    /**
     * Метод getUserBooksKept возвращает список объектов Book2User соответствующему отбору по параметрам
     * @param user - объект User
     * @param bookStatus - строка со значениями enum Type_Name{
     *     KEPT(1),
     *     CART(2),
     *     PAID(3),
     *     ARCHIVED(4);
     * @return - список, отфильтрованный по User и bookStatus или null
     * */
    public List<Book2User> getUserBooksStatus(User user, String bookStatus){
        if(user != null && bookStatus != "") {
            List<Book2User> list = book2UserRepository.findBook2UsersByUser(user);
            List<Book2User> filteredList = list.stream()
                    .filter(c -> c.getBook_file_type().getCode().equals(bookStatus))
                    .collect(Collectors.toList());
            return filteredList;
        }
        else{
            List<Book2User> listNull = new ArrayList<>();
            return listNull;
        }
    }

    /**
     * Метод getBooksFromBook2User возвращает список объектов Book из списка объектов Book2User
     * @param book2User - список объектов Book2User
     * @return - список объектов Book
     */
    public List<Book> getBooksFromBook2User(List<Book2User> book2User){
        if(book2User != null || book2User.size() != 0){
            List<Book> booksPost = new ArrayList<>();
            for(Book2User book : book2User)
                booksPost.add(book.getBook());
            return booksPost;
        }
        else{
           return new ArrayList<>();
        }
    }

    /**
     * Метод обрабатывает и сохраняет в базе данных комментарии of user о книге of book
     * @param comment - строка комментарий типа String
     * @param book - объект типа Book
     * @param user - объект типа User
     */
    public void bookReviewSave(String comment, Book book, User user){
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
    }

    /**
     * Метод обработки и сохранения лайков к комментариям
     * @param idReview - id комментария, сохраненного в БД
     * @param user - объект типа User
     */
    public void likeReviewSave(Integer idReview, User user){
        Book_Review_Like book_review_like = new Book_Review_Like();
        bookReviewLikeRepository.save(book_review_like);
        book_review_like.setBook_review(bookReviewRepository.findBook_ReviewById(idReview));
        book_review_like.setTime(LocalDateTime.now());
        book_review_like.setValue(1);
//            User user = new User();
//            user = userRepository.findUserByEmailContains(username);
        book_review_like.setUser(user);
        //book_review_like.setId(2);
        bookReviewLikeRepository.save(book_review_like);
    }

    /**
     * Метод обработки и сохранения в БД дизлайков к комментариям о книгах
     * @param idReview - id комментария, сохраннного в БД
     * @param user - объект типа User
     */
    public void dizlikeReviewSave(Integer idReview, User user){
        Book_Review_Like book_review_like = new Book_Review_Like();
        bookReviewLikeRepository.save(book_review_like);
        book_review_like.setBook_review(bookReviewRepository.findBook_ReviewById(idReview));
        book_review_like.setTime(LocalDateTime.now());
        book_review_like.setValue(0);
        book_review_like.setUser(user);
        bookReviewLikeRepository.save(book_review_like);
    }

    /**
     * Обработка и сохранение рейтинговой оценки книги присвоенной пользователем
     * @param bookId - id объекта Book
     * @param value - рейтинг от 1 до 5
     * @param user - объект типа User
     */
    public void BookRatingSave(Integer bookId,Integer value,User user){
        BookRating bookRating = new BookRating();
        bookRating.setBook(bookRepository.findBookById(bookId));
        bookRating.setRating(value);
        bookRating.setUser(user);
        bookRatingRepository.save(bookRating);
    }
}

~~~
* Все методы во вновь созданных классах и в классах контроллерах, подвергнутых рефакторингу, задокументированы с помощью JavaDOC

Пример изменений в классе /Controller/BookController.java
~~~
 /**
     * Обработка комментария к книге и его сохранение в базе данных
     * @param model - модель не используется
     * @param sl - атрибут slug объекта Book
     * @param comment - строка comment типа String
     * @return - URL страницы перехода
     */
    @PostMapping("/books/comment/{sl}")
    public String handleAddNewComment(Model model, @PathVariable("sl") String sl, @RequestParam String comment){
        
        User user = analitikService.autorizationGetUser();
        Logger.getLogger(BooksController.class.getSimpleName()).info("Пятый контроллер комментарий");
        if(user != null) {
           
            Book book = bookRepository.findBookBySlug(sl);
            analitikService.bookReviewSave(comment,book,user);
           
            return "redirect:/root/"+sl;
        }
        else{
            Logger.getLogger(BooksController.class.getSimpleName()).info("Open Signin page");
            return "/signin";
        }
    }
~~~
* Заменены объекты @ModelAttribute("postponedCount"), @ModelAttribute("catCount"), @ModelAttribute("userCustom")
в контроллерах /controller/AuthorsPageController.java, BookPosponedController.java, 
  BookshopCartController.java, DocPajeController.java, GenresPageController.java,
  MainPageController.java, RecentController.java, SlugController.java
  
~~~
/**
     * Возвращает количество книг в "Отложено"
     * Используется в header страницы для индикации текущего состояния сервиса "Отложено"
     * @return - количество отложенных книг
     */
    @ModelAttribute("postponedCount")
    public Integer postponedCound(){
        
        User user = new User();
        user = analitikService.autorizationGetUser();
        if(user != null) {            
            List<Book2User>filteredList = analitikService.getUserBooksStatus(user,"KEPT");           
            List<Book>booksPost = analitikService.getBooksFromBook2User(filteredList);
            return booksPost.size();
        }
        else{
            return 0;
        }
    }

    /**
     * Возвращает количество книг в корзине
     * Используется в header страницы для индикации текущего состояния сервиса "Корзина"
     * @return - количество книг в корзине
     */
    @ModelAttribute("catCount")
    public Integer catCound(){        
        User user = new User();
        user = analitikService.autorizationGetUser();        
        if(user != null) {            
            List<Book2User>filteredList = analitikService.getUserBooksStatus(user,"CART");
            List<Book> booksPost = analitikService.getBooksFromBook2User(filteredList);            
            return booksPost.size();
        }
        else{
            return 0;
        }
    }
    
    /**
     * Выводит имя текущего пользователя или сообщение "не определен"
     * @return - имя пользователя, авторизированного в системе либо строку "не определен"
     */
    @ModelAttribute("userCustom")
    public String userCustom(){        
        User user = analitikService.autorizationGetUser();
        if(user != null) {            
            return user.getUser_name();
        }
        else{
            return "не определен";
        }
    }

~~~
* в контроллерах /controller/AuthorsPageController, BookPostponedController,
BooksController, BookshopCartController, MainPageController
  вычистил сервисную часть, заменив ее на методы из /Service/AnalitikService.java

#Readme для домашнего задания 8.7
1. Реализовать обработку security-специфичных исключений
* Реализован класс обработки исключений ../errs/MyJWTException.java
~~~
package com.example.MyProjectWithSecurity.errs;

public class MyJWTException extends Exception {

    private final  String message;

    public MyJWTException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
~~~
* реализован контроллер обработчик JWT-зависимых исключений ../security/controller/JWTExceptionController.java

~~~
package com.example.MyProjectWithSecurity.security.controller;

import com.example.MyProjectWithSecurity.errs.MyJWTException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class JWTExceptionController extends ResponseEntityExceptionHandler {

//    @GetMapping("/signin")
//    public String getErrorJWT(){
//        return "/signin.html";
//    }

    @ResponseStatus(value = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ExceptionHandler(value = MyJWTException.class)
    @ResponseBody
    public String handlerError(MyJWTException myJWTException){
        return "/signin";
    }
}

~~~
* реализован тестовый контроллер ../security/controller/TestJwtTokenController.java
~~~
package com.example.MyProjectWithSecurity.security.controller;

import com.example.MyProjectWithSecurity.security.jwt.JWTUtil;
import com.example.MyProjectWithSecurity.security.service.JWTBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/test_page")
public class TestJwtTokenController {
    private final JWTUtil jwtUtil;
    private final JWTBlackListService jwtBlackListService;


    @Autowired
    public TestJwtTokenController(JWTUtil jwtUtil, JWTBlackListService jwtBlackListService) {
        this.jwtUtil = jwtUtil;
        this.jwtBlackListService = jwtBlackListService;
    }

    /**
     * Контроллер getTokenPage разработан для тестирования функциональности jwt-токенов
     * используется для открытия тестовой страницы ../test_page/token_test.html
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/token_test")
   public String getTokenPage(HttpServletRequest request, Model model){
       Cookie[]cookies = request.getCookies();
       String[]token = null;
       if(cookies != null){
           model.addAttribute("cookie",cookies);
           for(Cookie coo : cookies){
               if(coo.getName().equals("token")){
                   String foo = coo.getValue();
                    token = foo.split("\\.");
               }
           }

           if (token != null && token.length != 0) {
               model.addAttribute("token", token);
               Base64.Decoder decoder = Base64.getDecoder();
               String header = new String(decoder.decode(token[0]));
               String payload = new String(decoder.decode(token[1]));
               //String tail = new String(decoder.decode(token[2]));
               String[] decodArr = {header, payload};
               model.addAttribute("decod", decodArr);

               char[] ch = payload.toCharArray();
               StringBuffer buff = new StringBuffer();
               for (char c : ch) {
                   if (c != '"' && (c != '{' && c != '}')) {
                       buff.append(c);
                   }
               }
               payload = buff.toString();
               String[] payloadArr = payload.split("\\,");
               Map<String, String> map = new TreeMap<>();
               for (String s : payloadArr) {
                   String[] test = s.trim().split("\\:");

                   map.put(test[0], test[1]);

               }
               model.addAttribute("mapr", map);

               Long experat = Long.parseLong(map.get("exp"));
               Long iat = Long.parseLong(map.get("iat"));
               LocalDateTime ex = LocalDateTime.ofInstant(Instant.ofEpochSecond(experat), ZoneId.systemDefault());
               LocalDateTime i = LocalDateTime.ofInstant(Instant.ofEpochSecond(iat), ZoneId.systemDefault());
               model.addAttribute("experati", ex);
               model.addAttribute("both", i);
           }
       }
       return "/test_page/token_test";
   }

    /**
     * Тестовый контроллер используется для проверки методов работы с BlackList тоненов
     * тестирование добавления в blacklist по нажатию кнопки формы ../test_page/token_test.html
     * @param request
     * @param response
     * @return
     */
   @PostMapping("/terminate_token")
    public String getTerminateToken(HttpServletRequest request, HttpServletResponse response){
        jwtBlackListService.tokenTerminator(request, response);
        jwtBlackListService.addBlackList(request);
        return "redirect:/test_page/token_test";
   }
}
~~~
* реализована тестовая страница для визуализации jwt-токена ../templates/test_page/token_test.html
~~~html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  <style>
    .cookie_bloc{
      margin-left: 1%;
    }
    .clames_block{
      margin-left: 2%;
    }
    .decod_block{
      margin-left: 3%;
    }
    .map_block{
      margin-left: 4%;
    }
    h1{
      margin-left: 1%;
    }
  </style>

</head>
<body>
<h1>Тест cookis</h1>
<div th:each="cook : ${cookie}">
  <div class="cookie_bloc">
    <p th:text="'JWT-токен cookie - ' + ${cook.getName()}"></p>
    <p th:text="'Содержание JWT токена - '+${cook.getValue()}"></p>
  </div>
</div>
<div class="clames_block" th:each="tok : ${token}">
  <p th:text="${tok.toString()}"></p>
</div>
<div class="decod_block" th:each="dec : ${decod}">
  <p th:text="${dec.toString()}"></p>
</div>
<div class="map_block" th:each="mapTo : ${mapr}">
  <p th:text="'Ключ: ' + ${mapTo.getKey()} + '  значение: '+ ${mapTo.getValue()}"></p>
</div>
<div class="map_block">
  <p th:text="'Время создания токена - ' + ${both}"></p>
  <p th:text="'Время экспирации токена - ' + ${experati}"></p>
</div>
<form id="form_terminator" method="post" th:action="${'/test_page/terminate_token'}">
  <input type="submit">Удалить токен</input>
</form>
<!--    <div class="valid_block">-->
<!--        <p th:text="${valid}"></p>-->
<!--    </div>-->
<script src="/assets/plg/jQuery/jquery-3.5.1.min.js"></script>
<!--    <script src="/assets/js/my_cookie_script.js"></script>-->
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
</body>
</html>
~~~
* доработан класс ../security/jwt/JWTUnit.java

~~~
package com.example.MyProjectWithSecurity.security.jwt;


import com.example.MyProjectWithSecurity.errs.MyJWTException;
import io.jsonwebtoken.*;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value("${auth.secret}")
    private String secret;

    private String createToken(Map<String,Object> claims, String username){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }

    public String getSecret() {
        return secret;
    }

    /**
     * Метод генерации токена
     * @param userDetails - параметры текущего пользователя
     * @return - сгенерированный токен jwt
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Возвращает свойства пользователя
     * @param token - jwt токен
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) throws MyJWTException {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    /**
     * Возвращает распарсеное значение токена
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) throws MyJWTException {
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            Logger.getLogger(JWTUtil.class.getSimpleName()).info("Exception ExpiredJwtException token is experation from extractAllClaims");
            throw new MyJWTException("token is not validation from extractAllClaims");
        }
        return claims;
    }

    /**
     * Возвращает имя юзера из токена
     * @param token
     * @return
     */
    public String extractUsername(String token) throws MyJWTException {
        String username = null;
        try {
             username = extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e){
            Logger.getLogger(JWTUtil.class.getSimpleName()).info("Exception ExpiredJwtException token is experation from extractUsername");
            throw new MyJWTException("token is not validation from extractUsername");
        }
        return username;
    }

    /**
     * Возвращает срок годности токена
     * @param token
     * @return
     */
    public Date extractExpiration(String token) throws MyJWTException {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Возвращает информацию boolean истек срок годности токена или нет
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) throws MyJWTException {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Возвращает информацию boolean о валидности или невалидности токена.
     * Проверяет соответствие имени пользователя в токене и в текущем userDetails
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) throws MyJWTException {
        boolean validation = false;
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            validation = true;
        }catch (ExpiredJwtException e){
            Logger.getLogger(JWTUtil.class.getSimpleName()).info("Exception ExpiredJwtException token is experation from validateToken");
            throw new MyJWTException("token is not validation from validateToken");
        } catch (SignatureException ex){
            ex.printStackTrace();
        } catch (Exception exe){
            exe.printStackTrace();
        }
            /*String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));*/

        return validation;
    }
}
~~~
* доработан класс ../security/jwt/JWTRequestFilter.java
~~~
package com.example.MyProjectWithSecurity.security.jwt;

import com.example.MyProjectWithSecurity.data.JWTBlackList;
import com.example.MyProjectWithSecurity.security.repository.JWTBlackListRepository;
import com.example.MyProjectWithSecurity.errs.MyJWTException;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetailService;
import com.example.MyProjectWithSecurity.security.service.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailService bookstoreUserDetailService;
    private final JWTUtil jwtUtil;
    private final JWTBlackListRepository jwtBlackListRepository;
    //private final BookstoreUserDetails bookstoreUserDetails;

    @Autowired
    public JWTRequestFilter(BookstoreUserDetailService bookstoreUserDetailService, JWTUtil jwtUtil, JWTBlackListRepository jwtBlackListRepository) {
        this.bookstoreUserDetailService = bookstoreUserDetailService;
        this.jwtUtil = jwtUtil;

        //this.bookstoreUserDetails = bookstoreUserDetails;
        this.jwtBlackListRepository = jwtBlackListRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    token = cookie.getValue();
                    List<JWTBlackList>blackLists = jwtBlackListRepository.findAll();
                    for(JWTBlackList jl:blackLists){
                        if(token.equals(jl.getTokenValue())){
                            token="";
                        }
                    }
//
                    try {
                        if(token.equals("")){
                            username = null;
                        }
                        else
                            username = jwtUtil.extractUsername(token);
                    } catch (MyJWTException e) {
                        e.printStackTrace();
                    }

                }

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailService.loadUserByUsername(username);
                    try {
                        if (jwtUtil.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                                    null, userDetails.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    } catch (MyJWTException e) {
                        Logger.getLogger(JWTRequestFilter.class.getSimpleName()).info("I Catch Exception !");
                        e.printStackTrace();
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
~~~
3. Реализовать механизм blacklist для невалидных JWT-токенов
* реализован класс ../data/JWTBlackList.java
~~~
package com.example.MyProjectWithSecurity.data;

import javax.persistence.*;

/**
 * класс JWTBlackList создает таблицу в базе данных black_list
 * для записи туда токенов после выхода из программы по алгоритму /logout
 */
@Entity
@Table(name = "blackList")
public class JWTBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "value")
    private String tokenValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
~~~
* разработан класс ../security/service/JWTBlackListService.java
~~~
package com.example.MyProjectWithSecurity.security.service;

import com.example.MyProjectWithSecurity.security.repository.JWTBlackListRepository;
import com.example.MyProjectWithSecurity.data.JWTBlackList;
import com.example.MyProjectWithSecurity.security.data.BookstoreUserRegister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class JWTBlackListService {
    public final JWTBlackListRepository jwtBlackListRepository;
    public final BookstoreUserRegister userRegister;


    public JWTBlackListService(JWTBlackListRepository jwtBlackListRepository, BookstoreUserRegister userRegister) {
        this.jwtBlackListRepository = jwtBlackListRepository;
        this.userRegister = userRegister;
    }

    /**
     * Метод возвращает из базы данных список невалидных токенов
     * @return список невалидных токенов
     */
    public List<JWTBlackList> initBlackList(){
        List<JWTBlackList> jwtBlackLists = jwtBlackListRepository.findAll();
        if(jwtBlackLists.size() == 0 || jwtBlackLists == null){
            return new ArrayList<>();
        }
        else
            return jwtBlackLists;
    }

    /**
     * Метод добавляет токен в список и базу данных BlackList токенов
     * метод запускается при открытии страницы signin.html
     * @param request - HTTPServletRequest - получение списка действующих токенов
     * @return 0 - операция добавления прошла успешно, -1 - произошла ошибка
     */
    public Integer addBlackList(HttpServletRequest request){
        List<JWTBlackList>jwtBlackLists = initBlackList();
        Cookie[]cookies = request.getCookies();
        String tokenContext = "";
        String tokenName = "";
        if(cookies != null){
            for(Cookie coo : cookies){
                if(coo.getName().equals("tokenCopy")){
                    tokenContext = coo.getValue();
                    tokenName = coo.getName();
                }
            }
            boolean chek = true;
            for(JWTBlackList item : jwtBlackLists){
                if(item.getTokenValue().equals(tokenContext)) {
                    chek = false;
                    break;
                }
                else{
                    chek = true;
                }
            }
            if(chek == true){
                JWTBlackList jwtSave = new JWTBlackList();
                jwtSave.setTokenValue(tokenContext);
                jwtBlackListRepository.save(jwtSave);
                jwtBlackLists.add(jwtSave);
                return 0;
            }
            else{
                Logger.getLogger(JWTBlackListService.class.getSimpleName()).info("JWT-token"+tokenName+"in BlackList allready");
                return -1;
            }
        }else{
            Logger.getLogger(JWTBlackListService.class.getSimpleName()).info("JWT-token with name Token do not founded");
            return 1;
        }
    }

    /**
     * метод tokenTerminator - неудачная тестовая попытка уничтожить jwt-токен
     * в работе программы не используется, нужен был для проверки версии
     * @param request
     * @param response
     */
    public void tokenTerminator(HttpServletRequest request, HttpServletResponse response){
        Cookie[]cookies = request.getCookies();
        String[]tokenJWT = null;
        String payload = "";
        if(cookies != null){
            for(Cookie coo : cookies){
                if(coo.getName().equals("token")){

                    Cookie cookie = new Cookie("token","");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                    SecurityContextHolder.clearContext();
                }
            }
        }
    }
}

~~~
* доработаны контроллеры @PostMapping("/login") класса ../security/controller/AuthUserController.java
~~~
@PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse){
        //return userRegister.login(payload); //данная строка используется при использовании механизма сессий. Все что ниже для токенов.
        //____________Механизм использования токенов JWT___________________________//
        //----HttpServletResponse httpServletResponse в параментах тоже добавлен для схемы работы с токенами--//
        ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token",loginResponse.getResult());
        Cookie jwtCopy = new Cookie("tokenCopy",loginResponse.getResult()); //создание копии для сохранения в BlackList токенов

        httpServletResponse.addCookie(cookie);
        httpServletResponse.addCookie(jwtCopy);
        return loginResponse;
    }

~~~
* доработаны контроллеры @GetMapping("/signin") класса ../security/controller/AuthUserController.java
~~~
@GetMapping("/signin")
    public String handleSignin(HttpServletRequest request){
        jwtBlackListService.addBlackList(request); //добавлено для записи токена в BlackList
        Logger.getLogger(AuthUserController.class.getSimpleName()).info("Open signin page from AuthUserController!");
        return "signin";
    }
~~~
5. Доработать OAuth-авторизацию
* настройка application.properties
~~~
spring.security.oauth2.client.registration.facebook.client-id=236452818404590
spring.security.oauth2.client.registration.facebook.client-secret=f841cf4014771e8c97348c3de67669c8
~~~
* в процессе работы с Facebook возникла проблема со службой безопасности данного сайта
Facebook по непонятным причинам заблокировал аутентификацию OAuth2
* сгенерировано приложение в Facebook developers другого типа. Блокировка сервера защиты исчезла, но токен для входа в
приложение не передается и аутентификацию завершить не получается.
Данный пункт выполнен не полностью
6. Ограничить доступ к функционалу отзывов на книги
* данный функционал был реализован полностью при выполнении задания 7.6

