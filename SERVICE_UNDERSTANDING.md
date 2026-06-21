# Entendimiento del servicio

`banquito-switch-reporting-service` concentra la salida operativa del Switch: compensacion Off-Us, consolidacion de lineas, reporte de novedades, comprobante corporativo y ordenes de notificacion.

## Nota de migracion vigente

Las ordenes locales de notificacion deben evolucionar a integracion REST via Kong con Notification Service:

```http
POST /api/v1/notifications/requests
```

Esta integracion ya esta implementada de forma transicional: `FinalReportingServiceImpl` crea la orden local y, si la linea cobrable tiene correo, usa `NotificationClient` para solicitar Notification Service por Kong. No se usa SMTP ni Mailpit directamente desde el Switch.

El servicio ya expone consultas REST propias para Kong antes de retirar el gateway local. Ademas, novedades y comprobantes muestran `remainingAmount` como monto no procesado, no como monto liberado o devuelto.

Desde esta fase, el comprobante corporativo y el summary exponen la politica vigente mediante `unprocessedAmount`, `unprocessedAmountReturned=false` y `unprocessedAmountPolicy`. Los campos `fundingAdjustmentStatus`, `releasedAmount` y `fundingReleaseCoreTransactionId` se toleran como legacy si llegan desde eventos antiguos, pero no se presentan como devolucion real.

## Responsabilidad en lenguaje simple

Escucha lo que ocurre con cada linea del lote, espera a que billing confirme el cobro de comision y deja listos los documentos finales para consulta publica mediante API Gateway.

## Flujo interno principal

1. `PaymentLineRequestedObserverListener` observa lineas esperadas sin competir con routing.
2. `PaymentLineRoutedOffUsListener` consume lineas Off-Us y `OffUsClearingServiceImpl` las agrega al archivo de compensacion.
3. `OnUsSettlementCompletedListener` y `PaymentLineRejectedListener` registran resultados finales.
4. `LineConsolidationServiceImpl` actualiza `batch_processing_summary`.
5. Cuando todas las lineas tienen estado final, se publica `BatchLinesCompletedEvent` hacia billing.
6. `BillingCompletedListener` consume `BillingCompletedEvent`.
7. `FinalReportingServiceImpl` valida idempotencia por `batchId`.
8. Se generan `novelties_<batchId>.csv` y `corporate_receipt_<batchId>.json`.
9. Se registran `notification_order` para lineas cobrables.
10. Las ordenes con correo se envian a Notification Service por REST/Kong.
11. Se registra `generated_document` para trazabilidad futura de consulta/descarga.
12. `ReportingRestController` expone consultas HTTP publicas para futuro Kong.
13. `GrpcReportingGatewayService` sigue exponiendo consultas internas historicas, sin participar en la entrada publica vigente por Kong.

## Paquetes importantes

- `config`: declaracion de exchanges, queues, bindings y converters RabbitMQ.
- `client`: cliente REST hacia Notification Service por Kong.
- `dto.event`: eventos consumidos y publicados.
- `enums`: estados locales de consolidacion, documentos y notificaciones.
- `exception`: errores controlados de generacion de archivo de compensacion.
- `file`: escritura fisica de CSV/JSON.
- `listener`: entradas RabbitMQ.
- `mapper`: conversion manual del flujo Off-Us.
- `model`: entidades JPA propias.
- `repository`: repositorios Spring Data JPA propios.
- `service` y `service.impl`: interfaces e implementaciones de aplicacion.
- `controller`: endpoints REST publicos de consulta para futuro Kong.
- `grpc`: adapter gRPC interno para API Gateway.

## Clases principales

