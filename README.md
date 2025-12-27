# Proyecto de Franquicia - Prueba Técnica

Este proyecto es una implementación de una API REST para gestionar franquicias, sucursales y productos,
Este proyecto corresponde a una API REST reactiva implementada en **Java 17**, construida con **Spring WebFlux** y **MongoDB**, y basada en la arquitectura limpia hexagonal definida en el scaffold-clean-architecture de Bancolombia. La solución permite administrar franquicias, sucursales y productos, además de realizar consultas para identificar el producto con mayor disponibilidad de stock por cada sucursal.

---

## 📖 Descripción del Proyecto
Este proyecto consiste en una **API REST** diseñada para la administración de franquicias, permitiendo la gestión eficiente de sucursales y productos.
Una **franquicia** contiene:
- Un nombre
- Una lista de **sucursales**, donde cada sucursal tiene:
    - Un nombre
    - Una lista de **productos**, cada uno con:
        - Un nombre
        - Una cantidad de stock

---

## Tecnologías utilizadas

- **Spring Boot** (Java 17)
- **Arquitectura Hexagonal**
- **MongoDB** (base de datos NoSQL)
- **WebFlux** (programación reactiva)
- **Docker** (para despliegue local)
- **Terraform** (para despliegue en la nube)

---

## Arquitectura de Persistencia y Decisiones de Diseño

### Estrategia de Relación: Franchise ↔ Branch (1:N)
Patrón utilizado: Parent Referencing (Referencia al Padre).

En lugar de embeber la lista de Sucursales (branches) dentro del documento de la Franquicia (Patrón Embedding),
se optó por separar ambas entidades en colecciones distintas, donde la Sucursal mantiene una referencia a su Franquicia (franchiseId).

### Estrategia de Inventario: Branch ↔ Product
Patrón utilizado: Extended Reference Pattern (Patrón de Referencia Extendida).

Para la gestión del stock de productos dentro de una sucursal, se implementó una estrategia híbrida. Aunque existe un catálogo global de productos (ProductEntity),
cada sucursal almacena una copia parcial de los datos relevantes del producto (ID, Nombre y Stock Local) dentro de su propio documento.

### Manejo de Consistencia de Datos (Trade-offs)

Debido a la desnormalización introducida por el Extended Reference Pattern,
se asume un modelo de Consistencia Eventual para las actualizaciones.

---

## 🧰 Requisitos

Antes de desplegar la aplicación, asegúrate de tener instalados los siguientes componentes:

| Herramienta           | Descripción                                                                  |
|-----------------------|------------------------------------------------------------------------------|
| ☕ **Java 17**         | Versión recomendada para ejecutar el backend con Spring Boot.                |
| 📦 **Gradle**         | Usado para compilar, construir y gestionar las dependencias del proyecto.    |
| 📫 **Postman**        | Ideal para probar manualmente los endpoints REST de la API.                  |
| 🐳 **Docker Desktop** | Permite levantar MongoDB y/o el backend en contenedores de forma rápida.     |

- **Descargas sugeridas**

- **Cliente de pruebas de API**
    - Postman: https://www.postman.com/downloads/
    - Insomnia: https://insomnia.rest/download

- **Docker Compose**  
  Descarga por la página oficial: https://docs.docker.com/compose/install/
-
> 🔧 **Recomendación:** Verifica que cada herramienta esté instalada correctamente antes de continuar.

---

## Clonar el repositorio

Clona este repositorio en tu m�quina local:

    ```bash
    git clone:
      - https://github.com/santiagomurillo29/franchise_test.git

Luego accede a la carpeta del proyecto con:
- **cd franchise_test**

---

## Despliegue local
Este proyecto utiliza Docker para facilitar el despliegue en un entorno local sin necesidad de instalar Java o Mongo en tu máquina.

### Construcción y ejecución de los contenedores

1. Ubícate en la raíz del proyecto (donde se encuentra el archivo docker-compose.yml) y ejecuta:

   ```bash
      - docker compose up --build

