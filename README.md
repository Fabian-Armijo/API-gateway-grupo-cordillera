# API Gateway - Ecosistema Cordillera

Este proyecto actúa como el **API Gateway** central para la arquitectura de microservicios. Construido con Spring Cloud Gateway, su función principal es ser el único punto de entrada (Entrypoint) para los clientes frontend (como React), enrutando el tráfico de manera segura hacia los microservicios internos y gestionando las políticas de CORS.

## Características Principales

*   **Punto de Entrada Único:** Oculta la complejidad de la red interna (puertos y direcciones de los microservicios) al exterior.
*   **Enrutamiento Dinámico:** Redirige las peticiones entrantes (ej. `/bff/**`, `/api/productos/**`) a sus respectivos microservicios.
*   **Gestión de CORS:** Configuración centralizada para permitir el tráfico seguro desde aplicaciones Frontend (ej. `localhost:3000` o `localhost:5173`).

## 📋 Requisitos Previos

Asegúrate de tener instalado lo siguiente en tu entorno de desarrollo:

*   [Java Development Kit (JDK) 17 o superior](https://adoptium.net/)
*   [Apache Maven](https://maven.apache.org/) (o puedes usar el Wrapper de Maven incluido `./mvnw`)
*   IDE recomendado: IntelliJ IDEA, VS Code o Eclipse.

## ⚙️ Configuración del Entorno

El API Gateway está configurado por defecto para ejecutarse en el puerto **8090**. 
No requiere conexión directa a ninguna base de datos, ya que su trabajo es puramente de red.

Asegúrate de que los microservicios destino (BFF, Productos, Categorías, Stock) estén ejecutándose en sus respectivos puertos antes de realizar pruebas de integración.

## Cómo Ejecutar el Proyecto

Tienes varias opciones para levantar el API Gateway en tu máquina local:

### Opción 1: Usando tu IDE (Recomendado para desarrollo)
1. Abre el proyecto en tu IDE (IntelliJ IDEA o VS Code).
2. Localiza la clase principal `ApiGatewayApplication.java` (o el nombre que tenga tu clase con `@SpringBootApplication`).
3. Haz clic en el botón de **Run** (▶) o **Debug** (🐛).

### Opción 2: Usando la terminal con Maven
Abre una terminal en la raíz del proyecto (donde se encuentra el archivo `pom.xml`) y ejecuta:

```bash
mvn spring-boot:run
Si no tienes Maven instalado globalmente, usa ./mvnw spring-boot:run en Mac/Linux o mvnw.cmd spring-boot:run en Windows
```

Solución de Problemas Frecuentes
Error de CORS en el Frontend: Verifica que el puerto de tu aplicación React (ej. 3000 o 5173) esté correctamente listado en la sección globalcors del archivo application.yml.

Error 503 Service Unavailable: El API Gateway está funcionando, pero el microservicio al que intenta redirigir la petición está apagado o no responde.

Port already in use: El puerto 8090 ya está siendo ocupado por otro programa. Cierra el proceso conflictivo o cambia el puerto en el application.yml.
