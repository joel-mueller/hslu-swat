# Curl Commands

## Add customer

```shell
curl -X POST http://localhost:8080/api/customer \
-d "firstName=John" \
-d "lastName=Doe" \
-d "street=123 Main St" \
-d "zipCode=12345"
```
