### **Restaurants application**

Application provides to vote users for restaurants. Admins can add users, menus, dishes and restaurants. Application
use "soft delete" method witch able to get reports about past events in a database. Application provide authorization.
For first start please use username 'ADMIN' and password '1'. For security please change password after first login.
Application support REST API

Cople of curl operations:
Login curl -v --cookie-jar cookie -d username=admin@mail.ru -d password=1 -L http://localhost:8080/login

Get all restaurants curl -u admin@mail.ru:1 -i -H 'Accept:application/json' localhost:8080/rest/restaurants

Get most voted restaurants curl -u admin@mail.ru:1 -i -H 'Accept:application/json' localhost:
8080/rest/restaurants/sortedbyvotes?date

Vote restaurant curl -u admin@mail.ru:1 -i -H 'Accept:application/json' -d "restaurant=100004" "localhost:
8080/rest/votes/