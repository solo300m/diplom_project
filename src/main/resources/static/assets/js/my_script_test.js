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