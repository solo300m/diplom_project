package com.example.MyProjectWithSecurity.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name ="books")
@ApiModel("data model of books entity")
/*@AttributeOverride(name = "id",
column = @Column(name = "id_author", nullable = false))*/
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id of books",position = 1)
    private Integer id;
    @Type(type="org.hibernate.type.LocalDateType")
    @ApiModelProperty(value = "date enter of book to the database", position = 2)
    private LocalDate date;
    @ApiModelProperty(value = "1- book is bestseller, 0-it is not",position = 3)
    @JsonProperty("isBestseller")
    private Byte isbestseller;
    private String slug;
    private String image;
    @Type(type="text")
    private String discription;
    //private Integer id_author;
    //@Transient
    @ManyToOne
    @JoinColumn(name = "id_author", referencedColumnName = "id")
    @ApiModelProperty(value = "Author object", position = 5)
    private Authors author;

    @JsonGetter("authors")
    public String authorsFullName(){
        return author.toString();
    }
    @ManyToOne
    @JoinColumn(name = "idteg",referencedColumnName = "idteg")
    private Tag tag;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookRating>bookRatings = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2Author>book2Authors = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book_Review>bookReviews = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2User>book2Users = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book2Genre>book2Genres = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<File_Download>file_downloads = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Balance_Transaction>balance_transactions = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Book_File> book_files = new ArrayList<>();

    @ApiModelProperty(value = "title of the book", position = 4)
    private String title;
    @JsonProperty
    private Double priceOld;
    @Column(name = "discount")
    @JsonProperty("discount")
    private Double price;

    @JsonProperty
    public Double discountPrice(){
        Double discountedPriceDouble=0.0;
        discountedPriceDouble = (Math.round((priceOld - (priceOld * (price/100)))*100)/100.0);
        return discountedPriceDouble;
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Byte getIsbestseller() {
        return isbestseller;
    }

    public void setIsbestseller(Byte isbestseller) {
        this.isbestseller = isbestseller;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Authors getAuthor() {
        return author;
    }

    public void setAuthor(Authors author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Double priceOld) {
        this.priceOld = priceOld;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Book2Author> getBook2Authors() {
        return book2Authors;
    }

    public void setBook2Authors(List<Book2Author> book2Authors) {
        this.book2Authors = book2Authors;
    }

    public List<Book_Review> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<Book_Review> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public List<Book2User> getBook2Users() {
        return book2Users;
    }

    public void setBook2Users(List<Book2User> book2Users) {
        this.book2Users = book2Users;
    }

    public List<Book2Genre> getBook2Genres() {
        return book2Genres;
    }

    public void setBook2Genres(List<Book2Genre> book2Genres) {
        this.book2Genres = book2Genres;
    }

    public List<File_Download> getFile_downloads() {
        return file_downloads;
    }

    public void setFile_downloads(List<File_Download> file_downloads) {
        this.file_downloads = file_downloads;
    }

    public List<Balance_Transaction> getBalance_transactions() {
        return balance_transactions;
    }

    public void setBalance_transactions(List<Balance_Transaction> balance_transactions) {
        this.balance_transactions = balance_transactions;
    }

    public List<Book_File> getBook_files() {
        return book_files;
    }

    public void setBook_files(List<Book_File> book_files) {
        this.book_files = book_files;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<BookRating> getBookRatings() {
        return bookRatings;
    }

    public void setBookRatings(List<BookRating> bookRatings) {
        this.bookRatings = bookRatings;
    }

    @Override
    public String toString() {
        return "Book id= " + id +
                ", id_author=" + author.getId() +
                ", author='" + author.getAuthor() + '\'' +
                ", title='" + title + '\'' +
                ", priceOld='" + priceOld.toString() + '\'' +
                ", price='" + price.toString() + '\'';
    }

    public Integer getCountBookRating(Integer filterRating){
        List<BookRating>list = getBookRatings().stream().filter(c->c.getRating()==filterRating).collect(Collectors.toList());
        return list.size();
    }
    public List<BookRating> getBookRatingFiltered(Integer filterRating){
        List<BookRating>list = getBookRatings().stream().filter(c->c.getRating()==filterRating).collect(Collectors.toList());
        return list;
    }
    public Integer solverBookRating(){
        List<BookRating> list = getBookRatings();
        if(list.size()!=0 && list != null){
            Integer sumRating = 0;
            Integer Rating = 0;
            for(BookRating s: list){
                sumRating = sumRating + s.getRating();
            }
            Rating = Math.toIntExact(Math.round((double) sumRating / list.size()));
            return Rating;
        }
        else{
            return 0;
        }
    }
}
