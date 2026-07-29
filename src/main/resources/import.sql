INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO roles (nombre) VALUES ('ROLE_USER');

INSERT INTO usuarios (username, password, email, enabled) VALUES ('admin', '$2a$10$zSogB7TH/NggiaQQ.2OHz.ehYkuDxkxeeBVZ4uJlzrsLb7ObK1a92', 'admin@ejemplo.com', true);
INSERT INTO usuarios (username, password, email, enabled) VALUES ('user', '$2a$10$2IUXb6X7PsBoB3GAMSdAHebi7S4IP4wYI0N1n5RuqWJraZvDx4GUq', 'user@ejemplo.com', true);

INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 1);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (1, 2);
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (2, 2);

INSERT INTO tematica (nombre) VALUES ('Java Básico');
INSERT INTO tematica (nombre) VALUES ('Spring Boot');
INSERT INTO tematica (nombre) VALUES ('Bases de Datos');
INSERT INTO tematica (nombre) VALUES ('HTML y CSS');
INSERT INTO tematica (nombre) VALUES ('Git y Control de Versiones');

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Cuál es la palabra clave para definir una clase en Java?', 'class', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué método es el punto de entrada de un programa Java?', 'main', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Cuál es el tipo de dato primitivo para un número entero de 32 bits?', 'int', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué palabra clave se usa para heredar de una clase en Java?', 'extends', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación se usa para indicar que un método sobrescribe uno de la clase padre?', '@Override', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Cuál es el valor por defecto de una variable booleana en Java?', 'false', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué interfaz se usa para ordenar una lista con Collections.sort()?', 'Comparable', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué palabra clave evita que una clase sea heredada?', 'final', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Cómo se declara una constante en Java?', 'final', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué colección no permite elementos duplicados?', 'Set', 1);

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación marca una clase como componente de Spring?', '@Component', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación se usa para inyectar dependencias en Spring?', '@Autowired', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación convierte una clase en un controlador REST?', '@RestController', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué dependencia de Spring Boot permite crear aplicaciones web?', 'spring-boot-starter-web', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación mapea una petición GET a un método?', '@GetMapping', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué archivo contiene la configuración de una aplicación Spring Boot?', 'application.properties', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué motor de plantillas usa Spring Boot por defecto?', 'Thymeleaf', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación define una clase como servicio en Spring?', '@Service', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación se usa para mapear una entidad JPA?', '@Entity', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué anotación indica el campo que es clave primaria?', '@Id', 2);

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué significa la sigla SQL?', 'Structured Query Language', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando se usa para seleccionar datos de una tabla?', 'SELECT', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué cláusula filtra registros en una consulta SQL?', 'WHERE', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué tipo de JOIN devuelve solo los registros coincidentes de ambas tablas?', 'INNER JOIN', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando se usa para insertar datos en una tabla?', 'INSERT', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando elimina una tabla completa de la base de datos?', 'DROP TABLE', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué función devuelve el número de filas de una consulta?', 'COUNT', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué cláusula se usa para agrupar filas en SQL?', 'GROUP BY', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué es una clave foránea?', 'Un campo que referencia la clave primaria de otra tabla', 3);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando modifica datos existentes en una tabla?', 'UPDATE', 3);

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué etiqueta HTML define un párrafo?', '<p>', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué atributo HTML se usa para enlazar una hoja de estilos?', 'href', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué propiedad CSS cambia el color del texto?', 'color', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué etiqueta HTML define un enlace?', '<a>', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué propiedad CSS define el tamaño de la fuente?', 'font-size', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué etiqueta HTML se usa para insertar una imagen?', '<img>', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué valor de display hace que un elemento ocupe toda la línea?', 'block', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué etiqueta HTML define una lista desordenada?', '<ul>', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué propiedad CSS se usa para añadir espacio interno a un elemento?', 'padding', 4);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué etiqueta HTML define el encabezado principal de una página?', '<h1>', 4);

INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando inicia un repositorio de Git?', 'git init', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando añade archivos al área de staging?', 'git add', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando guarda los cambios en el repositorio?', 'git commit', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando descarga los cambios de un repositorio remoto?', 'git pull', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando sube los cambios a un repositorio remoto?', 'git push', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando crea una nueva rama?', 'git branch', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando cambia de rama?', 'git checkout', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando fusiona dos ramas?', 'git merge', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué comando muestra el historial de commits?', 'git log', 5);
INSERT INTO pregunta (tipo_pregunta, enunciado, respuesta, tematica_id) VALUES ('ABIERTA', '¿Qué archivo indica qué archivos debe ignorar Git?', '.gitignore', 5);

-- Preguntas de verdadero/falso
INSERT INTO pregunta (tipo_pregunta, enunciado, es_verdadero, tematica_id) VALUES ('V_F', 'Java es un lenguaje de programación orientado a objetos.', true, 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, es_verdadero, tematica_id) VALUES ('V_F', 'Spring Boot es un framework de JavaScript.', false, 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, es_verdadero, tematica_id) VALUES ('V_F', 'SQL significa Structured Query Language.', true, 3);

-- Preguntas de selección única
INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opcion_correcta, tematica_id) VALUES ('UNICA', '¿Cuál de los siguientes NO es un tipo de dato primitivo en Java?', 'int|String|boolean|double', 'String', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opcion_correcta, tematica_id) VALUES ('UNICA', '¿Cuál es la anotación principal de Spring Boot?', '@SpringBootApplication|@SpringBoot|@EnableAutoConfiguration|@Configuration', '@SpringBootApplication', 2);
INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opcion_correcta, tematica_id) VALUES ('UNICA', '¿Qué etiqueta HTML se usa para crear una tabla?', '<table>|<tr>|<td>|<th>', '<table>', 4);

-- Preguntas de selección múltiple
INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opciones_correctas, tematica_id) VALUES ('MULTIPLE', '¿Cuáles son modificadores de acceso en Java?', 'public|private|protected|static', 'public,private,protected', 1);
INSERT INTO pregunta (tipo_pregunta, enunciado, opciones, opciones_correctas, tematica_id) VALUES ('MULTIPLE', '¿Cuáles son tipos de JOIN en SQL?', 'INNER JOIN|LEFT JOIN|RIGHT JOIN|FULL JOIN', 'INNER JOIN,LEFT JOIN,RIGHT JOIN,FULL JOIN', 3);