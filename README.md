# ApiFinalBackend

Este proyecto es una API desarrollada en **Spring Boot** que permite consultar el tipo de cambio del día desde un servicio SOAP del Banco de Guatemala y gestionar un historial de solicitudes.

## Características principales
- Consulta del tipo de cambio del día a través de un servicio SOAP.
- Almacenamiento del historial de solicitudes en una base de datos PostgreSQL.
- Exposición de endpoints REST para consumir los datos.

## Requisitos previos
Antes de comenzar, asegúrate de tener instaladas las siguientes herramientas:

1. **Java 17 o superior**
   - Puedes instalarlo en macOS con Homebrew:
     ```bash
     brew install openjdk@17
     ```
   - Configura `JAVA_HOME`:
     ```bash
     export JAVA_HOME=$(/usr/libexec/java_home -v 17)
     export PATH=$JAVA_HOME/bin:$PATH
     ```

2. **Maven**
   - Instálalo en macOS con Homebrew:
     ```bash
     brew install maven
     ```

3. **PostgreSQL**
   - Asegúrate de que PostgreSQL esté instalado y ejecutándose.
   - Crea una base de datos llamada `neondb` o configura otra en `application.properties`.

4. **Git**
   - Si no tienes Git instalado, puedes descargarlo desde [Git](https://git-scm.com/).

## Pasos para configurar y ejecutar el proyecto

### 1. Clonar el repositorio
Clona este repositorio en tu máquina local:
```bash
git clone <URL_DEL_REPOSITORIO>
cd ApiFinalBackend

2. Configurar la base de datos

Abre el archivo src/main/resources/application.properties y asegúrate de configurar las credenciales correctas para PostgreSQL:

spring.datasource.url=jdbc:postgresql://<HOST>:<PUERTO>/<NOMBRE_BD>?sslmode=require
spring.datasource.username=<USUARIO>
spring.datasource.password=<CONTRASEÑA>

Ejemplo:

spring.datasource.url=jdbc:postgresql://localhost:5432/neondb
spring.datasource.username=postgres
spring.datasource.password=postgres

3. Construir el proyecto

Compila y construye el proyecto utilizando Maven:

mvn clean install

4. Ejecutar la aplicación

Ejecuta la aplicación con el siguiente comando:

mvn spring-boot:run

5. Acceso a los endpoints

Una vez que el servidor esté en ejecución (por defecto en el puerto 8080), puedes acceder a los siguientes endpoints:

Obtener el tipo de cambio del día

GET http://localhost:8080/tipoCambioDia

Obtener el historial de solicitudes

GET http://localhost:8080/historial

6. Probar la API

Puedes probar los endpoints utilizando herramientas como Postman o cURL.

Ejemplo con cURL:

curl -X GET http://localhost:8080/tipoCambioDia -H "Accept: application/json"
curl -X GET http://localhost:8080/historial -H "Accept: application/json"