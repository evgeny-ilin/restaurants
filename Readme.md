## **Restaurants application**

Application provides to vote users for restaurants. Admins can add users, menus, dishes and restaurants. Application
use "soft delete" method witch able to get reports about past events in a database. Application provide authorization.
For first start please use username 'ADMIN' and password '1'. For security please change password after first login.
Application support caching and REST API

Application created with Java 15, SpringBoot, Hibernmate, HSQLDB, QueryDSL, Jackson, Lombok, Swagger, JUnit

API Documentation
[http://localhost:8080/swagger-ui.html][API Documentation]

### **User vote**

_Get restaurants with menus and dishes to date_
<pre>
curl -L -X GET 'localhost:8080/rest/restaurants/hierarchy?date=2021-01-10' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Vote/change vote_
<pre>
curl -L -X POST 'localhost:8080/rest/votes/?restaurant=100018' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Delete vote_
<pre>
curl -L -X DELETE 'localhost:8080/rest/votes/100015' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

### **Admin (users)

_Create new user_
<pre>
curl -L -X POST 'localhost:8080/rest/users' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox' \
-H 'Content-Type: application/json' \
--data-raw '{
    "name": "USER_TEST-2",
    "email": "user-2@mail.ru",
    "password": "123",
    "roles": [
        "USER"
    ],
    "new": false
}'
</pre>

_Get all users_
<pre>
curl -L -X GET 'localhost:8080/rest/users' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get user by email_
<pre>
curl -L -X GET 'localhost:8080/rest/users/by?email=user-2@mail.ru'
</pre>

_Update user_
<pre>
curl -L -X PUT 'localhost:8080/rest/users/100015' \
-H 'Content-Type: application/json' \
--data-raw '{
    "deleteUser": null,
    "deleteDate": null,
    "id": 100015,
    "name": "USER_UPDATED-2",
    "email": "user-2@mail.ru",
    "password": null,
    "roles": [
        "USER"
    ],
    "enabled": true,
    "new": false
}'
</pre>

_Get user by id_
<pre>
curl -L -X GET 'localhost:8080/rest/users/100001'
</pre>

_Delete user_
<pre>
curl -L -X DELETE 'localhost:8080/rest/users/100015'
</pre>

_Enable/disable user_
<pre>
curl -L -X PATCH 'localhost:8080/rest/users/100001?enabled=false'
</pre>

### **Admin (restaurants)

_Create new restaurant_
<pre>
curl -L -X POST 'localhost:8080/rest/restaurants/0' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox' \
-H 'Content-Type: application/json' \
--data-raw '{
    "name": "Restaurant3"
}'
</pre>

_Get all restaurants_
<pre>
curl -L -X GET 'localhost:8080/rest/restaurants' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get restaurants with menus and dishes to date_
<pre>
curl -L -X GET 'localhost:8080/rest/restaurants/hierarchy?date=2021-01-10' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get restaurants that have dishes to date_
<pre>
curl -L -X GET 'localhost:8080/rest/restaurants/withdishes?date=' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get restaurant by id_
<pre>
curl -L -X GET 'localhost:8080/rest/restaurants/100004' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Delete restaurant_
<pre>
curl -L -X DELETE 'localhost:8080/rest/restaurants/100018' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get restaurants sorted by votes_
<pre>
curl -L -X GET 'localhost:8080/rest/restaurants/sortedbyvotes?date' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

### **Admin (menus)

_Create new menu for restaurant_
<pre>
curl -L -X POST 'localhost:8080/rest/menus/?menu=&restaurant=100018' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox' \
-H 'Content-Type: application/json' \
--data-raw '{
    "name": "Menu-2",
    "menuDate":"2020-10-25"
}'
</pre>

_Get all menus_
<pre>
curl -L -X GET 'localhost:8080/rest/menus' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get menu by id_
<pre>
curl -L -X GET 'localhost:8080/rest/menus/100006' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Link dishes to menu_
<pre>
curl -L -X POST 'localhost:8080/rest/menus/link?menuId=100019&dishesIds=100021,100022,100023' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Unlink dishes from menu_
<pre>
curl -L -X POST 'localhost:8080/rest/menus/unlink?menuId=100019&dishesIds=100022' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Delete menu_
<pre>
curl -L -X DELETE 'localhost:8080/rest/menus/100001'
</pre>

### **Admin (dishes)

_Create new dish_
<pre>
curl -L -X POST 'localhost:8080/rest/dishes/0' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox' \
-H 'Content-Type: application/json' \
--data-raw '{
    "name": "Dish-3",
    "price": 55.00
}'
</pre>

_Update dish_
<pre>
curl -L -X POST 'localhost:8080/rest/dishes/100011' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox' \
-H 'Content-Type: application/json' \
--data-raw '{
    "id": 100011,
    "name":"Dish3",
    "price": 5.55
}'
</pre>

_Get all dishes_
<pre>
curl -L -X GET 'localhost:8080/rest/dishes/' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get dish by id_
<pre>
curl -L -X GET 'localhost:8080/rest/dishes/100009' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get dish by restaurant_
<pre>
curl -L -X GET 'localhost:8080/rest/dishes/restaurant?date=&restaurantId=100002' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Get dish by menu_
<pre>
curl -L -X GET 'localhost:8080/rest/dishes/menu/100006' \
-H 'Authorization: Basic YWRtaW5AbWFpbC5ydTox'
</pre>

_Delete dish_
<pre>
curl -L -X DELETE 'localhost:8080/rest/dishes/100003'
</pre>
