<img src="https://getinsight.it/assets/images/logo-getinsight-486x123.png" style="background: black;">

# Projeto Backend - SpringBoot

![java](https://badgen.net/badge/Java/17/red?scale=1.2) ![spring-boot](https://badgen.net/badge/SpringBoot/3.1/green?scale=1.2) ![maven](https://badgen.net/badge/Maven/3.9.4/blue?scale=1.2)

> “Fear is the path to the dark side. Fear leads to anger. Anger leads to hate. Hate leads to suffering.” — Yoda

**Sumário**

- [Projeto Backend - Spring Boot](#projeto-backend---spring-boot)
  - [Ambiente de Desenvolvimento](#ambiente-de-desenvolvimento)
  - [Sobre o projeto](#sobre-o-projeto)
  - [Pré-requisitos](#pr-requisitos)
  - [Criar um novo projeto usando o archetype](#criar-um-novo-projeto-usando-o-archetype)
  - [Build e execução local](#build-e-execu%c3%a7%c3%a3o-local)
    - [Com arquivo .jar](#com-arquivo-jar)
    - [Com Docker](#com-docker)
  - [Acessar o projeto](#acessar-o-projeto)
  - [Upload de archetype no nexus](#upload-de-archetype-no-nexus)

## Ambiente de Desenvolvimento

Algumas instruções úteis sobre como instalar ferramentas para o ambiente de desenvolvimento local podem ser encontradas no arquivo [AMBIENTE](etc/AMBIENTE.md).

## Sobre o projeto

Trata-se de um projeto desenvolvido para ser referência e acelerar a configuração inicial de novos projetos.
Ele é baseado no documento de arquitetura corporativa **getInsight** backend
que utiliza SpringBoot e integração com OIDC (Keycloak), isso
que dizer que é necessário já ter o token para acessar os endpoints RESTful.

## Pré-requisitos

Para continuar, atente aos pré-requisitos:

- Java JDK 17
  - Como instalar?
    - Via [SDKMan](https://sdkman.io): `sdk install java 17.0.8-tem` (**recomendado**)
    - Instalação manual: [baixar](https://adoptium.net/temurin/releases?version=17)
- Maven 3.9.4
  - Como instalar?
    -  Via [SDKMan](https://sdkman.io): `sdk install maven 3.9.4` (**recomendado**)
    - Instalação manual: [baixar](https://maven.apache.org/download.cgi)
- Docker
  - Como instalar?
    - Instalação manual: [baixar](https://www.docker.com/products/docker-desktop/)
- Docker Compose V2 (já é instalado com o link acima)

## Criar um novo projeto usando o _archetype_

O archetype do projeto está publicado no [nosso repositório de pacotes no GitLab](https://gitlab.com/getinsight.it/arquitetura/registry/-/packages) o que possibilita criar um novo projeto usando-o como referência.

Para gerar um novo projeto chamado nome-do-seu-projeto siga os passos abaixo:

* Crie o arquivo `settings.xml` na pasta `.m2` do usuário logado configurado de acordo com a [documentação oficial](https://docs.gitlab.com/ee/user/packages/maven_repository/#authenticate-to-the-package-registry-with-maven):

- Windows:
```
%HOME_DRIVE%%HOME_PATH%\.m2\settings.xml
```
- Linux e macOS:
```
~/.m2/settings.xml
```

settings.xml:

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <servers>
        <server>
            <id>gitlab-maven</id>
            <configuration>
                <httpHeaders>
                    <property>
                        <name>Private-Token</name>
                        <value>seu-private-token</value>
                    </property>
                </httpHeaders>
            </configuration>
        </server>
    </servers>

</settings>
```

* Execute o comando:

``` bash
mvn archetype:generate -DgroupId=it.getinsight -Dversion=1.0.0-SNAPSHOT -DpackageName=it.getinsight -DarchetypeGroupId=it.getinsight -DarchetypeArtifactId=backend-accesspilot-archetype -DarchetypeVersion=2.0.2 -DinteractiveMode=true
```

## Build e execução local

### Com arquivo .jar
Para gerar a versão executável do projeto com a extensão `.jar` é necessário executar o comando abaixo no diretório raiz:

```bash
mvn clean package
```

Execute o comando abaixo para iniciar o projeto

```bash
java -jar target\nome-do-seu-projeto.jar
```

### Com Docker

#### Autenticação

Para seguir os passos abaixo, pode ser necessário se autenticar junto ao registry. Para isso, execute o comando e informar suas credencias de acesso ou Token de acesso pessoal, caso sua conta no GitLab possua autenticação de dois fatores configurada.

```bash
docker login registry.gitlab.com
```

Para gerar a versão executável do projeto com Docker é necessário executar o comando abaixo no diretório raiz:

```bash
docker build -t nome-do-seu-projeto:1 .
```

Obs.: Substitua `nome-do-seu-projeto` pelo nome do projeto que foi gerado.

## Acessar o projeto

Para acessar o projeto digite no navegador o endereço: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## Upload de archetype no registry

Para gerar o archetype deste projeto execute os comandos abaixo:

```bash
mvn archetype:create-from-project
```
entre na pasta que foi gerado o archetype

```bash
cd target/generated-sources/archetype
````

faça o upload

```bash
mvn deploy -DaltDeploymentRepository=gitlab-maven::default::https://gitlab.com/api/v4/projects/33054505/packages/maven
```

Caso não funcione coloque o seguinte código dentro do `pom.xml` no _archetype_ que foi gerado:

``` xml
<distributionManagement>
  <repository>
      <id>gitlab-maven</id>
      <url>https://gitlab.com/api/v4/projects/33054505/packages/maven</url>
  </repository>
</distributionManagement>
```
Em seguida execute o seguinte comando:

```bash
mvn deploy
```

