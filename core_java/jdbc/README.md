# Introduction
The Stockquote app is a Java application designed to facilitate real-time stock trading. 
It utilizes a third-party API to retrieve stock market data in JSON format, 
which is then parsed into Java objects for further processing. 
The app takes advantage of modern libraries such as OkHttp for seamless HTTP 
requests and Jackson for efficient JSON parsing all of which was done by using Maven for development and 
project management. To ensure code reliability, the project incorporates comprehensive unit and integration testing. 
Additionally, the app stores requests in a PostgreSQL database, 
which is hosted within a Docker container.


# Implementaiton
The Stockquote app was implemented using Java 11, 
leveraging its features and enhancements. 
It utilized the OkHttp library to support HTTP requests and responses, 
enabling seamless communication with the third-party API. 
Additionally, the app incorporated the Jackson library for 
efficient JSON parsing, allowing for easy manipulation of stock market data. 
The project was managed using Maven which facilitated dependency management 
and streamlined the development process.

## ER Diagram
![Local Image](https://i.imgur.com/6BGClQb.png)

## Design Patterns
In the Stockquote app, the DAO (Data Access Object) and Repository design patterns, along with the MVC (Model-View-Controller) architectural pattern, were applied to ensure a well-structured and maintainable codebase.

The DAO design pattern was utilized to separate the data access logic from the rest of the application. It provided a way to encapsulate the interaction with the PostgreSQL database within dedicated DAO classes. These DAO classes contained methods for CRUD (Create, Read, Update, Delete) operations, allowing the app to interact with the database without exposing the underlying implementation details. This abstraction facilitated modularity and improved code readability.

The Repository design pattern was employed to provide a higher-level interface for data access, acting as an intermediary between the DAOs and the rest of the application. The Repository class abstracted away the specific data access logic and provided a uniform API for retrieving and manipulating stock data. This pattern enabled the app to switch between different data sources or storage technologies without affecting the application code.

The MVC architectural pattern was adopted to separate the concerns of the application into three distinct components. The Model represented the business logic and data manipulation, the View was responsible for rendering the user interface, and the Controller handled the interaction between the Model and View. This separation of concerns improved code organization, code reusability, and simplified maintenance.

By employing the DAO and Repository design patterns along with the MVC architectural pattern, the Stockquote app achieved a modular and scalable structure, making it easier to develop, test, and maintain the application codebase.

# Test
The Stockquote app incorporates comprehensive Unit and Integration tests to ensure its reliability and accuracy. The testing process begins by establishing a connection to the Database and creating a new object. It then verifies the existence of the object by asserting that the findById method returns a Quote with the expected symbol. Finally, the quote is deleted to restore the database to its original state before running the tests.

The Integration tests are designed to validate the application's functionality from end to end. It follows a top-down approach where it fetches a new Quote from the API, parses it into a Java Object, saves it into the database, searches for it, and then deletes it.

To facilitate logging, the app utilizes the Log4j logger, which provides a flexible and configurable logging framework. This allows for efficient logging of important events, errors, and debugging information during the testing and runtime phases of the application.

By incorporating Unit and Integration tests, along with the Log4j logger, the Stockquote app ensures that it operates seamlessly, maintains data accuracy, and provides a robust user experience.

# How to Use
Once everything is set up and running. You can input commands on the console.
The basic structure of a command is as follows:

    [ticker] [command] {[]...additional parameters}

ticker - the symbol for the stock
command - save, find, findAll, buy, sell