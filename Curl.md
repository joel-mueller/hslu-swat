# Curl Commands

## Add customer

```shell
curl -X POST http://localhost:8080/api/customer \
     -H "Content-Type: application/json" \
     -d '{
           "firstName": "John",
           "lastName": "Doe",
           "street": "123 Main St",
           "zipCode": "12345"
         }'
```