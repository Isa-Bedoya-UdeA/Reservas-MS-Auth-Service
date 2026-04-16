# Pruebas en Postman

> Importante: El `Content-Type` de las peticiones debe ser `application/json`.

## Registrar Cliente Exitoso

```http
POST http://localhost:8081/api/auth/register/client
Content-Type: application/json

{
    "email": "cliente@email.com",
    "password": "Password123",
    "nombre": "Juan Pérez",
    "telefono": "3211234567"
}
```

**Respuesta esperada (201 Created):**
```json
{
    "message": "Usuario registrado exitosamente. Por favor verifica tu email.",
    "verificationToken": "token-de-verificacion"
}
```

## Registrar Cliente con Datos Inválidos

```http
POST http://localhost:8081/api/auth/register/client
Content-Type: application/json

{
    "email": "email-invalido",
    "password": "123",
    "nombre": "  ",
    "telefono": "abc"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "validationErrors": {
        "email": "El email debe tener un formato válido",
        "password": "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número",
        "nombre": "El nombre no puede estar vacío",
        "telefono": "El teléfono solo debe contener números"
    }
}
```

## Registrar Cliente con Email Ya Existente

```http
POST http://localhost:8081/api/auth/register/client
Content-Type: application/json

{
    "email": "cliente@email.com",
    "password": "Password123",
    "nombre": "Juan Pérez",
    "telefono": "3211234567"
}
```

**Respuesta esperada (409 Conflict):**
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "El email ya está registrado"
}
```

## Registrar Proveedor Exitoso

```http
POST http://localhost:8081/api/auth/register/provider
Content-Type: application/json

{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "0203c0b2-be07-44c0-8d12-bf9c362d10aa"
}
```

**Respuesta esperada (201 Created):**
```json
{
    "message": "Usuario registrado exitosamente. Por favor verifica tu email.",
    "verificationToken": "token-de-verificacion"
}
```

## Registrar Proveedor con UUID Inválido

```http
POST http://localhost:8081/api/auth/register/provider
Content-Type: application/json

{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "0203c0b2-be07"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
    "status": 400,
    "error": "Invalid Data Format",
    "message": "El ID de categoría debe ser un UUID válido (formato: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)"
}
```

## Registrar Proveedor con Categoría No Existente

```http
POST http://localhost:8081/api/auth/register/provider
Content-Type: application/json

{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "0203c0b2-be07-44c0-8d12-bf9c362d10aa"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
    "status": 400,
    "error": "Invalid Category",
    "message": "La categoría con ID '0203c0b2-be07-44c0-8d12-bf9c362d10aa' no existe en el servicio de catálogo"
}
```

## Registrar Proveedor con Categoría Inactiva

```http
POST http://localhost:8081/api/auth/register/provider
Content-Type: application/json

