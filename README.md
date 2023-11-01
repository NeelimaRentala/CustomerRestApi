# incentive-springboot-template-service

[![Generic badge](https://img.shields.io/badge/LANGUAGE-Java_17+-blue.svg)]()
[![Generic badge](https://img.shields.io/badge/DOKERIZED-YES-green.svg)]()

Template that can be used to quickly set up a new Spring Project

## Table of contents

* [Technologies](#technologies)
* [Getting started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installing](#installing)
* [Postman collection](#postman-collection)
* [Running the tests](#running-the-tests)
* [Deployment](#running-the-tests)
* [Troubleshooting](#troubleshooting)
* [Contributing](#contributing)
* [Authors](#authors)

## Technologies

* Spring Boot
* Docker
* MySQL

## Description

A rest API to parse the CSV file and save the records in Data base. This service contains 
2 post Requests and 1 Get Request.
1. POST: To save a customer which is passed as a Request Body.
2. POST: To save customers by reading a CSV file. The directory of the CSV is passed as a Request Param and this takes 
         care of reading all .csv files under this location. This method internally calls the 1st POST request to 
         iteratively loop through each record and save into database.
3. GET: To find the customers saved in the database based on the customer reference.

These methods take care of possible exceptions during the processing and return an appropriate output.
## Getting started

These instructions will get you a copy of the project up and running on your local machine for development and testing 
purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* Docker for Mac
* OpenJDK Java 17 or greater

## Installing

A step by step series of examples that tell you how to get a development env running

### Start the service:

```
mvn spring-boot:run
```

### Test Service

```
curl -v http://localhost:8081/ping
curl -v http://localhost:8081/swagger-ui/index.html
```
## Running the tests

```
mvn clean install
```
