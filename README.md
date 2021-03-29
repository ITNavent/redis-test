# Indicaciones

Levantar un proceso redis local o con docker.
 
```
docker run -p 6379:6379 --name redis-stream -d redis:6.0
```

Si se quiere usar un stream sin grupo de consumidores el cliente Lettuce lo crea solo, pero si queremos que ese stream sea consumido por un grupo necesitamos crear el grupo antes.

Para crear el groupo de consumidores.

```
redis-cli

> xgroup create nombre-stream nombre-grupo $ mkstream

> scan 0 type stream

```

Para levantar la app tenemos 2 perfiles, consumidor y productor.

```
./gradlew bootRun --args='--spring.profiles.active=productor'
```

Para el consumidor, se generara un id de consumidor distinto para cada instancia dentro del grupo, para poder levantar varias instancias en paralelo.

```
./gradlew bootRun --args='--spring.profiles.active=consumidor'
```