{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "uuid-de-categoria-inactiva"
}
```

**Respuesta esperada (400 Bad Request):**
```json
{
    "status": 400,
    "error": "Invalid Category",
    "message": "La categoría con ID 'uuid-de-categoria-inactiva' no está activa"
}
```

## Verificación de Email

### Descripción

El sistema de verificación de email asegura que los usuarios confirmen su dirección de correo electrónico antes de poder iniciar sesión. Esta funcionalidad es obligatoria tanto para clientes como para proveedores.

**Características importantes:**

- **No se puede iniciar sesión sin verificar el email:** El login fallará si el email del usuario no ha sido verificado.
- **Relación 1 a 1:** Cada usuario tiene un único token de verificación activo. Al generar un nuevo token, el anterior se elimina automáticamente.
- **Expiración de 24 horas:** Los tokens de verificación expiran después de 24 horas.
- **Reenvío de tokens:** Los usuarios pueden solicitar un nuevo token de verificación si el anterior expiró o no lo recibieron.
- **Email automático:** Al registrarse, se envía automáticamente un email de verificación al usuario.

### Endpoints de Verificación

## 1. Verificar Email - Token Válido (Éxito)

**Nombre:** Verify Email - Valid Token (Provider)
**URL:** `http://localhost:8081/api/auth/verify-email`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "f145de48-57d2-4236-95e3-7d2b4a0ace65"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "success": true,
    "message": "¡Felicidades! Tu email ha sido verificado y tu cuenta está activa.",
    "userId": "uuid-del-usuario",
    "email": "soccer@proveedor.com"
}
```

---

## 2. Verificar Email - Token Inválido (Error)

**Nombre:** Verify Email - Invalid Token
**URL:** `http://localhost:8081/api/auth/verify-email`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "00000000-0000-0000-0000-000000000000"
}
```
**Código esperado:** 410 Gone
**Response esperado:**
```json
{
    "message": "Token de verificación inválido o expirado"
}
```

---

## 3. Verificar Email - Token Vacío (Validación)

**Nombre:** Verify Email - Empty Token
**URL:** `http://localhost:8081/api/auth/verify-email`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": ""
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "message": "El token de verificación es requerido"
}
```

---

## 4. Verificar Email - Token Vencido (Error)

**Nombre:** Verify Email - Expired Token (Client)
**URL:** `http://localhost:8081/api/auth/verify-email`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "89dc79f3-6d79-475d-a417-b8784cc0cbe7"
}
```
**Código esperado:** 410 Gone
**Response esperado:**
```json
{
    "message": "Token de verificación inválido o expirado"
}
```

---

## 5. Reenviar Token - Email No Verificado (Éxito)

**Nombre:** Resend Verification - Unverified Email
**URL:** `http://localhost:8081/api/auth/resend-verification-email?email=juan.cliente@udea.edu.co`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:** (vacío)
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "success": true,
    "message": "Se ha reenviado un nuevo token de verificación a tu email. El enlace expira en 24 horas.",
    "userId": "uuid-del-usuario",
    "email": "juan.cliente@udea.edu.co"
}
```
**Nota:** Esta prueba enviará un email real a juan.cliente@udea.edu.co

---

## 6. Reenviar Token - Email Ya Verificado (Info)

**Nombre:** Resend Verification - Already Verified
**URL:** `http://localhost:8081/api/auth/resend-verification-email?email=soccer@proveedor.com`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:** (vacío)
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "success": false,
    "message": "Este email ya ha sido verificado. Puedes iniciar sesión directamente.",
    "userId": "uuid-del-usuario",
    "email": "soccer@proveedor.com"
}
```

---

## 7. Reenviar Token - Email No Existe (Error)

**Nombre:** Resend Verification - Email Not Found
**URL:** `http://localhost:8081/api/auth/resend-verification-email?email=noexiste@test.com`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:** (vacío)
**Código esperado:** 404 Not Found
**Response esperado:**
```json
{
    "message": "Usuario con email 'noexiste@test.com' no encontrado"
}
```

---

## 8. Reenviar Token - Email Vacío (Validación)

**Nombre:** Resend Verification - Missing Email
**URL:** `http://localhost:8081/api/auth/resend-verification-email`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:** (vacío)
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "message": "El email es requerido"
}
```

---

## 9. Verificar Email - Token Ya Usado (Error)

**Nombre:** Verify Email - Already Used Token
**URL:** `http://localhost:8081/api/auth/verify-email`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "f145de48-57d2-4236-95e3-7d2b4a0ace65"
}
```
**Código esperado:** 410 Gone
**Response esperado:**
```json
{
    "message": "Token de verificación inválido o expirado"
}
```
**Nota:** Esta prueba debe ejecutarse DESPUÉS de la prueba #1 (cuando el token ya fue usado)

---

### Configuración de Email

Para que el envío de emails funcione correctamente, debes configurar las siguientes variables en tu archivo `.env`:

```bash
# Email Configuration
EMAIL_USERNAME=tu-email@gmail.com
EMAIL_PASSWORD=tu-contraseña-de-aplicación
FRONTEND_URL=http://localhost:3000
```

**Para Gmail:**
1. Habilita la autenticación de dos pasos en tu cuenta de Google
2. Genera una contraseña de aplicación en: https://myaccount.google.com/apppasswords
3. Usa esa contraseña como `EMAIL_PASSWORD`
