# banquito-switch-reporting-service

## Nota de arquitectura objetivo

Este servicio integrara Notification Service por REST via Kong:

```http
POST http://localhost:8000/api/v1/notifications/requests
```

Tambien expone endpoints REST publicos de consulta que Kong Switch publica como entrada oficial. Los reportes y comprobantes deben ajustarse a la regla vigente: el monto no procesado despues del fondeo no se devuelve a la empresa.

## Responsabilidad

Servicio responsable del flujo Off-Us, la consolidacion final de lineas y el reporting final del lote. Consume lineas Off-Us, genera archivo de compensacion, observa resultados finales, publica `BatchLinesCompletedEvent`, consume `BillingCompletedEvent`, genera reporte de novedades, genera comprobante corporativo y registra ordenes de notificacion.

Expone endpoints REST de consulta para resultados ya generados, pensados para publicarse por Kong. No llama al Core Bancario, no acredita beneficiarios, no calcula comisiones oficiales, no calcula IVA, no envia correos SMTP y no implementa API Gateway.

## Ejecucion local

Desde la raiz del workspace:

```powershell
docker compose build reporting-service
docker compose up -d postgres rabbitmq batch-service routing-service on-us-settlement-service reporting-service billing-service
```

Compilar sin Maven global:

```powershell
docker run --rm -v maven_repo:/root/.m2 -v "${PWD}\banquito-switch-reporting-service:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 mvn -o -DskipTests compile
```

Puerto por defecto: `8085`.

Health:

```http
GET http://localhost:8085/actuator/health
```

## Variables de entorno

