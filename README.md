# MS Client Service

Este servicio se encarga de la creación, modificación y desactivación de clientes del banco.

## Aplicación

### 2.1. Pre-Requisitos
* Tener instalado [**Java 17**](https://www.oracle.com/java/technologies/downloads/).

Esta aplicación se conecta con **MySQL**  por lo que deberá tener una imagen o servicio en su local.

Para ejecutar el microservicio y los tests correctamente, asegúrate de tener correctamente configuradas las siguientes variables de entorno:

- `server.port`: ${SERVER_PORT} Define el puerto de la aplicación, por defecto es 8081

- `spring.account.service`: ${ACCOUNT_SERVICE} Esta variable especifica la URL del servicio de cuentas bancarias al que el microservicio se conecta. Por defecto, apunta a `http://localhost:8082/api/cuentas`.

- `spring.datasource.url`: ${DATABASE_URL} Esta variable define la URL de conexión a la base de datos.

- `spring.datasource.username`: ${DATABASE_USERNAME} Nombre de usuario de la base de datos.

- `spring.datasource.password`: ${DATABASE_PASSWORD} Contraseña de la base de datos.

Las variables de entorno se utilizan para configurar aspectos específicos de la aplicación, como la conexión a la base de datos y la comunicación con otros servicios.

## Endpoints

- `GET /api/clientes/health-check`: Retorna un mensaje indicando que el servicio está activo.

- `POST /api/clientes/nuevo`: Crea un nuevo cliente. Se espera un objeto JSON con la información del cliente en el cuerpo de la solicitud.

- `GET /api/clientes/{nit}`: Retorna la información del cliente correspondiente al número de identificación fiscal (NIT) especificado.

- `GET /api/clientes/todos`: Retorna todos los clientes. Se puede especificar el orden ascendente ("ASC") o descendente ("DESC") utilizando el parámetro de consulta `direction`.

- `GET /api/clientes/todos/page`: Retorna una lista paginada de clientes. Se puede especificar el número de página, el tamaño de página y el orden ascendente o descendente utilizando los parámetros de consulta correspondientes `direction`, `pageNumber` y `pageSize`.

- `PUT /api/clientes/{nit}`: Actualiza la información del cliente correspondiente al NIT especificado. Se espera un objeto JSON con la información actualizada del cliente en el cuerpo de la solicitud.

- `PUT /api/clientes/desactivar/{nit}`: Desactiva el cliente correspondiente al NIT especificado.

- `DELETE /api/clientes/{nit}`: Elimina el cliente correspondiente al NIT especificado.

### ⚠️ Alerta ⚠️

Siempre prefiera usar la desactivación que la eliminación. Esto evitará que tenga data inconsistente.

## Crear imagen

Para crear la imagen ejecute
 ```shell script
  podman build -t ms-client-service:latest -f Dockerfile .
  ```