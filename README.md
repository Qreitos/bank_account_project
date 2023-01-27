# Bank Account Project

<img src="https://img.shields.io/github/deployments/Qreitos/bank_account_project/bank-account-project?style=for-the-badge"> 

## Used technology
- Spring boot
- JWT Athorization
- Email verification
- Retrofit
- Flyway
- MySQL

[INSTRUCTIONS](https://bank-account-project.herokuapp.com/api/information)  
(User manual)

Bank Account rest API  
With this API you can register new users create different types of accounts and transfer money from one to another with real time currency rates.

POST registration endpoint ("/api/register")

```java 
@RequestMapping(value = "/register", method = POST)
public ResponseEntity<RegistrationResponseDto> registerData(
@RequestBody RegistrationRequestDto registrationRequestDto)
```

After successful registration, API will generate unique login number
and send email with verification token.

```json
{
    "message": "Registration successful. Please confirm verification email and save your login number!",
    "loginNumber": 76642,
    "httpStatus": "ACCEPTED",
    "responseDate": "2022-10-13T11:13:47.537160946+02:00"
}
```

Email verification

```java
SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("bank.account@azet.sk");
    mailMessage.setTo(recipientAddress);
    mailMessage.setSubject(subject);
    mailMessage.setText(
        "Hello, "
            + customer.getForName()
            + "!\n\n Please use this token for confirmation of your account by POST method "
            + "(/api/verification?token=[your_token]"
            + ("\n\n" + token + "\n\nThank you and have a nice day. :)"));
```


Authorization is done by JWT

```java
org.springframework.security.core.userdetails.User loggingUser =
        (org.springframework.security.core.userdetails.User)
            customerService.loadUserByLoginNumber(customerLogin.getLoginNumber());
```

```json
{
    "message": "Login successful",
    "httpStatus": "OK",
    "responseDate": "2022-10-13T11:47:13.840045039+02:00",
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3NjY0MiIsInJvbGVzIjpbXSwiaXNzIjoib3JnLnNwcmluZ2ZyYW1ld29yay53ZWIuc2VydmxldC5zdXBwb3J0LlNlcnZsZXRVcmlDb21wb25lbnRzQnVpbGRlckAyYWM0OTQwNyIsImV4cCI6MTY2NTY1ODAzM30.wdQ1k0IF6hD_ABVqy40YOGiebI03DDSGY01ug0xDBb0"
}
```

GET Home endpoint ("/api/home?currency=eur")

After successful login you are able to call home endpoint with JWT and currency parameter

```java
log.info("Decoding token");

Dotenv dotenv = Dotenv.load();
Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY"))
    .getBytes(StandardCharsets.UTF_8));
```
API will respond with CustomerResponseDto which contains info about customer and all accounts.  
Application is using Retrofit to get latest currency rates. You can choose from 150 currencies.


```json
{
    "customerId": 3,
    "forName": "Peter",
    "surName": "Spisak",
    "birthDate": "28.01.1994",
    "email": "pspisak94@gmail.com",
    "accounts": [
        {
            "accountId": 4,
            "accountType": "CLASSIC",
            "accountCreationDate": "2022-10-13",
            "accountNumber": 6810017666,
            "iban": "SK45 0120 0000 0068 1001 7666",
            "balance": 250.00,
            "currency": "EUR"
        }
    ]
}
```

Now you are able to create new accounts with "/create/account" endpoint passing account type as parameter.  
There are 3 types of accounts: CLASSIC, SAVING, INVESTOR.  
For example investor account can generate money in a period of time but there is a chance of loosing money.

```java
@RequestMapping(value = "api/create/account", method = PUT)
  public ResponseEntity<AccountCreateResponseDto> createAccount(
      @RequestHeader(name = "Authorization") String token,
      @RequestParam(name = "accountType") String accountType)
```

POST Transaction endpoint ("api/transaction")  
With this endpoint it is possible to transfer amounts of money to another accounts using IBAN format.  
You can make transactions in available currencies and API will convert them from base currency (EUR).  
All transactions (realised or not realised) are stored and accessible from database.

```java
  @RequestMapping(value = "/transaction", method = POST)
  public ResponseEntity<Transaction> realiseTransaction(
      @RequestHeader(name = "Authorization") String token,
      @RequestParam(name = "currency") String currency,
      @RequestBody Transaction transaction)
```

GET Accounts movements endpoint ("api/movements")  
Endpoint will return response with customers movements.
```json
{
    "transactions": [
        {
            "id": 1,
            "fromIban": "SK45 0120 0000 0031 8920 6398",
            "toIban": "SK45 0120 0000 0050 8578 8292",
            "amount": 120.00,
            "currency": "CZK",
            "realisationDate": "2022-10-17T18:35:50+02:00",
            "status": "Realised"
        },
        {
            "id": 2,
            "fromIban": "SK45 0120 0000 0031 8920 6398",
            "toIban": "SK45 0120 0000 0050 8578 8292",
            "amount": 1997.84,
            "currency": "HUF",
            "realisationDate": "2022-10-17T18:36:03+02:00",
            "status": "Not realised"
        }
    ]
}
```
