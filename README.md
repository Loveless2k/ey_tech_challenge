<h1> Mi Proyecto de API Rest</h1>
<p>Este proyecto consiste en una API RESTful desarrollada utilizando Spring Boot para administrar usuarios. Los usuarios pueden autenticarse y luego pueden crear, leer, actualizar y eliminar datos de usuario. El proyecto utiliza autenticación basada en token para autenticar a los usuarios y proporcionar acceso a los recursos de la API.</p>

<h2>Tecnologías y Herramientas Utilizadas</h2>
<ul>
  <li><b>Spring Boot:</b> Un framework de Java que simplifica el desarrollo de aplicaciones web. Se utilizó para crear la API RESTful y manejar las operaciones CRUD.</li>

  <li><b>Spring Security:</b> Se utilizó para implementar la autenticación basada en tokens y proteger los recursos de la API.</li>

  <li><b>JUnit y Mockito:</b> Se utilizaron para las pruebas unitarias y de integración, con el fin de garantizar que la lógica de la aplicación funcione correctamente. Sin embargo, las pruebas aún están en construcción y se están trabajando para garantizar una cobertura completa.</li>

  <li><b>Maven:</b> Esta herramienta se utilizó para manejar las dependencias del proyecto.</li>

  <li><b>H2 Database:</b> Una base de datos en memoria que se utilizó para el desarrollo y las pruebas.</li>

  <li><b>Hibernate:</b> Se utilizó como ORM (Object-Relational Mapping) para facilitar la interacción con la base de datos.</li>

  <li><b>Jackson:</b> Una biblioteca de Java que se utilizó para convertir objetos Java en JSON y viceversa.</li></ul>

<h2>Endpoints</h2>
<p>Los siguientes son los endpoints expuestos por la API:</p>

<ul><li><b>POST /login:</b> Este endpoint se utiliza para autenticar a un usuario. Se deben proporcionar un nombre de usuario y una contraseña en el cuerpo de la solicitud, y el endpoint devolverá un token si las credenciales son válidas.</li>

  <li><b>GET /users:</b> Este endpoint devuelve una lista de todos los usuarios activos. Requiere autenticación.</li>

  <li><b>GET /users/{id}:</b> Este endpoint devuelve los detalles de un usuario específico, donde {id} es el ID del usuario. Requiere autenticación.</li>

  <li><b>POST /users:</b> Este endpoint se utiliza para crear un nuevo usuario. Se deben proporcionar los detalles del usuario en el cuerpo de la solicitud. Devuelve los detalles del usuario creado. Requiere autenticación.</li></ul>

<p>Se pueden probar estos endpoints usando aplicaciones como Postman o Insomnia que permiten enviar solicitudes HTTP a la API.</p>

  <h2>Cómo Ejecutar el Proyecto</h2>
<ol><li>Clona el repositorio en tu máquina local.</li>
<li>Navega hasta el directorio del proyecto en tu terminal.</li>
<li>Ejecuta el comando mvn clean install para construir el proyecto y ejecutar las pruebas.</li>
<li>Luego, ejecuta el comando mvn spring-boot:run para iniciar la aplicación.</li>
<li>La aplicación estará en ejecución y escuchando en http://localhost:8080.</li></ol>
<p>Por favor, asegúrate de tener instalados Java y Maven en tu sistema para poder ejecutar el proyecto.</p>

<h2>Pruebas</h2>
<p>Las pruebas se pueden ejecutar con el comando mvn test. Esto ejecutará todas las pruebas unitarias y de integración y te proporcionará un resumen de los resultados. Nota: Las pruebas aún están en construcción y se están trabajando para garantizar una cobertura completa.</p>

<h2>Muestra</h2>
<p>Con insomnia tenemos nuestra lista de endpoints. Primero vamos a registrar un usuario. Para este endpoint no se requiere autenticación.</p>

![create user endpoint](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/e87defbe-13ee-46fa-bf8a-b1f9d36af4d0)

