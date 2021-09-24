# How to Use

### Authentification

The endpoints arre protected by httpBasicAuth.
The Credentials are:
    user: wedoogift
    password: wedoogift123


### Database Initialisation

You can init the database with calling this endpoint /database/generate
It will initialize the database as:

```json
{
  "wallets": [
    {
      "id": 1,
      "name": "gift cards",
      "type": "GIFT"
    },
    {
      "id": 2,
      "name": "food cards",
      "type": "FOOD"
    }
  ],
  "companies": [
    {
      "id": 1,
      "name": "Wedoogift",
      "balance": 1000
    },
    {
      "id": 2,
      "name": "Wedoofood",
      "balance": 3000
    }
  ],
  "users": [
    {
      "id": 1,
      "balance": [
        {
          "wallet_id": 1,
          "amount": 100
        }
      ]
    },
    {
      "id": 2,
      "balance": [
      ]
    },
    {
      "id": 3,
      "balance": []
    }
  ],
  "distributions": [
  ]
}

```

### Endpoints available

There are 3 available enpoints (without database intialization endpoint):

/user/{id}/balance ==> GET method. Get the balance of the user with userId {id}

/company/{id}/giftCards/distribute ==> POST method. Distribute gift cards to an user by companyId {id}
/company/{id}/mealVouchers/distribute ==> POST method. Distribute gift cards to an user by companyId {id}

Body:
```json
{
  "userId": 42,
  "amounts": [
    25.0,
    30.0
  ]
}
```
 Where id is the userId and amounts is a table of amount wich represent the list of giftCards or mealVoucher.
