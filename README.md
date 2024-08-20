## Requirements
To build e-commerce API

## Landscape Design
Please refer architecture.pdf file

## Design
Solution is designed to follow the whole user journey throughout.

## Technology
- Java
- Spring boot
- JPA
- Maven

## Database
- Im-Memory database

## Instructions to run
### Prerequisite
- Maven is install(https://maven.apache.org/)

### Run
Go to root directory
run following command
- mvn spring-boot:run

**Alternatively:** solution can also be run from any developer tool like intellij. Run AssignmentApplication file from your development tool.


## Documentation
Endpoint related documentation can be found at
http://localhost:8080/swagger-ui/index.html

## Testing
Following Testing page can be used to test user journey
http://localhost:8080/

1) See Available Products(for product Ids) -> Optional
2) Get Shopping Cart -> Add Items(as many times as you want) by entering product id from #1
3) Click on order
4) Enter name, email address and click on place order button

- Create multiple orders
- Check reporting data by clicking on reporting links 
- Check your orders

## Possible Improvements

- Configured this as a docker service in docker-compose.yml
- Include more test cases
- Logging & Exception handling for more edge cases

## Testing User Journey with APIs

List All Product
Get Shopping Cart -> Add item to the cart -> Proceed to Order with personal details -> Order confirmed(Payment link send if not paid)

List Al Product -> GET http://localhost:8080/api/product/all -> would return product related data with id that is the product id
Get Shopping cart -> GET http://localhost:8080/api/cart/new -> would return identifier that is your cartId

Add item to the cart -> POST http://localhost:8080/api/cart/{cartId}/addItem 
    BODY: {"productId": "The product id","quantity": "Quantity you want to buy"}

** Multiple calls has to be made to add multiple items

Proceed to Order with personal details -> POST http://localhost:8080/api/order
    BODY: {"cartId": "Your cart id", "name": "test","emailAddress": "test@test.com"}
    RESPONSE: Order id

Check the order with order id -> http://localhost:8080/api/order/{orderId}

## Reporting API

Get Top 5 selling products -> http://localhost:8080/api/report/top5SellingProduct

Get daily sales report -> http://localhost:8080/api/report/dailySalesReport?startDate=<start date>&endDate=<end date>

Least selling product -> http://localhost:8080/api/report/top5SellingProduct/leastSellingProduct


    