Esto iniciará los siguientes servicios:
- API (Spring Boot): Disponible en http://localhost:8080
- MongoDB: Base de datos.
- Mongo Express: Interfaz gráfica para ver los datos en http://localhost:8081

Ejemplo de cuando el servidor está levantado:
![](https://github.com/santiagomurillo29/imagenes/blob/main/Screenshot%202025-05-02%20015827.png)


Una vez que los contenedores están corriendo, puedes probar los endpoints de la API usando Postman o Insomnia apuntando a localhost:8080.

### Detener la aplicación
Cuando finalices tus pruebas, puedes detener todos los contenedores ejecutando:

    ```bash
    - docker compose down

---

## Endpoints

A continuación se muestran los endpoints expuestos en la API:

### 1. Crear una nueva franquicia
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/franchises`
-
- **Request Body**:
  ```json
  {
      "name": "Nombre de la franquisia"
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/createFranchise.png)

**Response**

```json
{
  "id": "694edfbdb88b6321db0ba976",
  "name": "Franchise",
  "branches": []
}

```

### 2. Crear una nueva sucursal para una franquicia
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/franchises/{idFranchise}/branches`
- **Cuerpo**:
  ```json
  {
      "name": "Nombre de la sucursal"
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/createBranch.png)

**Response**

```json
{
  "id": "694edfebb88b6321db0ba977",
  "name": "branch",
  "products": []
}
```

### 3. Agregar un producto global a la franquicia
Este endpoint es nuevo, el cual ayuda a tener productos globales a una franquicia especifica antes de agregarlos a una sucursal primero.
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/franchises/{idFranchise}/products`
- **Cuerpo**:
  ```json
   {
      "name": "Nombre de producto",
      "stock": 50,
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/createProduct.png)

**Response**

```json
{
  "id": "694ee033b88b6321db0ba978",
  "name": "products",
  "stock": 100
}
```



### 4. Agregar un producto a una sucursal
Una vez asociados los productos a la franquicia, se puede añadir los productos a la sucursal.
- **M�todo**: `POST`
- **URL**: `http://localhost:8080/api/branches/{idBranch}/products`
- **Cuerpo**:
  ```json
  {
      "productId": "idProduct",
      "stock": 25,
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/AddProductToBranch.png)

**Response**

```json
{
  "id": "694edfebb88b6321db0ba977",
  "name": "branch",
  "products": [
    {
      "id": "694ee033b88b6321db0ba978",
      "name": "products",
      "stock": 30
    }
  ]
}
```

### 5. Eliminar un producto de una sucursal
- **Método**: `DELETE`
- **URL**: `http://localhost:8080/api/branches/{idBranch}/products/{idProduct}`

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/Delete.png)

**Response**

**No Content**



### 6. Modificar el stock de un producto
Modifica el stock del producto que se encuentra en la franquicia.
- **Método**: `PATCH`
- **URL**: `http://localhost:8080/api/products/{idProduct}/stock`
- **Cuerpo**:
  ```json
  {
      "stock": 50
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/UpdateStock.png)

**Response**

```json
{
  "id": "694ee033b88b6321db0ba978",
  "name": "products",
  "stock": 80
}
```



### 7. Mostrar el producto con más stock en una sucursal de una franquicia
Muestra el producto con mayor stock de las sucursales que están asociadas a la franquicia.
- **Método**: `GET`
- **URL**: `http://localhost:8080/api/franchises/{idFranchise}/largest-stock`

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/GetLargestStock.png)

**Response**

```json
[
  {
    "branchId": "694edfebb88b6321db0ba977",
    "branchName": "branch",
    "productId": "694ee033b88b6321db0ba978",
    "productName": "products",
    "stock": 30
  }
]
```



### 8. Mostrar franquicia
Este endpoint en nuevo el cual muestra la una franquicia por by id.
- **Método**: `GET`
- **URL**: `http://localhost:8080/api/franchise/{idFranchise}`

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/GetFranchise.png)

