# clean architecture + rxjava2 + spring boot + mongo + feign + hystrix + spock + swagger

### run the application
```sh
$ mvn spring-boot:run
```

### execute tests
```sh
$ mvn clean test
```

### api documentation
http://localhost:8080/swagger-ui.html

### sample requests
```sh
$ curl -X GET -H 'Accept: application/json' 'http://localhost:8080/restaurants/chips_n_burger_1'
```

User 'john@doe.com' has 20% off in all menu items. Check with following request:
```sh
$ curl -X GET -H 'Accept: application/json' -H 'uid: john@doe.com' 'http://localhost:8080/restaurants/chips_n_burger_1'
```

### clean architecture reference
https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html

### domain model
https://github.com/gbroveri/rxsample/blob/master/domain.png

### sample json output
https://github.com/gbroveri/rxsample/blob/master/menu.json
