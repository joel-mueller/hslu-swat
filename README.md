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
Note: The Formatter is also built in the `mvn package` command, so you dont need to run this a lot

```shell
mvn formatter:format
```

## Run SonarQube

- start the sonar qube conainers in the directory of the project with `docker compose -f sonar.yaml up -d`
- open `http://localhost:9001/` and create a token on the website and copy it
- create a `sonar.env` file where you store the key like this `SONAR_TOKEN=<your-secret-token>`
- run the `sonar.sh` script
- see the latest changes on the site

I used following [article](https://medium.com/@denis.verkhovsky/sonarqube-with-docker-compose-complete-tutorial-2aaa8d0771d4) for how to install it.

## TODO

- [x] Code Formatter eingebaut
- [ ] Datebank anbindung fertig machen
- [ ] Mock datenbank erstellen für tests
- [ ] Tests libary (joel)
- [x] Refactoring libary gemäss feedback (joel)
- [ ] Libary in api einbinden
- [ ] User authifizierung
- [ ] Mapper schreiben für datenbank
- [ ] Paralellisierung
- [ ] Container Tests
