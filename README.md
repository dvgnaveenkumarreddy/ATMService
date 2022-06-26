# ATMService

This application is built using Java, Maven, Spring boot, Spring data JPA and MongoDB.

Steps to setup the project

1. Clone the project.
2. Import project into IDE and build.
3. You should have MongoDB configured in your local system and running.
4. Run project as spring application.
5. Use postman or ARC or soup UI for testing.

## Fetch the balance

> GET http://localhost:8080/api/accountBalance

### On Success

#### Request:-
```
{
  "accountNumber":123456789,
    "pin":1234
}
```

#### Response:-
```
{
    "accountNumber": 123456789,
    "balance": 800.0,
    "denomination": null,
    "message": "Success!"
}
```

### On Failure

#### Request:-

Trying to check balance with wrong pin

```
{
    "accountNumber": 123456789,
    "pin": 123
}
```

#### Response:- 

```
{
    "errorMessage": "Invalid Account Number or Pin",
    "requestedURI": "/api/accountBalance"
}
```

## Withdraw Money

> PUT http://localhost:8080/api/withdraw

### On Success

#### Request:-
```
{
    "accountNumber": 123456789,
    "pin": 1234,
    "withdrawAmount": 15
}
```

#### Response:-
```
{
    "accountNumber": 123456789,
    "balance": 770.0,
    "denomination": {
        "fives": 1,
        "tens": 1,
        "twenties": 0,
        "fifties": 0
    },
    "message": "Success!"
}
```

### On Failure

Trying to dispense money greater than balance amount in the account 

#### Request:-
```
{
    "accountNumber": 123456789,
    "pin": 1234,
    "withdrawAmount": 800
}
```

#### Response:- 

```
{
    "errorMessage": "Don't have enough funds in your Account",
    "requestedURI": "/api/withdraw"
}
```
### On Failure

When we don't have enough money in ATM

#### Request:-

```
{
    "accountNumber": 123456789,
    "pin": 1234,
    "withdrawAmount": 800
}
```

#### Response:- 

```
{
    "errorMessage": "Don't have enough funds in ATM machine, Sorry! ",
    "requestedURI": "/api/withdraw"
}
```
### On Failure

When Entered amount is Invalid

#### Request:-

```
{
    "accountNumber": 123456789,
    "pin": 1234,
    "withdrawAmount": 801
}
```

#### Response:- 

```
{
    "errorMessage": "Please enter the amount in multiples of 5 or 10",
    "requestedURI": "/api/withdraw"
}
```

### On Failure
 When entered amount is low
#### Request:-

```
{
    "accountNumber": 123456789,
    "pin": 1234,
    "withdrawAmount": 0
}
```

#### Response:- 

```
{
    "errorMessage": "Entered Balance is too low",
    "requestedURI": "/api/withdraw"
}
```
### Test case coverage snippet 

![image](https://user-images.githubusercontent.com/46936243/175803614-b97432f0-3aae-486a-89bb-a856753db83a.png)

