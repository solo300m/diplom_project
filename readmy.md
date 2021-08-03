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