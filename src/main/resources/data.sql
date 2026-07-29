INSERT INTO roles (nombre)
SELECT 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ROLE_ADMIN');

INSERT INTO roles (nombre)
SELECT 'ROLE_USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ROLE_USER');

INSERT INTO usuarios (username, password, email, enabled)
SELECT 'admin', '$2a$10$zSogB7TH/NggiaQQ.2OHz.ehYkuDxkxeeBVZ4uJlzrsLb7ObK1a92', 'admin@ejemplo.com', true
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'admin');

INSERT INTO usuarios (username, password, email, enabled)
SELECT 'user', '$2a$10$2IUXb6X7PsBoB3GAMSdAHebi7S4IP4wYI0N1n5RuqWJraZvDx4GUq', 'user@ejemplo.com', true
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'user');

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 1 AND rol_id = 1);

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT 1, 2 WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 1 AND rol_id = 2);

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT 2, 2 WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 2 AND rol_id = 2);

INSERT INTO tematica (nombre)
SELECT 'Java Básico' WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nombre = 'Java Básico');

INSERT INTO tematica (nombre)
SELECT 'Spring Boot' WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nombre = 'Spring Boot');

INSERT INTO tematica (nombre)
SELECT 'Bases de Datos' WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nombre = 'Bases de Datos');

INSERT INTO tematica (nombre)
SELECT 'HTML y CSS' WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nombre = 'HTML y CSS');

INSERT INTO tematica (nombre)
SELECT 'Git y Control de Versiones' WHERE NOT EXISTS (SELECT 1 FROM tematica WHERE nombre = 'Git y Control de Versiones');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Cuál es la palabra clave para definir una clase en Java?', 'class', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuál es la palabra clave para definir una clase en Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué método es el punto de entrada de un programa Java?', 'main', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué método es el punto de entrada de un programa Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Cuál es el tipo de dato primitivo para un número entero de 32 bits?', 'int', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuál es el tipo de dato primitivo para un número entero de 32 bits?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué palabra clave se usa para heredar de una clase en Java?', 'extends', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué palabra clave se usa para heredar de una clase en Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación se usa para indicar que un método sobrescribe uno de la clase padre?', '@Override', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación se usa para indicar que un método sobrescribe uno de la clase padre?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Cuál es el valor por defecto de una variable booleana en Java?', 'false', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuál es el valor por defecto de una variable booleana en Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué interfaz se usa para ordenar una lista con Collections.sort()?', 'Comparable', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué interfaz se usa para ordenar una lista con Collections.sort()?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué palabra clave evita que una clase sea heredada?', 'final', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué palabra clave evita que una clase sea heredada?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Cómo se declara una constante en Java?', 'final', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cómo se declara una constante en Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué colección no permite elementos duplicados?', 'Set', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué colección no permite elementos duplicados?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación marca una clase como componente de Spring?', '@Component', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación marca una clase como componente de Spring?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación se usa para inyectar dependencias en Spring?', '@Autowired', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación se usa para inyectar dependencias en Spring?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación convierte una clase en un controlador REST?', '@RestController', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación convierte una clase en un controlador REST?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué dependencia de Spring Boot permite crear aplicaciones web?', 'spring-boot-starter-web', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué dependencia de Spring Boot permite crear aplicaciones web?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación mapea una petición GET a un método?', '@GetMapping', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación mapea una petición GET a un método?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué archivo contiene la configuración de una aplicación Spring Boot?', 'application.properties', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué archivo contiene la configuración de una aplicación Spring Boot?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué motor de plantillas usa Spring Boot por defecto?', 'Thymeleaf', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué motor de plantillas usa Spring Boot por defecto?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación define una clase como servicio en Spring?', '@Service', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación define una clase como servicio en Spring?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación se usa para mapear una entidad JPA?', '@Entity', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación se usa para mapear una entidad JPA?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué anotación indica el campo que es clave primaria?', '@Id', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué anotación indica el campo que es clave primaria?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué significa la sigla SQL?', 'Structured Query Language', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué significa la sigla SQL?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando se usa para seleccionar datos de una tabla?', 'SELECT', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando se usa para seleccionar datos de una tabla?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué cláusula filtra registros en una consulta SQL?', 'WHERE', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué cláusula filtra registros en una consulta SQL?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué tipo de JOIN devuelve solo los registros coincidentes de ambas tablas?', 'INNER JOIN', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué tipo de JOIN devuelve solo los registros coincidentes de ambas tablas?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando se usa para insertar datos en una tabla?', 'INSERT', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando se usa para insertar datos en una tabla?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando elimina una tabla completa de la base de datos?', 'DROP TABLE', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando elimina una tabla completa de la base de datos?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué función devuelve el número de filas de una consulta?', 'COUNT', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué función devuelve el número de filas de una consulta?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué cláusula se usa para agrupar filas en SQL?', 'GROUP BY', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué cláusula se usa para agrupar filas en SQL?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué es una clave foránea?', 'Un campo que referencia la clave primaria de otra tabla', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué es una clave foránea?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando modifica datos existentes en una tabla?', 'UPDATE', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando modifica datos existentes en una tabla?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué etiqueta HTML define un párrafo?', '<p>', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué etiqueta HTML define un párrafo?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué atributo HTML se usa para enlazar una hoja de estilos?', 'href', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué atributo HTML se usa para enlazar una hoja de estilos?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué propiedad CSS cambia el color del texto?', 'color', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué propiedad CSS cambia el color del texto?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué etiqueta HTML define un enlace?', '<a>', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué etiqueta HTML define un enlace?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué propiedad CSS define el tamaño de la fuente?', 'font-size', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué propiedad CSS define el tamaño de la fuente?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué etiqueta HTML se usa para insertar una imagen?', '<img>', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué etiqueta HTML se usa para insertar una imagen?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué valor de display hace que un elemento ocupe toda la línea?', 'block', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué valor de display hace que un elemento ocupe toda la línea?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué etiqueta HTML define una lista desordenada?', '<ul>', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué etiqueta HTML define una lista desordenada?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué propiedad CSS se usa para añadir espacio interno a un elemento?', 'padding', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué propiedad CSS se usa para añadir espacio interno a un elemento?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué etiqueta HTML define el encabezado principal de una página?', '<h1>', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué etiqueta HTML define el encabezado principal de una página?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando inicia un repositorio de Git?', 'git init', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando inicia un repositorio de Git?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando añade archivos al área de staging?', 'git add', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando añade archivos al área de staging?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando guarda los cambios en el repositorio?', 'git commit', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando guarda los cambios en el repositorio?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando descarga los cambios de un repositorio remoto?', 'git pull', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando descarga los cambios de un repositorio remoto?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando sube los cambios a un repositorio remoto?', 'git push', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando sube los cambios a un repositorio remoto?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando crea una nueva rama?', 'git branch', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando crea una nueva rama?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando cambia de rama?', 'git checkout', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando cambia de rama?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando fusiona dos ramas?', 'git merge', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando fusiona dos ramas?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué comando muestra el historial de commits?', 'git log', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué comando muestra el historial de commits?');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id)
SELECT 'ABIERTA', '¿Qué archivo indica qué archivos debe ignorar Git?', '.gitignore', 5
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué archivo indica qué archivos debe ignorar Git?');

