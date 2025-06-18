# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**


**Creación de tablas en PostgreSQL:**

POST /api/v1/orders → Crear pedido

PUT /api/v1/orders/{id}/items → Modificar ítems del pedido

GET /api/v1/orders/{id} → Obtener pedido

CREATE TABLE IF NOT EXISTS public.customers
(
id integer NOT NULL DEFAULT nextval('customers_id_seq'::regclass),
name character varying(255) COLLATE pg_catalog."default" NOT NULL,
email character varying(255) COLLATE pg_catalog."default" NOT NULL,
created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT customers_pkey PRIMARY KEY (id),
CONSTRAINT customers_email_key UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.customers
OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.distribution_centers
(
id integer NOT NULL DEFAULT nextval('distribution_centers_id_seq'::regclass),
name character varying(255) COLLATE pg_catalog."default" NOT NULL,
location text COLLATE pg_catalog."default" NOT NULL,
CONSTRAINT distribution_centers_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.distribution_centers
OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.inventory
(
id integer NOT NULL DEFAULT nextval('inventory_id_seq'::regclass),
product_id integer NOT NULL,
distribution_center_id integer NOT NULL,
stock integer NOT NULL DEFAULT 0,
CONSTRAINT inventory_pkey PRIMARY KEY (id),
CONSTRAINT inventory_product_id_distribution_center_id_key UNIQUE (product_id, distribution_center_id),
CONSTRAINT inventory_distribution_center_id_fkey FOREIGN KEY (distribution_center_id)
REFERENCES public.distribution_centers (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION,
CONSTRAINT inventory_product_id_fkey FOREIGN KEY (product_id)
REFERENCES public.products (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.inventory
OWNER to postgres;
-- Index: idx_inventory_product_center

-- DROP INDEX IF EXISTS public.idx_inventory_product_center;

CREATE INDEX IF NOT EXISTS idx_inventory_product_center
ON public.inventory USING btree
(product_id ASC NULLS LAST, distribution_center_id ASC NULLS LAST)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.order_items
(
id integer NOT NULL DEFAULT nextval('order_items_id_seq'::regclass),
order_id integer NOT NULL,
product_id integer NOT NULL,
quantity integer NOT NULL,
price numeric(10,2) NOT NULL,
distribution_center_id integer NOT NULL,
CONSTRAINT order_items_pkey PRIMARY KEY (id),
CONSTRAINT order_items_order_id_product_id_key UNIQUE (order_id, product_id),
CONSTRAINT order_items_distribution_center_id_fkey FOREIGN KEY (distribution_center_id)
REFERENCES public.distribution_centers (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION,
CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id)
REFERENCES public.orders (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE CASCADE,
CONSTRAINT order_items_product_id_fkey FOREIGN KEY (product_id)
REFERENCES public.products (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.order_items
OWNER to postgres;
-- Index: idx_order_items_order

-- DROP INDEX IF EXISTS public.idx_order_items_order;

CREATE INDEX IF NOT EXISTS idx_order_items_order
ON public.order_items USING btree
(order_id ASC NULLS LAST)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.orders
(
id integer NOT NULL DEFAULT nextval('orders_id_seq'::regclass),
customer_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
customer_email character varying(255) COLLATE pg_catalog."default" NOT NULL,
status character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT 'PENDING'::character varying,
total numeric(12,2),
created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
customer_id integer,
CONSTRAINT orders_pkey PRIMARY KEY (id),
CONSTRAINT orders_customer_id_fkey FOREIGN KEY (customer_id)
REFERENCES public.customers (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.orders
OWNER to postgres;
-- Index: idx_orders_email

-- DROP INDEX IF EXISTS public.idx_orders_email;

CREATE INDEX IF NOT EXISTS idx_orders_email
ON public.orders USING btree
(customer_email COLLATE pg_catalog."default" ASC NULLS LAST)
TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.payments
(
id integer NOT NULL DEFAULT nextval('payments_id_seq'::regclass),
order_id integer NOT NULL,
payment_method character varying(50) COLLATE pg_catalog."default" NOT NULL,
payment_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
amount numeric(12,2) NOT NULL,
status character varying(50) COLLATE pg_catalog."default" DEFAULT 'COMPLETED'::character varying,
CONSTRAINT payments_pkey PRIMARY KEY (id),
CONSTRAINT payments_order_id_fkey FOREIGN KEY (order_id)
REFERENCES public.orders (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.payments
OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.products
(
id integer NOT NULL DEFAULT nextval('products_id_seq'::regclass),
vendor_id integer NOT NULL,
name character varying(255) COLLATE pg_catalog."default" NOT NULL,
description text COLLATE pg_catalog."default",
price numeric(10,2) NOT NULL,
created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT products_pkey PRIMARY KEY (id),
CONSTRAINT products_vendor_id_fkey FOREIGN KEY (vendor_id)
REFERENCES public.vendors (id) MATCH SIMPLE
ON UPDATE NO ACTION
ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.products
OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.vendors
(
id integer NOT NULL DEFAULT nextval('vendors_id_seq'::regclass),
name character varying(255) COLLATE pg_catalog."default" NOT NULL,
email character varying(255) COLLATE pg_catalog."default" NOT NULL,
created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT vendors_pkey PRIMARY KEY (id),
CONSTRAINT vendors_email_key UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.vendors
OWNER to postgres;

# **r2dbc**
PostgreSQL

# **Gradle**
Correr el proyecto:
./gradlew bootRun

-Seguridad JWT API con AzureAD Tenant:

-ClientId: 965239c6-1310-4488-93b7-92cff18b70c4
-Scope: api://965239c6-1310-4488-93b7-92cff18b70c4

-Cada peticion a las API debe ser con JWT del Directorio Activo Azure, Bearer Token Authorization.

# _**Docker Kafka **_

imagen con docker-compose.yml:
-
 [docker-compose.yml](https://drive.google.com/file/d/13NtGsA78bhLQ0j0zDHSGs7065NpFR23z/view?usp=sharing)

# **Run:** 

docker-compose up -d

![Kafka producer](https://drive.google.com/file/d/13BaXkt9UN1xThDrRoheFw-3zIE5x6LxX/view?usp=sharing)

![Kafka consumer](https://drive.google.com/file/d/1FrNMVQ595oGWLtmYDhqD8BOmssC1LXyc/view?usp=sharing)




