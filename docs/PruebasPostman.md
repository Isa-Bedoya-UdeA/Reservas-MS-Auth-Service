# Pruebas en Postman

## Registro de Usuarios

### Descripción

El sistema de registro permite crear nuevos usuarios tanto como clientes como proveedores. Los clientes pueden reservar servicios, mientras que los proveedores pueden ofrecer servicios. Ambos tipos de usuarios deben verificar su email antes de poder iniciar sesión.

**Características importantes:**
- **Validación de datos:** Se valida el formato de email, complejidad de contraseña, y otros campos obligatorios.
- **Email único:** No se permite registrar dos usuarios con el mismo email.
- **Categorías para proveedores:** Los proveedores deben asociarse a una categoría activa del servicio de catálogo.
- **Verificación de email obligatoria:** Después del registro, se envía un email de verificación que debe confirmarse antes de iniciar sesión.

### Endpoints de Registro

## 1. Registrar Cliente Exitoso

**Nombre:** Register Client - Success
**URL:** `http://localhost:8081/api/auth/register/client`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "cliente@email.com",
    "password": "Password123",
    "nombre": "Juan Pérez",
    "telefono": "3211234567"
}
```
**Código esperado:** 201 Created
**Response esperado:**
```json
{
    "message": "Usuario registrado exitosamente. Por favor verifica tu email.",
    "verificationToken": "token-de-verificacion"
}
```

## 2. Registrar Cliente con Datos Inválidos

**Nombre:** Register Client - Invalid Data
**URL:** `http://localhost:8081/api/auth/register/client`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "email-invalido",
    "password": "123",
    "nombre": "  ",
    "telefono": "abc"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
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

## 3. Registrar Cliente con Email Ya Existente

**Nombre:** Register Client - Email Already Exists
**URL:** `http://localhost:8081/api/auth/register/client`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "cliente@email.com",
    "password": "Password123",
    "nombre": "Juan Pérez",
    "telefono": "3211234567"
}
```
**Código esperado:** 409 Conflict
**Response esperado:**
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "El email ya está registrado"
}
```

## 4. Registrar Proveedor Exitoso

**Nombre:** Register Provider - Success
**URL:** `http://localhost:8081/api/auth/register/provider`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "0203c0b2-be07-44c0-8d12-bf9c362d10aa"
}
```
**Código esperado:** 201 Created
**Response esperado:**
```json
{
    "message": "Usuario registrado exitosamente. Por favor verifica tu email.",
    "verificationToken": "token-de-verificacion"
}
```

## 5. Registrar Proveedor con UUID Inválido

**Nombre:** Register Provider - Invalid UUID
**URL:** `http://localhost:8081/api/auth/register/provider`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "0203c0b2-be07"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "status": 400,
    "error": "Invalid Data Format",
    "message": "El ID de categoría debe ser un UUID válido (formato: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)"
}
```

## 6. Registrar Proveedor con Categoría No Existente

**Nombre:** Register Provider - Category Not Found
**URL:** `http://localhost:8081/api/auth/register/provider`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "0203c0b2-be07-44c0-8d12-bf9c362d10aa"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "status": 400,
    "error": "Invalid Category",
    "message": "La categoría con ID '0203c0b2-be07-44c0-8d12-bf9c362d10aa' no existe en el servicio de catálogo"
}
```

## 7. Registrar Proveedor con Categoría Inactiva

**Nombre:** Register Provider - Category Inactive
**URL:** `http://localhost:8081/api/auth/register/provider`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "contacto@esteticabelleza.com",
    "password": "Password123",
    "nombreComercial": "Estética Belleza",
    "direccion": "Centro",
    "telefonoContacto": "3211234567",
    "idCategoria": "uuid-de-categoria-inactiva"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
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

## 8. Verificar Email - Token Válido (Éxito)

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

## 9. Verificar Email - Token Inválido (Error)

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

## 10. Verificar Email - Token Vacío (Validación)

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

## 11. Verificar Email - Token Vencido (Error)

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

## 12. Reenviar Token - Email No Verificado (Éxito)

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

## 13. Reenviar Token - Email Ya Verificado (Info)

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

## 14. Reenviar Token - Email No Existe (Error)

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

## 15. Reenviar Token - Email Vacío (Validación)

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

## 16. Verificar Email - Token Ya Usado (Error)

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

## Autenticación y Login

### Descripción

El sistema de autenticación implementa login con JWT (JSON Web Tokens), refresh tokens para mantener sesiones activas, y medidas de seguridad como límite de intentos fallidos y bloqueo temporal de cuentas.

**Requisitos previos:**
- El usuario debe haberse registrado previamente
- El email del usuario debe estar verificado
- El usuario debe tener una cuenta activa (no bloqueada)

