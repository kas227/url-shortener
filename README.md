# Url Shortener
This project is a simple url shortening service offering basic functionality to create and look up shortened urls.

An easy example would be
```
https://www.google.com/search?client=firefox-b-d&q=long+url => https://your.domain/=ved2e
```

## Setup
The application makes use of the [Spring Boot ecosystem](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html) and utilises [Spring Boot Web](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web) for web service capabilities and [Spring Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data) for persistence. 
Note that the `spring-boot-starter` packages of these dependencies are used. These starters also include other dependencies and default configurations. 

## Getting Started
Simply start the spring boot application:
```shell
./mvnw spring-boot:run
```

### Endpoints

#### Create Short Url
`POST` - `/`

```
{
    "url": "<YOUR_URL>"
}
```
```
# 200
<KEY>
```

#### Create Alias
`POST` - `/`

```
# 200
{
    "url": "<YOUR_URL>",
    "alias": "<YOUR_ALIAS>"
}
```
```
<KEY/ALIAS>
```

#### Lookup Url 
`GET` - `/<KEY>`
```
# 308 
REDIRECT
```

#### Get Stats for <KEY>
`GET` - `/api/stats?key=<KEY>`
```
# 200 
{
    "url": "<URL>",
    "clicks": <AMOUNT_OF_CLICKS> 
}
```

### Research 
Since this is an exercise with predefined requirements the following section will document some insights *how* and *why* certain aspects of the application are designed.

### Choice of technologies 
As described above the application should use Spring Boot, so the stack is set. While alternatives like plain Java SDK 
could also solve the task there are also benefits of using Spring Boot like familiarity of project structure of other potential maintainers.

For simplicity’s sake an H2 in-memory database will be used with JPA abstraction to allow to change this decision later on 
once more requirements become apparent.

Besides the mentioned dependencies the project setup will be kept slim to make it easier to understand and maintain.

### Approach
In general the service needs a possibility to resolve a *long url* or *initial url* based on a shorter key.

One of the more challenging parts of the problem is to create *short urls* from the passed *long urls* and  vice verca.
This process should be fast and inexpensive but reliable. While no specific requirements are given a service that adds 
noticeable time when calling a (short) url will not be a good experience for the user.

#### Hashing
A naive approach could be to **hash** the url. However, hashing cannot guarantee that there will be no collisions even though it might be negligible small. 
Collisions could be solved by saving the hash as a *key* together with the target *long url* and regenerating a salted hash in case a collision occurs.
The length of popular hashes like SHA or MD5 is also not directly usable as they are too long (ofc there are ways to shorten).
Yet, with increasing numbers of entries the number of collisions will increase making the generation more costly due to recalculations.
We would need a way to detect duplicate keys when adding new entries which further complicates this approach (race conditions, transactions, indexes).

#### Encoding
Another approach would be to encode the url with for example Base64. However, the resulting string will not be short enough.
However, one can use a combination of an auto incrementing number series and base conversion to generate 
short urls in a controlled way.

*Example Creation*:
1. *long url* is received
2. auto increment id is taken from series => "100"
3. id is converted from base10 to base36* => "100_10" => "2S_36"
4. long url is saved with id 
5. base36 value "2S" is returned as *short url*

*Example Read*:
1. *short url* is received => "2S"
2. *short url* is converted to base10 => "2S_36" => "100_10"
3. *long url* is read from entry under id => "100"
4. Redirect is returned
 
*base36 because there are 36 characters in the case-insensitive alphanumeric dictionary ([a-b][0-9]).

This approach should be fairly simple to achieve since base conversion is a simple algorithm and Hibernate provides [generated
sequence ids](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-auto).
We therefore can save a new entry in the table and receive back a new unique id which we can encode.

⚠️This eliminates the need to explicitly to prevent race conditions and auto id guarantees no collisions.
> Therefore this is the way chosen here.

## Architecture
On a high level the structure will adhere to spring mvc and have controller(s) accepting incoming web requests and passing them 
to the respective services. Services should contain domain logic or further call into the domain layer while the controller
should only handle web service specifics like understanding web requests and serializing/deserializing dtos.
Data access is achieved with repositories.


