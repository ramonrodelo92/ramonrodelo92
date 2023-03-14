# test
backend test with java using spring boot

# RUN

Setear las variables de entorno en archivo "env.bat".
Ejecutar "call env.bat" desde CMD para setear variables de entorno.
Ejecutar el comando "mvnw.cmd spring-boot:run" desde CMD.

# RUN TESTS

Setea la variable de entorno DB_URL con base de datos para pruebas.
Los tests se ejecutaran solo si existe "test" en la variable "DB_URL" ejemplo "jdbc:postgresql://host:5432/dbtest" para evitar modificar bases de datos que no sean para pruebas.
Ejecutar el comando "mvnw.cmd test -Dtest=TestApplicationControllerTests".
