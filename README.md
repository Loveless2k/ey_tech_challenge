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

  <li><b>DEL /users/{id}</b> Este endpoint prepara el borrado lógico del usuario, en la plataforma.

<p>Se pueden probar estos endpoints usando aplicaciones como Postman o Insomnia que permiten enviar solicitudes HTTP a la API.</p>

<h2> Script para la base de datos </h2>

<p>Se ha utilizado Flyway como herramienta en este proyecto, por lo que es posible encontrar los script para la base de datos en la carpeta db/migration dentro del proyecto, en formato H2.</p>

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

![create user endpoint](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/ca5ea0d5-56fe-4133-9f5d-fab691928412)

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

![user create response](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/6abd2779-5f08-48dc-b737-e8384e1287b0)

<p>Si se revisa el Header de la respuesta, podemos encontrar el location que nos retorna el usuario y su UUID.</p>

![user create response header location](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/43370477-232b-469b-9149-e49ad73cd803)

<p>Si se trata de re-enviar la petición, la respuesta cambia a "El correo ya existe".</p>

![user create response email already exists](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/2a64a05d-b19c-4eb3-aed4-6e84c91c2b7c)

<p>Existen otras validaciones asociadas a los datos del usuario que saltan cuando se cumple la condición, como que la contraseña contenga como mínimo una mayúscula, 2 dígitos y minúsculas.
Bien, ya tenemos el UUID del usuario, que utilizaremos a lo largo de las peticiones. 
  Sabemos, por ejemplo, que el UUID de ricardo es <i>7d7e1836-6702-44ba-b382-5d365b5fc565</i>.
  La contraseña para cada usuario es <b>Adanny22</b>, que fue encriptada con el algoritmo Bcrypt.</p>

<p>Posteriormente, si se trata de listar a los usuarios (GET List Users), solo se tendrá como resultado un 403 Forbidden, porque aún no se ha autenticado ningún usuario de los que se han creado.</p>

![List users response](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/12a92814-49c8-4dc3-8505-22fea7dd4e56)

<p>Este comportamiento se repite para <b>PUT</b> Update User, <b>GET</b> User Data y <b>DEL</b> DELETE User. <b>POST</b> Register User y <b>POST</b> User Login son los únicos endpoints con acceso liberado.</p>

<p>Lo primero, entonces, será autenticar un usuario.
Vamos, luego, a <b>POST</b> User Login.</p>

![POST User Login](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/287edcf4-70aa-458f-a1b7-fadb8b827103)

<p>En el payload se tendrá el nombre "Ernesto" y la contraseña "Adanny22". Si bien la contraseña es correcta, no se ha creado ningún usuario llamado "Ernesto".
Por este motivo, al enviar la petición, al enviar la petición, la respuesta es un 403 Forbidden.</p>

![POST User Login response 403](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/7852fd0e-7279-40ad-b979-b460a6d13fbb)

<p>Ahora vamos a hacer que Ricardo Carte, usuario creado en pasos anteriores, haga login en el sistema.</p>
  
![user create response](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/16ed7632-fa90-41c6-a8c4-c25e50800aae)

<p>El resultado es una respuesta 200, que retorna el token JWT válido de ese usuario.
  Bien, ahora que se ha autenticado el usuario ¿Será que ya podrá listar a los usuarios GET List Users?.

Vamos a usar el token JWT de Ricardo como Bearer para el endpoint Get List Users</p>

![GET List Users Bearer](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/82846f25-af95-4608-9d92-41c7cf7cf61d)

<p>Además, podemos ver que este endpoint está configurado para paginarse, en orden, en función del id de usuario</p>

![GET List Users paginated url](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/e21a5fb0-e0ca-4831-b17b-988704af942e)

<p>Finalmente, si se envía esa petición, el servidor response con un 200 OK, mostrando el id, nombre, correo, teléfonos, fecha de creación, fecha de modificación, fecha de último login, token y estado de activación, para cada usuario creado.</p>

![GET List Users Response 200 OK](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/f24f51b4-2789-4e5d-a9ee-ab6c6b0b2cd3)

<p>Ok, podemos ver que la fecha de modificación de Ricardo es nula, porque Ricardo no se ha modificado nunca. Y su fecha de last login difiere de la fecha de creación, porque efectivamente se hizo Login de Ricardo posteriormente a su creación.
Como estamos autenticados como Ricardo, vamos a modificar a otro usuario. Modificaremos a jorge, que tiene dos teléfonos y el correo jorge@gmail.com a través del endpoint PUT Update User:</p>

![PUT Update User Response 200 OK](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/3f725c75-cef0-418f-af8c-0230675d242f)

<p>Hemos vuelto a utilizar el token JWT acá, para no tener problemas de autorización.
Además, hemos utilizado el ID de Jorge para hacer la modificación.
Se le modificó el correo a jorge a lucas@gmail.com, y se modificaron sus teléfonos. Ahora tiene 3 teléfonos.

Esta vez, para revisar los detalles del usuario jorge en más detalle, utilizaremos el endpoint <b>GET</b> Get User Data</p>

![GET User Data Response 200 OK](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/97987152-35f4-4e7e-92c4-68403499ffe5)

<p>Acá podemos notar que el nombre del usuario aún es Jorge, pero ahora su correo es lucas@gmail.com y tiene 3 teléfonos.
Además, el campo modified se ha completado, ya no es nulo.
El usuario se ha modificado con éxito.

Procederemos a darlo de baja con DEL Delete User:</p>

![DEL Delete User 204 No Content](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/034f3cd6-8cfe-4e40-ad2c-f0a499939f15)

<p>Ahora podemos ver que la respuesta del servidor fue un 204 No Content, y no hay body de respuesta. Esto es lógico, dado que hemos eliminado al usuario.
Vamos a comprobar si se ha eliminado con éxito, buscando a Jorge en la lista de usuarios.</p>

![GET List Users searching for jorge](https://github.com/Loveless2k/ey_tech_challenge/assets/130776120/aca79b59-824f-4543-8fc7-9ce1519b8aa7)

<p>No hay señal de jorge en ningúna de las páginas mostradas como respuesta.
Ésta fue la implementación de un borrado lógico, ya que realmente solo se ha cambiado el estado del usuario de activo a inactivo y dicho estado ha sido filtrado en la lista.

Con esto, hemos terminado el recorrido de esta aplicación restful.</p>

<h2>Diagrama de la solución</h2>

<p>En construcción, debido al tiempo no me ha sido posible aún generar el diagrama.</p>
