**Response**

```json
{
  "id": "694edfbdb88b6321db0ba976",
  "name": "Franchise",
  "branches": [
    {
      "id": "694edfebb88b6321db0ba977",
      "name": "branch",
      "products": [
        {
          "id": "694ee033b88b6321db0ba978",
          "name": "products",
          "stock": 30
        }
      ]
    }
  ]
}
```



### 9. Mostrar sucursales
Este endpoint en nuevo el cual muestra la una sucursal por by id.
- **Método**: `GET`
- **URL**: `http://localhost:8080/api/branches/{idBranch}`

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/GetBranch.png)

**Response**

```json
{
  "id": "694edfebb88b6321db0ba977",
  "name": "branch",
  "products": [
    {
      "id": "694ee033b88b6321db0ba978",
      "name": "products",
      "stock": 30
    }
  ]
}
```


### 10. Mostrar productos
Este endpoint en nuevo el cual muestra todos los productos que están asociados a una franquicia.
- **Método**: `GET`
- **URL**: `http://localhost:8080/api/franchises/{idFranchise}/products`

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/GetProducts.png)

**Response**

```json
[
  {
    "id": "694ee033b88b6321db0ba978",
    "name": "products",
    "stock": 80
  }
]
```


### 11. Actualizar el nombre de la franquicia
- **Método**: `PATCH`
- **URL**: `http://localhost:8080/api/franchises/{idFranchise}/name`
- **Cuerpo**:
  ```json
  {
      "name": "Nuevo nombre de franquicia"
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/UpdateNameFranchise.png)

**Response**

```json
{
  "id": "694edfbdb88b6321db0ba976",
  "name": "franchise update"
}
```



### 12. Actualizar el nombre de la sucursal
- **Método**: `PATCH`
- **URL**: `http://localhost:8080/api/branches/{idBranch}/name`
- **Cuerpo**:
  ```json
  {
      "name": "Nuevo nombre de sucursal"
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/GetBranch.png)

**Response**

```json
{
  "id": "694edfebb88b6321db0ba977",
  "name": "branch update"
}
```



### 12. Actualizar el nombre de un producto
- **Método**: `PATCH`
- **URL**: `http://localhost:8080/api/products/{idProduct}/name`
- **Cuerpo**:
  ```json
  {
      "name": "Nuevo nombre de sucursal"
  }

*Ejemplo*
![](https://github.com/santiagomurillo29/imagenes_franchise/blob/main/UpdateNameProduct.png)

**Response**

```json
{
  "id": "694ee033b88b6321db0ba978",
  "name": "products update"
}
```

---

# Despliegue en la nube

## Guía de Despliegue en AWS (ECS + ECR + API Gateway) con Terraform

Esta guía describe, paso a paso, cómo desplegar la aplicación Franchise en la nube de AWS usando Terraform como Infrastructure as Code (IaC).

### Servicios utilizados en Terraform:

- **Amazon ECR (Elastic Container Registry)**:  
  Se utilizó para almacenar la imagen Docker de la aplicación de forma segura.


- **Amazon ECS (Elastic Container Service)** con **Fargate**:  
  Se creó un *Task Definition* en el que se definieron dos contenedores:
    - Uno para la API (imagen desde ECR)
    - Otro para MongoDB


- **Amazon ECS Cluster y Service**:  
  Se desplegaron los contenedores dentro de un *cluster ECS*, y se configuró un *service* para mantener la disponibilidad de la API.


- **Application Load Balancer (ALB)**:  
  Se utilizó un **Load Balancer** para distribuir el tráfico entrante hacia el contenedor de la API.  
  También se definió una ruta de *health check* (`/`) para monitorear la disponibilidad de la API desde el balanceador.


- **Amazon API Gateway**:  
  Finalmente, se expuso públicamente la API a través de una URL usando **API Gateway**, el cual redirige las solicitudes al Load Balancer.

---

### Requisitos previos

- **Terraform**
  1.7. Probado con versiones ≥ 1.7.

- **AWS CLI**:
  Configurado con un perfil con permisos para ECR, ECS, ALB, IAM y API Gateway (**Configuración de las credenciales**).

- **Cuenta AWS**:
  Con región principal us‑east‑1.

Una vez construida la imagen, puedes ubicarte en la carpeta \terraform\envs\dev.

---

## Paso 1: Configuración de credenciales sensibles

Edite variables.tf y asigne sus credenciales de acceso, access_key y secret_key con los cuales tiene los permisos para trabajar con aws.
(Tener las credenciales de acceso en un archivo no es recomendable, pero en este caso de prueba lo asignamos ahí).

## Paso 2: Crear el Repositorio ECR (Fase 1)

Antes de desplegar la aplicación, necesitamos que el repositorio ECR exista para poder subir nuestra imagen de Docker. 
Usaremos Terraform para crear solo este recurso inicialmente.

Ubícate en la carpeta terraform/envs/dev y ejecuta:

terraform init
terraform apply -target=module.ecr -auto-approve

Cuando termine, verás un output verde llamado ecr_repository_url. Copia esa URL, la necesitarás para el siguiente paso. 
(Ejemplo de URL: 123456789.dkr.ecr.us-east-1.amazonaws.com/nombre-repo)

---

## Paso 3: Construcción y Publicación de la Imagen Docker

Regresa a la raíz del proyecto (donde se encuentra tu Dockerfile) y ejecuta los siguientes comandos reemplazando los valores con la URL que obtuviste en el paso anterior.

1. Autenticarse en ECR: (Nota: Reemplaza <TU_URL_ECR> por la base de la URL, sin el nombre del repo, o usa el comando completo abajo).  

    ```bash
    **Comando para login (Mac/Linux):**
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <PEGA_AQUI_EL_INICIO_DE_TU_URL_ECR>