INSERT INTO pregunta (tipo_pregunta, enunciado, es_verdadero, tematica_id)
SELECT 'V_F', 'Java es un lenguaje de programación orientado a objetos.', true, 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = 'Java es un lenguaje de programación orientado a objetos.');

INSERT INTO pregunta (tipo_pregunta, enunciado, es_verdadero, tematica_id)
SELECT 'V_F', 'Spring Boot es un framework de JavaScript.', false, 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = 'Spring Boot es un framework de JavaScript.');

INSERT INTO pregunta (tipo_pregunta, enunciado, es_verdadero, tematica_id)
SELECT 'V_F', 'SQL significa Structured Query Language.', true, 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = 'SQL significa Structured Query Language.');

INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opcion_correcta, tematica_id)
SELECT 'UNICA', '¿Cuál de los siguientes NO es un tipo de dato primitivo en Java?', 'int|String|boolean|double', 'String', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuál de los siguientes NO es un tipo de dato primitivo en Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opcion_correcta, tematica_id)
SELECT 'UNICA', '¿Cuál es la anotación principal de Spring Boot?', '@SpringBootApplication|@SpringBoot|@EnableAutoConfiguration|@Configuration', '@SpringBootApplication', 2
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuál es la anotación principal de Spring Boot?');

INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opcion_correcta, tematica_id)
SELECT 'UNICA', '¿Qué etiqueta HTML se usa para crear una tabla?', '<table>|<tr>|<td>|<th>', '<table>', 4
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Qué etiqueta HTML se usa para crear una tabla?');

INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opciones_correctas, tematica_id)
SELECT 'MULTIPLE', '¿Cuáles son modificadores de acceso en Java?', 'public|private|protected|static', 'public,private,protected', 1
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuáles son modificadores de acceso en Java?');

INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opciones_correctas, tematica_id)
SELECT 'MULTIPLE', '¿Cuáles son tipos de JOIN en SQL?', 'INNER JOIN|LEFT JOIN|RIGHT JOIN|FULL JOIN', 'INNER JOIN,LEFT JOIN,RIGHT JOIN,FULL JOIN', 3
WHERE NOT EXISTS (SELECT 1 FROM pregunta WHERE enunciado = '¿Cuáles son tipos de JOIN en SQL?');