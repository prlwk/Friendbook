# Friendbook Android Application
## Project Description
Mobile Android application for lovers of reading books. Users will be able to write book reviews, view various collections, add other users as friends, and view their book ratings. Kotlin language will be used for the project implementation. For the server part, a microservice architecture will be used, written using the Spring Framework and the Java language.

Мобильное Android приложение для любителей читать книги. Пользователи смогут писать отзывы книгам, смотреть различные подборки, добавлять других пользователей в друзья и смотреть их оценки книгам. Для реализации проекта будет использован язык Kotlin. Для серверной части будет использована микросервисная архитектура, написанная с помощью Spring Framework и языка Java.
## Project Team
* Молчанова Анастасия Алексеевна - 3530904/00101
* Пашков Данил Алексеевич - 3530904/00101
* Рыжова Алена Алексеевна - 3530904/00101
* Усанов Андрей Романович - 3530904/00101
## Description problem
Every second Russian reads books. According to the survey, on average each reader has read 5 books in the last three months. At the same time, hundreds of books are published every day, many of which are not worthy of attention and do not carry any artistic meaning.
As a result, for a large number of people the choice of a book is a problem. Our team thought about how we can help users in this matter.
Our application will allow you to search for books and authors by specified criteria, view collections, as well as rate works and write reviews on them, which may give rise to some competitive aspect among users.

Каждый второй россиянин читает книги. Согласно опросу, в среднем каждый из читающих граждан прочел 5 книг за последние три месяца. При этом, каждый день издаются сотни книг, многие из которых не достойны внимания и не несут никакого художественного смысла.
В следствии этого, для большого количества людей выбор книги является проблемой. Наша команда задумалась, каким образом можно помочь пользователям в этом вопросе.
Наше приложение позволит искать книги и авторов по заданным критериям, просматривать подборки, а также оценивать произведения и писать на них отзывы, что может породить некоторый соревновательный аспект у пользователей.
## Requirements
#### Use Case Diagram 
<img src="/images/use-case-diagram.png" width="600">

#### Tasks
1. Develop database schemas for storing information for each of the microservices.
2. Develop 4 microservices: AuthorService, BookService, ReviewService and UserService, and connect the service discovery service and load balancer.
3. Ensure secure storage of user data.
4. Create a mobile application presentation prototype.
5. Transfer views to XML files.
6. Connect the client and server parts using JSON/HTTP.
7. Fill the database with the necessary amount of information about books and authors.
8. Write Unit Tests for API and mobile application.
## Architecture
#### System context diagram
<img src="/images/system-context-diagram.jpg" width="600">

#### Container diagram
<img src="/images/container-diagram.jpg" width="600">

## Design
The prototype of the project can be viewed in [Figma](https://www.figma.com/file/nGornPW9vpsak0k7U5DCRi/book?node-id=0%3A1)
#### Light theme 
<p>
    <img src="/images/screen-1-light.jpg" width="200">
    <img src="/images/screen-2-light.jpg" width="200">
    <img src="/images/screen-3-light.jpg" width="200">
<p>

#### Dark theme 
<p>
    <img src="/images/screen-1-dark.jpg" width="200">
    <img src="/images/screen-2-dark.jpg" width="200">
    <img src="/images/screen-3-dark.jpg" width="200">
<p>

## Technology stack
#### For backend:
* The [Spring Framework](https://spring.io/) was chosen for the development API. A [microservice architecture](https://spring.io/microservices) was used, each of the microseries is a separate [Spring Boot](https://spring.io/projects/spring-boot) application. With several ready-to-use cloud templates, [Spring Cloud](https://spring.io/cloud) helped our project with service discovery: [Spring Cloud Netflix Eureka](https://cloud.spring.io/spring-cloud-netflix/reference/html/), load balancing, and acted as an API gateway.
* [DBMS MySql](https://www.mysql.com/) was chosen for data storage.
* [Docker](https://www.docker.com/) was used to build the project, each microservice is a separate image.
#### For android app:
* [OkHttp3](https://square.github.io/okhttp/) is an interceptor which helps to log your API calls
* [Retrorfit](https://square.github.io/retrofit/) is a type-safe HTTP client
* [Dagger2](https://dagger.dev/dev-guide/) is a compile-time dependency injection framework
* [Glide](https://github.com/bumptech/glide) is an image loading framework
* Coroutines are a Kotlin feature that converts async callbacks for long-running tasks
* ViewModel is designed to store and manage UI-related data in a lifecycle conscious way
## Source
* [Android Mobile App](https://github.com/prlwk/Friendbook/tree/main/client)

* [API](https://github.com/prlwk/Friendbook/tree/main/API)
## Unit tests
[Tests for android app](https://github.com/prlwk/Friendbook/tree/main/client/app/src/test/java/com/src/book)
