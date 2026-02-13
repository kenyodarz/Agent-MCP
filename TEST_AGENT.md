# Test del Agente MCP

## Prerequisitos
1. El servidor MCP debe estar corriendo en `http://localhost:8080`
2. El agente debe estar corriendo en `http://localhost:8081`

## Prueba 1: Chat simple

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -d "Hola, ¿qué herramientas tienes disponibles?"
```

## Prueba 2: Usar herramientas del MCP

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -d "¿Puedes usar tus herramientas para ayudarme?"
```

## Verificar logs

Revisa los logs del agente para ver:
- Conexión exitosa al servidor MCP
- Obtención del token OAuth2 de Entra ID (si está configurado)
- Registro de las herramientas del MCP
- Ejecución de las herramientas

## Respuesta esperada

Texto plano con la respuesta del agente:
```
Hola! Tengo acceso a las siguientes herramientas del servidor MCP: ...
```
