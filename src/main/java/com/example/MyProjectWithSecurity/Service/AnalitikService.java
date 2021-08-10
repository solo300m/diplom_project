package com.example.MyProjectWithSecurity.Service;

import com.example.MyProjectWithSecurity.Repositories.*;
import com.example.MyProjectWithSecurity.controller.BookPostponedController;
import com.example.MyProjectWithSecurity.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AnalitikService {

    private final UserRepository userRepository;
    private final Book2UserRepository book2UserRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;
    private final Book2UserTypeRepository book2UserTypeRepository;

    @Autowired
    public AnalitikService(UserRepository userRepository, Book2UserRepository book2UserRepository,
                           BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository,
                           BookRepository bookRepository, BookRatingRepository bookRatingRepository,
                           Book2UserTypeRepository book2UserTypeRepository) {
        this.userRepository = userRepository;
        this.book2UserRepository = book2UserRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRepository = bookRepository;
        this.bookRatingRepository = bookRatingRepository;
        this.book2UserTypeRepository = book2UserTypeRepository;
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

    /**
     * Метод book2UserSaveBookWithStatus сохраняет объект Book в БД Book2User
     * со статусом enum Type_Name(Отложить, Купить, Оплачено, Архив)
     * @param book - объект класса Book
     * @param user - объект класса User
     * @param status - - строка со значениями enum Type_Name{
     *      KEPT(1),
     *      CART(2),
     *      PAID(3),
     *      ARCHIVED(4)};
     */
    public void book2UserSaveBookWithStatus(Book book, User user, String status){
        Book2User book2User = new Book2User();
        book2User.setBook(book);
        book2User.setUser(user);
        book2User.setBook_file_type(book2UserTypeRepository.findBook2User_typeByCode(status));
        book2User.setTime(LocalDateTime.now());
        book2UserRepository.save(book2User);

        List<Book2User> listNull = new ArrayList<>();
        listNull = book2UserRepository.findBook2UsersByBookIs(bookRepository.findBookById(null));
        Logger.getLogger(BookPostponedController.class.getSimpleName()).info("Null records in BD are "+listNull.size());
        if(listNull.size()!=0)
            book2UserRepository.deleteAll(listNull);

    }
}
