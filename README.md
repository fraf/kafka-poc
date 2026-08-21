# kafka-poc
Mise en place d'une webapp spring avec kafka comme broker

## Commit #12428e6

![Initial commit avec Spring Initializr](spring_initializr.jpg)

## Commit #6833241

1. Architecture Hexagonale/Clean Code/DDD
   - fr.poc.kafka (src/main/java)
     - **Config** (spring boot configuration)
     - **[Bounded Context]** (µService)
       - **Application** (business features access)
       - **Domain** (centric business oriented)
         - *ent* (DDD entities)
         - *vo* (DDD value objects)
         - *ports* (outbound interfaces)
           - *primary* (input into model - interfaces)
           - *secondary* (output from model - interfaces)
         - *services* (centric business rules)          
       - **Infrastructure** (centric technical oriented)
         - *mappers* (converters from/to domain entities)
         - *primary*
           - *dtos* (presentation layer)
           - *kafka* (message broker consumers)
           - *rest* (rest services api)
         - *secondary*
           - *entities* (persistent object)
           - *kafka* (message broker producers)
           - *repository* (data access objects)
     - `MainApp.java` (project root package)

   - fr.poc.kafka (src/main/resources)
     - **db.migration** (flyway db update scripts)
     - `application.yml` (spring boot key/value configuration)
     - `logback-spring.yml` (Spring boot's logback integration configuration file)

2. Tests d'intégration avec les containers
   - Nécessite un client docker où est installer Intellij Serveur  (*docker desktop* pour windows, *wsl2* pour linux avec intellij en remote)
   - Variables d'environnement :
     ```
          DOCKER_HOST=tcp://localhost:2375
          JAVA_HOME=path/to/jdk/root/dir
     ``` 

Pour permettre le reuse des container, il faut mettre le fichier `.testcontainers.properties` dans `%USERPROFILE%`
La valeur à true permet de laisser l'instance de container après les tests executés.
Attention les container persistent, si on veut les supprimer entrer la commande `docker rm -f $(docker ps -aq)`

## Commit "Ajout qualité de code sonar"

Le plugin Jacoco permet de se greffer au processus JUnit pour calculer le taux de couverture de code. Le rapport généré est directement envoyé au sonar.
Le plugin sonar-maven permet de détecter les code issues.

### Activation mode debug pour IT

Par défaut, le processus maven avec le goal `verify` lance les IT dans un autre thread que le sien (process maven).
Le build maven et IT JUnit sont donc séparés dans 2 processus distincts.
Par conséquent, durant l'éxécution de `maven verify`, on ne peut pas mettre de breakpoint dans le code de test.
Pour se faire il faut configurer le plugin des IT `maven-failsafe`, il existe deux façons :
>- Faire du remote debug. IJ se connecte au process JUnit et tient compte des breakpoint. Il faut lancer les 2 process, maven verify et le remote debugger IJ.
>- Forcer maven à faire du monothread. Avec Forkount=0, le process de build et les IT JUnit sont dans le même thread. Build moins performant, mais au moins on peut faire marcher les breakpoint.

**Note :** A noter que le jacoco ne marche que si maven et les IT Junit sont dans un thread séparé (par défaut).
Pour le mode debug, décommenter la ligne `<forkCount>0</forkCount>` du plugin `maven-failsafe`.

# Killer le listener tomcat avec son port d'écoute

Sous powershell :
```
netstat -ano | findstr :8080
taskkill /PID THE_PID_HERE /F
```

## Lancement de kafka avec docker sous WSL2

Les fichiers nécessaires sont dans `/docker/*`. Il contient :
- `docker-compose-kafka.yml` : Permet de lancer un cluster kafka composé d'un seul broker. 
Les listeners qui sont à l'écoute des requêtes clients à 3 interfaces : 
  - host (en provenance de l'extérieur du docker donc réseau localhost)
  - docker (en provenance d'un client situé dans un autre container sous docker donc même réseau)
  - controller (en provenance de kraft pour la synchronisation des clusters kafka)


Ci-dessous les commandes docker pour opérer avec le cluster `kafka` : 
- Lancement du kafka cluster ET de l'interface kafka ui avec `docker compose` (mode daemon ou ctrl+Z en mode interactif)
- production des messages
- consommation des messages
- connexion au broker si configuration necessaire

```
docker compose -f docker-compose.yml up -d
docker exec -ti kafka-broker /opt/kafka/bin/kafka-console-producer.sh --bootstrap-server :9092 --topic demo
docker exec -ti kafka-broker /opt/kafka/bin/kafka-console-consumer.sh --command-config /home/appuser/config-consumer.properties --bootstrap-server :9092 --topic demo
docker exec -ti kafka-broker /bin/bash
``` 

L'id du consumer group étant défini, le consumer sera toujours identifié et aura son propre offset de sa partition lue. 
A l'inverse du consumer anonyme qui n'a pas d'appartenance et lit soit sur écoute du producer soit toute la file depuis le début (`--from-beginning`)

*TODO* 
> - OpenAuth avec Keycloack
> - db.migration pour le dataset de test
> - s'intruire sur la scalabilite des bdd (multitenant)
> - Le Domain Driven Design
> - Le Test Driven Development (TDD)
> - Le Clean Code
> - BDD (Behavior driven developpement)
> - PDD (Prompt driven developpement)
> - Craftmanship (logiciel bien conçu)
> - Java-Based No-Code and Low-Code Application
> - Le DevSecOps, l’infrastructure as code
> - principes SOLID
> - langchain4j (github et comparaison LLM)
> - Etudier la JVM la structure en Heap, gestion mémoire
> - WebSocket qui affiche les notifications du consumer
> - Serveless (environnement AWS cloud)

Il existe plusieurs cases couramment utilisées : comme la camelCase, la snake_case, la PascalCase, kebab-case ou encore l’UPPER_CASE

*WORD CLOUD*

Person, Account with Profil (announce, buys, parameters - identiy/pass/notif/adress/payments), Preferences with Favorites (search, products)

Order : one product and 2 accounts 

Delivery : carrier, price, location 

Product focus : Favorite, description, title, state, price, location, weight, volume 

Workflow Creation : state : draft, created, updated, deleted 

Catalog, Research, Classification (tag, category with filters, fulltext)