<p>Como podemos observar, el payload en formato Json para el método POST consta de los atributos:</p>
<ul>
  <li>name</li>
  <li>email</li>
  <li>password (En una etapa temprana del proyecto, se validaba con una expresión regular. Hoy, es un dato encriptado bajo el algoritmo Bcrypt</li>
  <li>gender (Agregado en esta versión)</li>
  <li>phones</li>
  <ul>
  <li>number</li>
  <li>citycode</li>
  <li>countrycode</li>
  </ul>
</ul>

<p>El resultado de enviar ese payload es el siguiente:<p>

![user create response](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/9752bd70-31d7-4bc6-8b9b-a72f59976a03)

<p>Si se revisa el Header de la respuesta, podemos encontrar el location que nos retorna el usuario y su UUID.</p>

![user create response header location](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/0d57f328-75ff-48e7-843f-eee9a3b8b247)

<p>Si se trata de re-enviar la petición, la respuesta cambia a "El correo ya existe".</p>

![user create response email already exists](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/778b1711-a83b-4b88-b7b9-b94c719a2bf3)

<p>Existen otras validaciones asociadas a los datos del usuario que saltan cuando se cumple la condición, como que la contraseña contenga como mínimo una mayúscula, 2 dígitos y minúsculas.
Bien, ya tenemos el UUID del usuario, que utilizaremos a lo largo de las peticiones. 
  Sabemos, por ejemplo, que el UUID de ricardo es <i>7d7e1836-6702-44ba-b382-5d365b5fc565</i>.
  La contraseña para cada usuario es <b>Adanny22</b>, que fue encriptada con el algoritmo Bcrypt.</p>

<p>Posteriormente, si se trata de listar a los usuarios (GET List Users), solo se tendrá como resultado un 403 Forbidden, porque aún no se ha autenticado ningún usuario de los que se han creado.</p>

![List users response](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/b2b8d453-58f1-4f0f-8525-9db66921da8e)

<p>Este comportamiento se repite para <b>PUT</b> Update User, <b>GET</b> User Data y <b>DEL</b> DELETE User. <b>POST</b> Register User y <b>POST</b> User Login son los únicos endpoints con acceso liberado.</p>

<p>Lo primero, entonces, será autenticar un usuario.
Vamos, luego, a <b>POST</b> User Login.</p>

![POST User Login](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/bdf9150b-e8ac-46e8-a9e7-d3b0262221e5)

<p>En el payload se tendrá el nombre "Ernesto" y la contraseña "Adanny22". Si bien la contraseña es correcta, no se ha creado ningún usuario llamado "Ernesto".
Por este motivo, al enviar la petición, al enviar la petición, la respuesta es un 403 Forbidden.</p>

![POST User Login response 403](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/46511dbd-9ea1-466a-9ca5-e063c8aca016)

<p>Ahora vamos a hacer que Ricardo Carte, usuario creado en pasos anteriores, haga login en el sistema.</p>
  
![user create response](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/2f68606d-5cad-4760-bb73-dcfabe68ca31)

<p>El resultado es una respuesta 200, que retorna el token JWT válido de ese usuario.
  Bien, ahora que se ha autenticado el usuario ¿Será que ya podrá listar a los usuarios GET List Users?.

Vamos a usar el token JWT de Ricardo como Bearer para el endpoint Get List Users</p>

![GET List Users Bearer](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/88c033a0-15e2-43f6-bdb0-2f3c7dca1475)

<p>Además, podemos ver que este endpoint está configurado para paginarse, en orden, en función del id de usuario</p>

![GET List Users paginated url](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/b74c3bda-3b9d-4c8e-a760-e2c5db79340a)

<p>Finalmente, si se envía esa petición, el servidor response con un 200 OK, mostrando el id, nombre, correo, teléfonos, fecha de creación, fecha de modificación, fecha de último login, token y estado de activación, para cada usuario creado.</p>

![GET List Users Response 200 OK](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/694fd469-6ee8-4685-864c-346f86a2851f)

<p>Ok, podemos ver que la fecha de modificación de Ricardo es nula, porque Ricardo no se ha modificado nunca. Y su fecha de last login difiere de la fecha de creación, porque efectivamente se hizo Login de Ricardo posteriormente a su creación.
Como estamos autenticados como Ricardo, vamos a modificar a otro usuario. Modificaremos a jorge, que tiene dos teléfonos y el correo jorge@gmail.com a través del endpoint PUT Update User:</p>

![PUT Update User Response 200 OK](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/0709461c-d7c4-4e4f-8387-bfb41ac48eb3)

<p>Hemos vuelto a utilizar el token JWT acá, para no tener problemas de autorización.
Además, hemos utilizado el ID de Jorge para hacer la modificación.
Se le modificó el correo a jorge a lucas@gmail.com, y se modificaron sus teléfonos. Ahora tiene 3 teléfonos.

Esta vez, para revisar los detalles del usuario jorge en más detalle, utilizaremos el endpoint <b>GET</b> Get User Data</p>

![GET User Data Response 200 OK](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/72d02d5d-f95b-4c55-af4d-ebee9d179c95)

<p>Acá podemos notar que el nombre del usuario aún es Jorge, pero ahora su correo es lucas@gmail.com y tiene 3 teléfonos.
Además, el campo modified se ha completado, ya no es nulo.
El usuario se ha modificado con éxito.

Procederemos a darlo de baja con DEL Delete User:</p>

![DEL Delete User 204 No Content](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/72d7c315-f78f-4861-a0cf-1c039bc97e53)

<p>Ahora podemos ver que la respuesta del servidor fue un 204 No Content, y no hay body de respuesta. Esto es lógico, dado que hemos eliminado al usuario.
Vamos a comprobar si se ha eliminado con éxito, buscando a Jorge en la lista de usuarios.</p>

![GET List Users searching for jorge](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/7c280747-92a1-4864-95e1-d11b9eafe879)

<p>No hay señal de jorge en ningúna de las páginas mostradas como respuesta.
Ésta fue la implementación de un borrado lógico, ya que realmente solo se ha cambiado el estado del usuario de activo a inactivo y dicho estado ha sido filtrado en la lista.

Con esto, hemos terminado el recorrido de esta aplicación restful.</p>

















