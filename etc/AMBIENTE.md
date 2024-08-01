# Sobre o ambiente de desenvolvimento

Para executar as soluções em ambiente `localhost`, pode ser interessante ter instâncias de banco de dados, fila, cache distribuido e repositório de objetos instaladas localmente.

Aqui tem um pequeno guia sobre como usar o Docker para instalar a maioria das ferramentas necessárias no seu ambiente local.

## Execução concomitante

A melhor e mais fácil das opções. Desta forma, basta garantir que a variável ambiente `SPRING_DOCKER_COMPOSE_ENABLED` está definida com o valor `true`. Ao subir, a aplicação irá, automaticamente, fazer uso do arquivo `docker-compose,yaml` e configurar todos os containeres automaticamente. Ao parar, a aplicação vai parar todos os containeres também de forma automática.


>> Para as opções a seguir, definir a variável ambiente `SPRING_DOCKER_COMPOSE_ENABLED` com o valor `false`.


## Instalação automática

Temos um arquivo `docker-compose.yaml` preparado para instalar as soluções padrão. Para adotar a instalação automática, execute o comando à partir desta pasta:

```shell
docker compose -p getInsight up -d
```

Veja abaixo as credenciais e URLs de cada serviço, quando aplicável.

## Instalação Manual

Caso você não deseje instalar todas as soluções, veja abaixo os guias de instalação individual de cada solução.

### PostgreSQL (Banco de dados relacional)

Esta solução exige mapear um volume para funcionar adequadamente. Crie um usando o comando:

```shell
docker volume create postgres
```

Em seguidam para instalar o PostgreSQL:

```shell
 docker run -d -p 5433:5432 --name=PostgreSQL --restart=always -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -v postgres:/var/lib/postgresql/data postgres:15-alpine
```

O banco de dados estará disponível no seu `localhost`, na porta `5432` com as credenciais:

- Login: `postgres`
- Senha: `postgres`

**Importante**: certifique-se de criar, quando for necessário, um banco de dados diferente do padrão para usar com sua aplicação.

### RabbitMQ (Fila)

Esta solução exige mapear um volume para funcionar adequadamente. Crie um usando o comando:

```shell
docker volume create rabbitmq
```

Em seguida, para instalar o RabbitMQ:

```shell
docker run -d -p 5672:5672 -p 15672:15672 --name=RabbitMQ --restart=always -v rabbitmq:/var/lib/rabbitmq rabbitmq:management-alpine
```

O RabbitMQ estará disponível para uso em `localhost` na porta AMQP `5672` e a interface de gerenciamento em http://localhost:15672. Ambos com as credenciais:

- Login: `guest`
- Senha: `guest`

### Redis (Cache Distribuído)

Esta solução exige mapear um volume para funcionar adequadamente. Crie um usando o comando:

```shell
docker volume create redis
```

Em seguida, para instalar o Redis:

```shell
docker run -d -p 6379:6379 --name=Redis --restart=always -v redis:/data redis:alpine
```

O Redis estará disponível para uso em `localhost` na porta `6379` e não exigirá autenticação.


### MinIO (Bucket)

Esta solução exige mapear um volume para funcionar adequadamente. Crie um usando o comando:

```shell
docker volume create minio
```

Em seguida, para instalar o MinIO:

```shell
docker run -d -p 9100:9000 --name=MinIO --restart=always -e MINIO_ACCESS_KEY=admin -e MINIO_SECRET_KEY=abc12345 -v minio:/data minio/minio:latest server /data
```

O MinIO estará disponível para uso em http://localhost:9100, com as credenciais:

- Access key: `admin`
- Secret key: `abc12345`


### MongoDB (Banco de dados não relacional)

Esta solução exige mapear um volume para funcionar adequadamente. Crie um usando o comando:

```shell
docker volume create mongo
```
Em seguida, para instalar o MongoDB:

<sub>Modo Cluster</sub>
```shell
docker run -d -p 27017:27017 --name=MongoDB --restart=always -e MONGODB_USERNAME=user -e MONGODB_PASSWORD=senha -e MONGODB_DATABASE=dbTeste -e MONGODB_REPLICA_SET_MODE=primary -e MONGODB_REPLICA_SET_KEY=replicasetkey123 -v mongo:/bitnami/mongodb bitnami/mongodb:latest
```

<sub>Modo Normal</sub>
```shell
docker run -d -p 27017:27017 --name=MongoDB --restart=always -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=123456 -v mongo:/data/db mongo:latest
``` 

O MongoDB estará disponível em `localhost` na porta `27017 com as credenciais:

- Login: `usuario`
- Senha: `senha`
- Banco de dados padrão: `dbTeste`


### [Opcional] Portainer (GUI para Docker)

Esta solução exige mapear um volume para funcionar adequadamente. Crie um usando o comando:

```shell
docker volume create portainer_data
```
Em seguida, para instalar o Portainer:

```shell
docker run -d -p 9000:9000 --name=portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce
```

O Portainer estará disponível para uso em http://localhost:9000. As credenciais serão criadas por você, no primeiro acesso.

### [Opcional] ElasticSearch

Este é um serviço específico para projetos que usam esta solução. Converse com um arquiteto para saber mais.

```shell
docker run -d --name elasticsearch --restart=always -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.12.0
```

#### [Situacional] Microsoft SQL Server

Esta ferramenta necessita de um volume para salvar os dados. Cria-se um com o comando:

```shell
docker volume create mssql
```

Para instalar e executar o Microsoft SQL Server no Docker, use o comando:

```shell
docker run \
  -e "ACCEPT_EULA=Y" \
  -e "SA_PASSWORD=Java1234" \
  -e "MSSQL_PID=Developer" \
  -v mssql:/var/opt/mssql \
  -p 1433:1433 \
  --name=SQLServer \
  -d mcr.microsoft.com/mssql/server:2019-latest
```

##### Computadores com Apple Silicon ou ARM64

Em computadores com Apple Silicon (e qualquer outro com arquitetura ARM64), a imagem a ser executada muda. O comando para criação do volume permanece o mesmo, porém o comando para instalar e executar passa a ser este:

```shell
docker run \
  -e "ACCEPT_EULA=Y" \
  -e "SA_PASSWORD=Java1234" \
  -e "MSSQL_PID=Developer" \
  -v mssql:/var/opt/mssql \
  -p 1433:1433 \
  --name=SQLServer \
  -d mcr.microsoft.com/azure-sql-edge
```
