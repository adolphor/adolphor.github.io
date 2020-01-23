```bash
        error_page 404 /error/404.html;
        location = /404.html {
        	root   /home/my_nginx/adolphor/_site/error;
        	index  /404.html;
        }


# /home/my_nginx/adolphor/_site/404.html
error_page 404 /404.html;
location = /404.html {
    root   /home/my_nginx/adolphor/_site;
    index  /error/404.html;
}

#  open() "/home/my_nginx/adolphor/_site/blog/1997/01/01/assets/css/main.css" failed
error_page 404 /error/404.html;

# open() "/home/my_nginx/adolphor/_site/error/error/404.html" failed
error_page 404 /error/404.html;
location = /error/404.html {
    root   /home/my_nginx/adolphor/_site/error;
    index  /404.html;
}

# open() "/home/my_nginx/adolphor/_site/blog/1997/01/01/assets/css/main.css" failed
error_page 404 /error/404.html;
location = /404.html {
    root   /home/my_nginx/adolphor/_site/error;
    index  /404.html;
}

# open() "/home/my_nginx/adolphor/_site/blog/1997/01/01/assets/css/main.css" failed
error_page 404 /error/404.html;
location = /404.html {
    root   /home/my_nginx/adolphor/_site/error;
    index  404.html;
}

# open() "/home/my_nginx/adolphor/_site/error/error/404.html" failed
error_page 404 /error/404.html;
location = /error/404.html {
    root   /home/my_nginx/adolphor/_site/error;
    index  404.html;
}
```