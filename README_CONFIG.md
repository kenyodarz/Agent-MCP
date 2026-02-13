# Guía de Configuración del Agente MCP

## Opción 1: LM Studio (Local - Recomendado para desarrollo)

### Instalación
1. Descarga LM Studio: https://lmstudio.ai/
2. Descarga un modelo recomendado:
   - **Phi-3-mini-4k-instruct** (2GB, rápido)
   - **Llama-3.2-3B-Instruct** (3GB, bueno)
   - **Mistral-7B-Instruct** (7GB, muy bueno)

### Configuración
1. Abre LM Studio
2. Ve a la pestaña "Local Server"
3. Carga el modelo descargado
4. Click en "Start Server" (puerto 1234 por defecto)

### Variables de entorno (opcional)
```bash
# Por defecto usa http://localhost:1234/v1
export LM_STUDIO_URL=http://localhost:1234/v1
export LM_STUDIO_MODEL=local-model
```

### Prueba
```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: text/plain" \
  -d "Hola, ¿qué herramientas tienes del servidor MCP?"
```

---

## Opción 2: OpenRouter (Cloud - Múltiples modelos)

### Configuración
```bash
export LM_STUDIO_URL=https://openrouter.ai/api/v1
export LM_STUDIO_API_KEY=sk-or-v1-xxxxx  # Tu API key de OpenRouter
export LM_STUDIO_MODEL=google/gemini-2.0-flash-exp
```

### Modelos disponibles
- `google/gemini-2.0-flash-exp` - Gemini 2.0 Flash
- `openai/gpt-4o-mini` - GPT-4 Mini
- `anthropic/claude-3.5-sonnet` - Claude 3.5
- Ver más: https://openrouter.ai/models

---

## Opción 3: Ollama (Local - Alternativa a LM Studio)

### Instalación
```bash
# Instala Ollama: https://ollama.ai/
ollama pull llama3.2:3b
ollama serve  # Puerto 11434 por defecto
```

### Configuración
```bash
export LM_STUDIO_URL=http://localhost:11434/v1
export LM_STUDIO_MODEL=llama3.2:3b
```

---

## Servidor MCP

El agente se conecta al servidor MCP en:
```bash
export MCP_SERVER_URL=http://localhost:8080
```

Asegúrate de que el servidor MCP esté corriendo antes de iniciar el agente.

---

## Logs

Los logs están configurados en nivel DEBUG para:
- `co.com.bancolombia` - Código del agente
- `org.springframework.ai` - Spring AI
- `org.springframework.ai.mcp` - Cliente MCP
- `org.springframework.ai.chat` - ChatClient

Verás en los logs:
- Conexión al servidor MCP
- Herramientas registradas
- Mensajes enviados y recibidos
- Llamadas al modelo de IA