- `SERVER_PORT`
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USERNAME`, `RABBITMQ_PASSWORD`
- `RABBIT_EXCHANGE_BATCH`, `RABBIT_EXCHANGE_ROUTING`, `RABBIT_EXCHANGE_SETTLEMENT`, `RABBIT_EXCHANGE_CLEARING`, `RABBIT_EXCHANGE_BILLING`
- `RABBIT_QUEUE_REPORTING_PAYMENT_LINES_OBSERVER`
- `RABBIT_QUEUE_REPORTING_ON_US_COMPLETED`
- `RABBIT_QUEUE_REPORTING_LINE_REJECTED`
- `RABBIT_QUEUE_CLEARING_OFF_US`
- `RABBIT_QUEUE_CLEARING_INCLUDED`
- `RABBIT_QUEUE_BILLING_BATCH_COMPLETED`
- `RABBIT_QUEUE_REPORTING_BILLING_COMPLETED`
- `RABBIT_ROUTING_KEY_PAYMENT_LINE_REQUESTED`
- `RABBIT_ROUTING_KEY_ON_US_COMPLETED`
- `RABBIT_ROUTING_KEY_ROUTED_OFF_US`
- `RABBIT_ROUTING_KEY_OFF_US_INCLUDED`
- `RABBIT_ROUTING_KEY_LINE_REJECTED`
- `RABBIT_ROUTING_KEY_BATCH_LINES_COMPLETED`
- `RABBIT_ROUTING_KEY_BILLING_COMPLETED`
- `SWITCH_FILES_OUTPUT_DIRECTORY`
- `SWITCH_FILES_CLEARING_DIRECTORY`
- `SWITCH_FILES_REPORTS_DIRECTORY`
- `SWITCH_FILES_RECEIPTS_DIRECTORY`
- `NOTIFICATION_KONG_BASE_URL`
- `NOTIFICATION_KONG_REQUESTS_PATH`
- `NOTIFICATION_KONG_AUTH_TOKEN`
- `NOTIFICATION_KONG_CLIENT_TOKEN_ENABLED`
- `NOTIFICATION_KONG_CLIENT_ID`
- `NOTIFICATION_KONG_CLIENT_SECRET`
- `NOTIFICATION_KONG_REQUIRED_SCOPE`
- `NOTIFICATION_KONG_CLIENT_TOKEN_PATH`
- `NOTIFICATION_KONG_CLIENT_TOKEN_REFRESH_SKEW_SECONDS`
- `NOTIFICATION_KONG_CONNECT_TIMEOUT_MS`
- `NOTIFICATION_KONG_READ_TIMEOUT_MS`
- `NOTIFICATION_SWITCH_ORIGIN_SERVICE`
- `NOTIFICATION_SWITCH_EVENT_TYPE_ON_US`
- `NOTIFICATION_SWITCH_EVENT_TYPE_OFF_US`
- `NOTIFICATION_SWITCH_PRIORITY`
- `NOTIFICATION_SWITCH_CHANNEL_TYPE`
- `NOTIFICATION_SWITCH_SUBJECT`
- `GRPC_SERVER_PORT`

## Base de datos

Usa `SWITCH_REPORTES_DB`. Hibernate se mantiene en `spring.jpa.hibernate.ddl-auto=validate`; no genera ni actualiza tablas.

Scripts:

```text
src/main/resources/db/init/001_create_off_us_clearing_tables.sql
src/main/resources/db/init/002_create_line_consolidation_tables.sql
src/main/resources/db/init/003_create_final_reporting_tables.sql
```

Tablas propias:

- `"ARCHIVO_COMPENSACION"`
- `"LINEA_ARCHIVO_COMPENSACION"`
- `"OBSERVACION_LINEA_LOTE"`
- `"RESULTADO_PROCESAMIENTO_LINEA"`
- `"RESUMEN_PROCESAMIENTO_LOTE"`
- `"REPORTE_NOVEDADES"`
- `"LINEA_REPORTE_NOVEDADES"`
- `"COMPROBANTE_CORPORATIVO"`
- `"ORDEN_NOTIFICACION"`
- `"DOCUMENTO_GENERADO"`

## Eventos

Consume:

- `PaymentLineRequestedEvent` desde `switch.reporting.payment-lines-observer.queue`
- `OnUsSettlementCompletedEvent` desde `switch.reporting.on-us-completed.queue`
- `PaymentLineRejectedEvent` desde `switch.reporting.line-rejected.queue`
- `PaymentLineRoutedOffUsEvent` desde `switch.clearing.off-us.queue`
- `BillingCompletedEvent` desde `switch.reporting.billing-completed.queue`

Publica:

- `OffUsClearingIncludedEvent` con routing key `payment.line.off-us.included`
- `BatchLinesCompletedEvent` con routing key `batch.lines.completed`

No publica `BatchClosedEvent` en esta fase; el cierre queda persistido localmente y preparado para consulta futura por Gateway/gRPC.

## gRPC interno para Gateway

Expone un servidor gRPC interno en `grpc.server.port`:

```text
banquito.switchpagos.reporting.v1.ReportingGatewayService
```

Operaciones:

- `GetBatchSummary`
- `GetNoveltyReport`
- `GetCorporateReceipt`
- `GetClearingFile`

Las consultas usan solo la base de datos propia de reporting y los archivos generados bajo `switch.files.output-directory`.

## REST publico de consulta para futuro Kong

`ReportingRestController` expone rutas HTTP equivalentes al contrato publico para que Kong pueda enrutar directamente hacia `reporting-service`:

```http
GET http://localhost:8085/api/v1/batches/{batchId}/summary
GET http://localhost:8085/api/v1/batches/{batchId}/novelties/details
GET http://localhost:8085/api/v1/batches/{batchId}/reports/novelties
GET http://localhost:8085/api/v1/batches/{batchId}/receipts/corporate
GET http://localhost:8085/api/v1/batches/{batchId}/commission
GET http://localhost:8085/api/v1/batches/{batchId}/clearing-file
GET http://localhost:8085/api/v1/batches/{batchId}/notifications
```

Estas rutas son solo consultas de resultados ya generados. No ejecutan generacion manual de reportes, cierre de lote, envio manual de notificaciones ni etapas internas del flujo.

Las respuestas de novedades y archivo de compensacion se entregan como JSON con metadata y `content` del CSV generado. No se exponen rutas absolutas internas del contenedor; `filePath` conserva la ruta relativa persistida.

Errores controlados:

- `400`: `batchId` invalido.
- `404`: resumen, reporte, comprobante, archivo o lote no encontrado.
- `500`: error tecnico controlado, por ejemplo lectura fallida de archivo generado.

El gRPC interno puede conservarse para contratos internos/historicos mientras no se retire fisicamente el codigo, pero no participa en la exposicion publica vigente.

Kong local DB-less del Switch publica estas rutas en:

```text
http://localhost:8010
```

Upstream interno:

```text
http://banquito-switch-reporting-service:8085
```

## Documentos generados

Archivo de compensacion:

```text
/app/output/clearing/clearing_<shortBatchId>.csv
```

Reporte de novedades:

```text
/app/output/reports/novelties_<batchId>.csv
```

Comprobante corporativo:

```text
/app/output/receipts/corporate_receipt_<batchId>.json
```

El comprobante mantiene `remainingAmount` por compatibilidad y agrega la semantica vigente:

```json
{
  "remainingAmount": "100.00",
  "unprocessedAmount": "100.00",
  "unprocessedAmountReturned": false,
  "unprocessedAmountPolicy": "El monto no procesado no se libera ni se devuelve a la empresa."
}
```

No se generan campos vigentes de liberacion, reverso o devolucion. Las columnas `"AJUSTE_FONDEO"_"ESTADO"`, `released_amount` y `funding_release_core_transaction_id` quedan solo como legado transicional hasta una migracion SQL posterior.

Las tablas guardan rutas relativas como `reports/...` y `receipts/...` para no depender de rutas absolutas del sistema operativo.

## Ordenes de notificacion

Se registran en `"ORDEN_NOTIFICACION"` solo para lineas cobrables:

- `ACREDITADA_ON_US`
- `INCLUIDA_ARCHIVO_COMPENSACION`

Si la linea cobrable tiene correo, el servicio llama a Notification Service real por Kong:

```http
POST ${NOTIFICATION_KONG_BASE_URL:http://kong-gateway:8000}${NOTIFICATION_KONG_REQUESTS_PATH:/api/v1/notifications/requests}
```

Autenticacion vigente:

```properties
notification.kong.auth-token=${NOTIFICATION_KONG_AUTH_TOKEN:${CORE_KONG_AUTH_TOKEN:}}
notification.kong.client-token.enabled=${NOTIFICATION_KONG_CLIENT_TOKEN_ENABLED:${CORE_KONG_CLIENT_TOKEN_ENABLED:true}}
notification.kong.client-id=${NOTIFICATION_KONG_CLIENT_ID:${CORE_KONG_CLIENT_ID:switch-pagos-internos-service}}
notification.kong.client-secret=${NOTIFICATION_KONG_CLIENT_SECRET:${CORE_KONG_CLIENT_SECRET:}}
notification.kong.required-scope=${NOTIFICATION_KONG_REQUIRED_SCOPE:${CORE_KONG_REQUIRED_SCOPE:core.reserve.consume}}
notification.kong.client-token-path=${NOTIFICATION_KONG_CLIENT_TOKEN_PATH:${CORE_KONG_CLIENT_TOKEN_PATH:/api/v1/auth/client-token}}
```

Si `NOTIFICATION_KONG_AUTH_TOKEN` esta configurado, se usa como override manual. Si esta vacio y `NOTIFICATION_KONG_CLIENT_TOKEN_ENABLED=true`, el servicio obtiene `client-token`, lo cachea en memoria y lo renueva antes de expirar. Si Notification requiere otro cliente o scope, la orden queda `FALLIDA` sin romper el cierre del lote.

Estados locales:

- `GENERADA`: orden con correo aceptada por Notification Service.
- `OMITIDA`: orden cobrable sin correo; no se llama a Notification.
- `FALLIDA`: Notification rechazo, no respondio o hubo timeout/conectividad; el cierre final del lote continua.

Mensajes vigentes:

- On-Us: `Su pago fue acreditado en una cuenta Banco BanQuito.`
- Off-Us: `Su pago fue incluido en el archivo de compensacion interbancaria.`
- Lineas rechazadas o fallidas no generan notificacion de exito.

Payload enviado:

```json
{
  "correlationId": "uuid-linea",
  "eventType": "MASS_PAYMENT_ON_US_PROCESSED",
  "originService": "banquito-switch-reporting-service",
  "priority": "NORMAL",
  "channelType": "EMAIL",
  "recipient": "beneficiario@example.com",
  "recipientName": "Beneficiario",
  "templateCode": null,
  "subject": "Pago procesado Banco BanQuito",
  "body": "Su pago fue acreditado en una cuenta Banco BanQuito.",
  "payload": {
    "batchId": "uuid",
    "lineId": "uuid",
    "amount": "100.00",
    "currency": "USD",
    "finalStatus": "ACREDITADA_ON_US"
  },
  "evidenceDocumentUuid": null
}
```

La tabla actual no tiene columnas dedicadas para `notificationUuid` ni estado externo. Temporalmente se guarda trazabilidad minima en `"ESTADO"` y `"MENSAJE"`; una migracion SQL posterior debe agregar campos dedicados.

## Idempotencia

- `"REPORTE_NOVEDADES"."ID_LOTE"` es unico.
- `"COMPROBANTE_CORPORATIVO"."ID_LOTE"` es unico.
- `"ORDEN_NOTIFICACION"."ID_LINEA"` es unico.
- `"DOCUMENTO_GENERADO"` tiene unicidad por `"ID_LOTE"` y `document_type`.
- Si llega de nuevo `BillingCompletedEvent` para un lote ya reportado, el listener hace ack y no regenera archivos ni duplica registros.

## Prueba manual basica

1. Levantar el flujo completo:

```powershell
docker compose up -d postgres rabbitmq batch-service routing-service on-us-settlement-service reporting-service billing-service
```

2. Cargar un lote mixto:

```powershell
curl.exe -s -X POST "http://localhost:8081/api/v1/batches/upload" `
  -F "file=@docs/examples/files/valid_mixed_batch.csv;filename=valid_mixed_batch_reporting.csv" `
  -F "channel=WEB" `
  -F "receivedBy=local"
```

3. Verificar documentos y persistencia:

```powershell
docker exec banquito-switch-reporting-service ls -la /app/output/reports
docker exec banquito-switch-reporting-service ls -la /app/output/receipts
docker exec banquito-switch-postgres psql -U postgres -d SWITCH_REPORTES_DB -c 'select "ID_LOTE", "ESTADO", "RUTA_ARCHIVO" from "REPORTE_NOVEDADES" order by "FECHA_GENERACION" desc limit 5;'
docker exec banquito-switch-postgres psql -U postgres -d SWITCH_REPORTES_DB -c 'select "ID_LOTE", "RUTA_ARCHIVO", "ESTADO_COBRO", "MONTO_TOTAL_COBRADO" from "COMPROBANTE_CORPORATIVO" order by "FECHA_GENERACION" desc limit 5;'
docker exec banquito-switch-postgres psql -U postgres -d SWITCH_REPORTES_DB -c 'select "ESTADO", count(*) from "ORDEN_NOTIFICACION" group by "ESTADO";'
```

4. Consultar los endpoints REST directos de reporting con el `batchId` generado:

```powershell
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/summary"
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/novelties/details"
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/reports/novelties"
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/receipts/corporate"
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/commission"
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/clearing-file"
curl.exe -s "http://localhost:8085/api/v1/batches/{batchId}/notifications"
```

5. Para probar Notification real, levantar Kong/Notification del Core, configurar `NOTIFICATION_KONG_CLIENT_SECRET` o `CORE_KONG_CLIENT_SECRET` para token automatico, procesar un lote con correos en lineas cobrables y revisar:

```powershell
docker exec banquito-switch-postgres psql -U postgres -d SWITCH_REPORTES_DB -c 'select "ID_LINEA", "EMAIL_NOTIFICACION", "ESTADO_FINAL", "ESTADO", "MENSAJE" from "ORDEN_NOTIFICACION" order by "FECHA_CREACION" desc limit 10;'
```

## Decisiones tecnicas

- `BillingCompletedEvent` se consume por RabbitMQ; no se consulta la base de `billing-service`.
- El comprobante muestra `taxAmount` y `totalChargedAmount` solo si vienen en el evento de billing; Core REST actualmente puede no devolver desglose de IVA ni total cobrado.
- El JSON del comprobante se escribe sin dependencia adicional de serializacion.
- `remainingAmount` es monto no procesado/no devuelto. `releasedAmount`, `fundingAdjustmentStatus` y `fundingReleaseCoreTransactionId` son legacy y no se usan para la salida vigente.
- Notification Service se consume por REST via Kong desde el flujo de cierre; no se implementa SMTP ni Mailpit directo dentro del Switch.
- Los endpoints REST de reporting reutilizan `ReportingQueryService`, igual que el adapter gRPC interno, para evitar duplicar reglas de consulta.
- No hay reintentos automaticos ni DLQ para notificaciones en esta fase.
- El contrato gRPC interno conservado se define en `src/main/proto/reporting_gateway.proto`.
- No se implementan DLQ ni reintentos avanzados en esta fase.
