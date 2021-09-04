$(function(){
   var $cookie_item = document.cookie.split('; ').find(row=>row.startsWith('token')).split('=')[1].trim();
   alert("Значение cookie token = "+$cookie_item);
   setCookie('token',"",{'max-age': 0});
   $cookie_item = document.cookie.split('; ').find(row=>row.startsWith('token')).split('=')[1].trim();
   alert("Значение cookie token = "+$cookie_item);
   deleteCookie('token');
   $cookie_item = document.cookie.split('; ').find(row=>row.startsWith('token')).split('=')[1].trim();
   alert("Значение cookie token = "+$cookie_item);
});
function setCookie(name, value, options = {}){
  option = {
     path: '/',
     ...options
  };
  if(options.expires instanceof Date){
     options.expires = options.expires.toUTCString();
  }
  let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);
  for(let optionKey in options){
     updatedCookie += "; " + optionKey;
     let optionValue = options[optionKey];
     if(optionValue !== true){
        updatedCookie += "=" + optionValue;
     }
  }
  document.cookie = updatedCookie;
};
function deleteCookie(name){
   setCookie(name, "", {'max-age':-1});
}