- `OffUsClearingServiceImpl`: genera archivo de compensacion Off-Us y publica resultado de inclusion.
- `LineConsolidationServiceImpl`: consolida estados finales y publica `BatchLinesCompletedEvent`.
- `FinalReportingServiceImpl`: genera reporte final, comprobante y ordenes de notificacion al recibir billing.
- `NotificationClient`: envia `POST /api/v1/notifications/requests` via Kong usando token Bearer automatico o override manual.
- `FinalReportingServiceImpl`: ignora campos legacy de liberacion/reverso al construir el comprobante vigente.
- `FinalReportingFileWriter`: escribe el CSV de novedades y el JSON de comprobante con la politica de monto no procesado/no devuelto.
- `BillingCompletedListener`: listener pequeno que delega el cierre final.
- `RabbitBatchLinesCompletedEventPublisher`: publica el cierre operativo de lineas hacia billing.
- `ReportingQueryServiceImpl`: consulta resumen, reporte, comprobante y archivo de compensacion desde datos locales.
- `ReportingRestController`: publica `GET /api/v1/batches/{batchId}/summary`, novedades, comprobante, clearing-file y ordenes de notificacion.
- `GlobalExceptionHandler`: traduce errores de consulta REST a respuestas JSON sin stack traces.
- `GrpcReportingGatewayService`: traduce el contrato gRPC interno hacia `ReportingQueryService`.

## Tablas propias

- `clearing_file`: archivo de compensacion por lote.
- `clearing_file_line`: lineas Off-Us incluidas o fallidas.
- `batch_line_observation`: snapshot de lineas esperadas, beneficiario, cuenta, routing y correo.
- `line_processing_result`: resultado final unico por `lineId`.
- `batch_processing_summary`: totales operativos del lote y bandera de publicacion a billing.
- `novelty_report`: cabecera del reporte de novedades.
- `novelty_report_line`: detalle linea a linea del reporte.
- `corporate_receipt`: snapshot del comprobante corporativo.
- `corporate_receipt`: conserva columnas legacy de liberacion solo por compatibilidad con el SQL actual; no representan flujo vigente.
- `notification_order`: ordenes de notificacion generadas, omitidas o fallidas. La tabla actual guarda respuesta minima en `status`/`message`; queda pendiente ampliar columnas para `notificationUuid`, estado externo y error.
- `generated_document`: indice de documentos listos para consulta futura.

No hay foreign keys hacia otros microservicios ni hacia el Core.

## Eventos propios

Consume:

- `PaymentLineRequestedEvent`
- `PaymentLineRoutedOffUsEvent`
- `OnUsSettlementCompletedEvent`
- `PaymentLineRejectedEvent`
- `BillingCompletedEvent`

Publica:

- `OffUsClearingIncludedEvent`
- `BatchLinesCompletedEvent`

`BatchClosedEvent` queda pendiente; esta fase persiste el cierre final sin publicar un evento adicional.

## Archivos propios

- `/app/output/clearing/clearing_<shortBatchId>.csv`
- `/app/output/reports/novelties_<batchId>.csv`
- `/app/output/receipts/corporate_receipt_<batchId>.json`

## Integraciones externas

Integra con:

- RabbitMQ para consumir/publicar eventos.
- PostgreSQL propio para trazabilidad.
- Volumen local de archivos para salida operativa.
- Notification Service por REST via Kong.
- gRPC interno `ReportingGatewayService`.
- Kong futuro por HTTP REST directo hacia `reporting-service`.

No integra con Core Bancario ni con camara externa. Tampoco envia SMTP real desde el Switch.

## Que NO hace

- No expone endpoints REST de ejecucion manual de negocio; solo consultas publicas de resultados ya generados.
- No llama al Core.
- No calcula comision oficial ni IVA.
- No registra contabilidad.
- No confirma acreditacion externa Off-Us.
- No envia correos por SMTP propio; delega notificaciones a Notification Service.
- No implementa API Gateway.

## Pendientes o limitaciones conocidas

- Agregar DLQ y reintentos avanzados.
- Confirmar cliente/scope definitivo de Notification si el cliente tecnico compartido no queda autorizado.
- Agregar columnas dedicadas para `notificationUuid`, estado externo y error de Notification.
- Configurar Kong para publicar las rutas REST de reporting y retirar el gateway propio cuando existan rutas equivalentes.
- Publicar `BatchClosedEvent` si se requiere para monitoreo u observabilidad.
