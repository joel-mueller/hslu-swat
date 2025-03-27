# LibOS 📚
Projekt für das Modul Software Architecture and Techniques

## Unser Team
- Jeremy Funke
- Joel Müller

## Unser Tech Stack
- Java
- Spring
- MySQL oder MongoDB
- Maven

## Produktvision
Library Management System:
- CRUD Operationen für Bücher, Studenten und ausgeliehene Bücher
- Check-in/check-out Funktion für ausgeliehene Bücher
- Authentifizierung für Benutzer (Admin, Guest ect.) mit Rechten

## Run Spring Boot

```shell
mvn spring-boot:run
```

```shell
curl http://localhost:8080/api/hello
```

## Format Code

```shell
mvn formatter:format
```

## TODO

- [x] Code Formatter eingebaut
- [ ] Datebank anbindung fertig machen
- [ ] Mock datenbank erstellen für tests
- [ ] Tests libary (joel)
- [ ] Refactoring libary gemäss feedback (joel)
- [ ] User authifizierung
- [ ] Mapper schreiben für datenbank
- [ ] Paralellisierung
- [ ] Container Tests
