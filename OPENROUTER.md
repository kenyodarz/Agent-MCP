# Configuración de OpenRouter para el Agente MCP

## Variables de entorno necesarias

```bash
# API Key de OpenRouter (obtener en https://openrouter.ai/keys)
export OPENROUTER_API_KEY=sk-or-v1-xxxxx

# Modelo a usar (opcional, por defecto: google/gemini-2.0-flash-exp)
export OPENROUTER_MODEL=google/gemini-2.0-flash-exp

# URL del servidor MCP (opcional, por defecto: http://localhost:8080)
export MCP_SERVER_URL=http://localhost:8080
```

## Modelos disponibles en OpenRouter

### Google Gemini
- `google/gemini-2.0-flash-exp` - Gemini 2.0 Flash (recomendado, rápido y económico)
- `google/gemini-pro-1.5` - Gemini 1.5 Pro
- `google/gemini-flash-1.5` - Gemini 1.5 Flash

### OpenAI
- `openai/gpt-4o` - GPT-4 Optimized
- `openai/gpt-4o-mini` - GPT-4 Mini (económico)
- `openai/gpt-4-turbo` - GPT-4 Turbo

### Anthropic
- `anthropic/claude-3.5-sonnet` - Claude 3.5 Sonnet
- `anthropic/claude-3-opus` - Claude 3 Opus

### Otros
- `meta-llama/llama-3.1-70b-instruct` - Llama 3.1 70B
- `mistralai/mistral-large` - Mistral Large

Ver lista completa: https://openrouter.ai/models

## Ventajas de OpenRouter

1. **Una sola API Key**: Acceso a todos los modelos con una sola key
2. **Fallback automático**: Si un modelo falla, OpenRouter puede usar otro
3. **Precios competitivos**: Mejores precios que llamar directamente a los proveedores
4. **Sin gestión de múltiples keys**: No necesitas API keys de Google, OpenAI, Anthropic, etc.
5. **Cambio de modelo en caliente**: Solo cambia la variable de entorno `OPENROUTER_MODEL`

## Ejemplo de uso

```bash
# Usar Gemini 2.0 Flash
export OPENROUTER_MODEL=google/gemini-2.0-flash-exp

# Cambiar a GPT-4o Mini
export OPENROUTER_MODEL=openai/gpt-4o-mini

# Cambiar a Claude 3.5 Sonnet
export OPENROUTER_MODEL=anthropic/claude-3.5-sonnet
```

## Prueba del agente

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -d "Hola, ¿qué modelo estás usando?"
```