2. Construir la imagen:

    ```bash
    docker build -t franchise .


3. Etiquetar la imagen: Reemplaza <URL_COMPLETA_DEL_REPO> con el output del Paso 2:
    ```bash
    Docker tag franchise:latest <URL_COMPLETA_DEL_REPO>:latest

4. Subir la imagen:
    ```bash
    docker push <URL_COMPLETA_DEL_REPO>:latest

## Paso 4: Despliegue de la Aplicación (Fase 2)

Ahora que la imagen ya existe en AWS, podemos desplegar el resto de la infraestructura (ECS, Load Balancer, API Gateway, etc.) sin errores.

Vuelve a la carpeta terraform/envs/dev y ejecuta:

    ```bash
    terraform apply -auto-approve

Terraform detectará que el ECR ya existe (no lo volverá a crear) y procederá a crear todo lo demás.

## Resultado final

Al terminar, se mostrará un output agw_url similar a: https://abcde12345.execute-api.us-east-1.amazonaws.com/

Importante:

- Espera 2-3 minutos para que el Load Balancer y los servicios de AWS se estabilicen.
- Prueba la URL en tu navegador. Deberías ver el mensaje de confirmación "ok" o la respuesta de tu API.
- Esta URL reemplaza a tu localhost:8080.


### Limpieza de recursos
Si deseas eliminar toda la infraestructura creada para evitar costos:

    ```bash
      terraform destroy -auto-approve

## ⚠️ Notas Importantes y Demo
- Demo en vivo: Existe un despliegue de referencia disponible para pruebas en: https://qxuvk20sld.execute-api.us-east-1.amazonaws.com
- Modo de uso: La API mantiene los mismos endpoints que el entorno local. Simplemente reemplace http://localhost:8080 por la URL de arriba en su cliente HTTP (Postman/Insomnia).
- Horario de Disponibilidad: Por motivos de optimización de costos en la nube, este entorno de prueba solo se encuentra activo de 08:00 AM a 06:00 PM (GMT-5 / Hora Colombia). Fuera de este horario, el servicio no responderá.