### Endpoints de Autenticación

## 17. Login Exitoso como Cliente

**Nombre:** Login - Successful Client
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "carlos@email.com",
    "password": "Password123"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "CLIENTE",
    "userId": "uuid-del-usuario",
    "email": "carlos@email.com"
}
```

---

## 18. Login Exitoso como Proveedor

**Nombre:** Login - Successful Provider
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "salon@bellavida.com",
    "password": "Password123"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "PROVEEDOR",
    "userId": "uuid-del-usuario",
    "email": "salon@bellavida.com"
}
```

---

## 19. Login Fallido por Contraseña Incorrecta

**Nombre:** Login - Wrong Password
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "carlos@email.com",
    "password": "WrongPass"
}
```
**Código esperado:** 401 Unauthorized
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Correo o contraseña incorrectos",
    "path": "/api/auth/login"
}
```

---

## 20. Login con Email No Verificado

**Nombre:** Login - Email Not Verified
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "usuario-no-verificado@email.com",
    "password": "Password123"
}
```
**Código esperado:** 403 Forbidden
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Debes verificar tu email antes de iniciar sesión",
    "path": "/api/auth/login"
}
```

---

## 21. Login con Usuario No Existente

**Nombre:** Login - User Not Found
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "noexiste@email.com",
    "password": "Password123"
}
```
**Código esperado:** 404 Not Found
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Usuario con email 'noexiste@email.com' no encontrado",
    "path": "/api/auth/login"
}
```

---

## 22. Login con Datos Vacíos (Validación)

**Nombre:** Login - Empty Credentials
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "",
    "password": ""
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/login",
    "validationErrors": {
        "email": "El email es requerido",
        "password": "La contraseña es requerida"
    }
}
```

---

## 23. Login con Email Inválido (Validación)

**Nombre:** Login - Invalid Email Format
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "email-invalido",
    "password": "Password123"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/login",
    "validationErrors": {
        "email": "El email debe tener un formato válido"
    }
}
```

---

## 24. Login con Cuenta Bloqueada

**Nombre:** Login - Account Locked
**URL:** `http://localhost:8081/api/auth/login`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "carlos@email.com",
    "password": "Password123"
}
```
**Código esperado:** 423 Locked
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 423,
    "error": "Locked",
    "message": "Demasiados intentos fallidos. Tu cuenta está bloqueada por 24 horas. Intenta de nuevo después de 2026-04-17 a las 12:00",
    "path": "/api/auth/login"
}
```
**Nota:** Esta prueba debe ejecutarse DESPUÉS de 5 intentos fallidos consecutivos con la misma cuenta.

---

## 25. Refresh Token Exitoso

**Nombre:** Refresh Token - Success
**URL:** `http://localhost:8081/api/auth/refresh`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "CLIENTE",
    "userId": "uuid-del-usuario",
    "email": "carlos@email.com"
}
```
**Nota:** El refresh token debe obtenerse de una respuesta de login exitosa. El nuevo refresh token es una rotación del anterior.

---

## 26. Refresh Token Inválido

**Nombre:** Refresh Token - Invalid
**URL:** `http://localhost:8081/api/auth/refresh`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "refreshToken": "token-invalido"
}
```
**Código esperado:** 401 Unauthorized
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Refresh token inválido o expirado",
    "path": "/api/auth/refresh"
}
```

---

## 27. Refresh Token Vacío (Validación)

**Nombre:** Refresh Token - Empty
**URL:** `http://localhost:8081/api/auth/refresh`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "refreshToken": ""
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/refresh",
    "validationErrors": {
        "refreshToken": "El refresh token es requerido"
    }
}
```

---

## 28. Logout Exitoso

**Nombre:** Logout - Success
**URL:** `http://localhost:8081/api/auth/logout`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
**Código esperado:** 200 OK
**Response esperado:** (vacío)
**Nota:** El refresh token debe obtenerse de una respuesta de login exitosa. Después del logout, el token no puede usarse para renovar access tokens.

---

## 29. Logout con Token Inválido

**Nombre:** Logout - Invalid Token
**URL:** `http://localhost:8081/api/auth/logout`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "refreshToken": "token-invalido"
}
```
**Código esperado:** 401 Unauthorized
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Refresh token no encontrado",
    "path": "/api/auth/logout"
}
```

---

## 30. Logout con Token Vacío (Validación)

**Nombre:** Logout - Empty Token
**URL:** `http://localhost:8081/api/auth/logout`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "refreshToken": ""
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T12:00:00",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/logout",
    "validationErrors": {
        "refreshToken": "El refresh token es requerido"
    }
}
```

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
