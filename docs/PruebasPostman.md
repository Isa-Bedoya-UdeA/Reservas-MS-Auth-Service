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
    "password": "[CONTRASEÑA_VÁLIDA]",
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
    "password": "[CONTRASEÑA_INVÁLIDA]",
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
    "password": "[CONTRASEÑA_VÁLIDA]",
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
    "password": "[CONTRASEÑA_VÁLIDA]",
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
    "password": "[CONTRASEÑA_VÁLIDA]",
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
    "password": "[CONTRASEÑA_VÁLIDA]",
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
    "password": "[CONTRASEÑA_VÁLIDA]",
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
    "token": "[TOKEN_DE_VERIFICACIÓN]"
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
    "token": "[TOKEN_INVÁLIDO]"
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
    "token": "[TOKEN_VENCIDO]"
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
    "token": "[TOKEN_DE_VERIFICACIÓN]"
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
    "password": "[CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "accessToken": "[ACCESS_TOKEN]",
    "refreshToken": "[REFRESH_TOKEN]",
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
    "password": "[CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "accessToken": "[ACCESS_TOKEN]",
    "refreshToken": "[REFRESH_TOKEN]",
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
    "password": "[CONTRASEÑA_INCORRECTA]"
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
    "password": "[CONTRASEÑA_VÁLIDA]"
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
    "password": "[CONTRASEÑA_VÁLIDA]"
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
    "password": "[CONTRASEÑA_VÁLIDA]"
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
    "password": "[CONTRASEÑA_VÁLIDA]"
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
    "refreshToken": "[REFRESH_TOKEN]"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "accessToken": "[ACCESS_TOKEN]",
    "refreshToken": "[REFRESH_TOKEN]",
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
    "refreshToken": "[TOKEN_INVÁLIDO]"
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
    "refreshToken": "[REFRESH_TOKEN]"
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
    "refreshToken": "[TOKEN_INVÁLIDO]"
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
EMAIL_USERNAME=[TU_EMAIL]
EMAIL_PASSWORD=[TU_CONTRASEÑA_DE_APLICACIÓN]
FRONTEND_URL=http://localhost:3000
```

**Para Gmail:**
1. Habilita la autenticación de dos pasos en tu cuenta de Google
2. Genera una contraseña de aplicación en: https://myaccount.google.com/apppasswords
3. Usa esa contraseña como `EMAIL_PASSWORD`

---

## Gestión de Contraseñas

### Descripción

El sistema de gestión de contraseñas incluye dos funcionalidades principales:

1. **Olvidé mi contraseña (Password Reset)** - Permite a los usuarios recuperar su cuenta cuando olvidan su contraseña, sin necesidad de autenticación.
2. **Cambiar contraseña (Change Password)** - Permite a los usuarios autenticados cambiar su contraseña actual por una nueva.

**Características importantes:**
- **Validación de formato:** Las contraseñas deben cumplir con requisitos de complejidad (8 caracteres mínimos, mayúscula, minúscula, número).
- **Email de confirmación:** Se envían emails de confirmación para detectar cambios no autorizados.
- **Revocación de sesiones:** Al cambiar la contraseña, se revocan todos los refresh tokens del usuario.
- **Tokens de un solo uso:** Los tokens de reset expiran en 24 horas y solo pueden usarse una vez.

### Endpoints de Gestión de Contraseñas

## 31. Solicitar Reset de Contraseña - Email Existe (Éxito)

**Nombre:** Password Reset Request - Email Exists
**URL:** `http://localhost:8081/api/auth/password-reset/request`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "carlos@email.com"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "message": "Si el email existe en nuestro sistema, recibirás un enlace para restablecer tu contraseña",
    "success": true,
    "timestamp": "2026-04-16T16:30:00Z"
}
```
**Nota:** Esta prueba enviará un email real a carlos@email.com con el token de reset. El token se puede obtener de la base de datos o del email.

---

## 32. Solicitar Reset de Contraseña - Email No Existe (Info)

**Nombre:** Password Reset Request - Email Not Found
**URL:** `http://localhost:8081/api/auth/password-reset/request`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "noexiste@email.com"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "message": "Si el email existe en nuestro sistema, recibirás un enlace para restablecer tu contraseña",
    "success": true,
    "timestamp": "2026-04-16T16:30:00Z"
}
```
**Nota:** Por seguridad, el sistema siempre retorna 200 OK para no revelar si un email existe o no.

---

## 33. Solicitar Reset de Contraseña - Email Vacío (Validación)

**Nombre:** Password Reset Request - Empty Email
**URL:** `http://localhost:8081/api/auth/password-reset/request`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": ""
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:30:00Z",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/password-reset/request",
    "validationErrors": {
        "email": "El email es requerido"
    }
}
```

---

## 34. Solicitar Reset de Contraseña - Email Inválido (Validación)

**Nombre:** Password Reset Request - Invalid Email
**URL:** `http://localhost:8081/api/auth/password-reset/request`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "email-invalido"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:30:00Z",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/password-reset/request",
    "validationErrors": {
        "email": "El email debe tener un formato válido"
    }
}
```

---

## 35. Confirmar Reset de Contraseña - Token Válido (Éxito)

**Nombre:** Password Reset Confirm - Valid Token
**URL:** `http://localhost:8081/api/auth/password-reset/confirm`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "[TOKEN_DE_RESET]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "message": "Contraseña restablecida exitosamente",
    "success": true,
    "timestamp": "2026-04-16T16:35:00Z"
}
```
**Nota:** El token debe obtenerse de una respuesta de solicitud de reset exitosa (prueba #31) o de la base de datos.

---

## 36. Confirmar Reset de Contraseña - Contraseña Inválida (Validación)

**Nombre:** Password Reset Confirm - Invalid Password
**URL:** `http://localhost:8081/api/auth/password-reset/confirm`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "[TOKEN_DE_RESET]",
    "newPassword": "[CONTRASEÑA_INVÁLIDA]"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:35:00Z",
    "status": 400,
    "error": "Invalid Password",
    "message": "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número",
    "path": "/api/auth/password-reset/confirm"
}
```

---

## 37. Confirmar Reset de Contraseña - Token No Existe (Error)

**Nombre:** Password Reset Confirm - Token Not Found
**URL:** `http://localhost:8081/api/auth/password-reset/confirm`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "[TOKEN_INVÁLIDO]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 410 Gone
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:35:00Z",
    "status": 410,
    "error": "Invalid Token",
    "message": "Token de restablecimiento inválido o expirado",
    "path": "/api/auth/password-reset/confirm"
}
```

---

## 38. Confirmar Reset de Contraseña - Token Expirado (Error)

**Nombre:** Password Reset Confirm - Expired Token
**URL:** `http://localhost:8081/api/auth/password-reset/confirm`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "[TOKEN_DE_RESET]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 410 Gone
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:35:00Z",
    "status": 410,
    "error": "Invalid Token",
    "message": "Token de restablecimiento inválido o expirado",
    "path": "/api/auth/password-reset/confirm"
}
```
**Nota:** Esta prueba debe ejecutarse con un token que haya expirado (más de 24 horas después de su creación).

---

## 39. Confirmar Reset de Contraseña - Token Ya Usado (Error)

**Nombre:** Password Reset Confirm - Already Used Token
**URL:** `http://localhost:8081/api/auth/password-reset/confirm`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "token": "[TOKEN_DE_RESET]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 410 Gone
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:35:00Z",
    "status": 410,
    "error": "Invalid Token",
    "message": "Token de restablecimiento inválido o expirado",
    "path": "/api/auth/password-reset/confirm"
}
```
**Nota:** Esta prueba debe ejecutarse DESPUÉS de la prueba #35 (cuando el token ya fue usado).

---

## 40. Cambiar Contraseña - Exitoso (Autenticado)

**Nombre:** Change Password - Success
**URL:** `http://localhost:8081/api/auth/change-password`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "currentPassword": "[CONTRASEÑA_ACTUAL_VÁLIDA]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "message": "Contraseña cambiada exitosamente",
    "success": true,
    "timestamp": "2026-04-16T16:40:00Z"
}
```
**Nota:** El access token debe obtenerse de una respuesta de login exitosa (prueba #17 o #18).

---

## 41. Cambiar Contraseña - Contraseña Actual Incorrecta (Error)

**Nombre:** Change Password - Wrong Current Password
**URL:** `http://localhost:8081/api/auth/change-password`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "currentPassword": "[CONTRASEÑA_INCORRECTA]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 401 Unauthorized
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:40:00Z",
    "status": 401,
    "error": "Unauthorized",
    "message": "La contraseña actual es incorrecta",
    "path": "/api/auth/change-password"
}
```

---

## 42. Cambiar Contraseña - Nueva Contraseña Igual a Actual (Error)

**Nombre:** Change Password - Same Password
**URL:** `http://localhost:8081/api/auth/change-password`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "currentPassword": "[CONTRASEÑA_ACTUAL_VÁLIDA]",
    "newPassword": "[CONTRASEÑA_ACTUAL_VÁLIDA]"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:40:00Z",
    "status": 400,
    "error": "Same Password",
    "message": "La nueva contraseña debe ser diferente a la actual",
    "path": "/api/auth/change-password"
}
```

---

## 43. Cambiar Contraseña - Nueva Contraseña Inválida (Validación)

**Nombre:** Change Password - Invalid New Password
**URL:** `http://localhost:8081/api/auth/change-password`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "currentPassword": "[CONTRASEÑA_ACTUAL_VÁLIDA]",
    "newPassword": "[CONTRASEÑA_INVÁLIDA]"
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:40:00Z",
    "status": 400,
    "error": "Invalid Password",
    "message": "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número",
    "path": "/api/auth/change-password"
}
```

---

## 44. Cambiar Contraseña - Campos Vacíos (Validación)

**Nombre:** Change Password - Empty Fields
**URL:** `http://localhost:8081/api/auth/change-password`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "currentPassword": "",
    "newPassword": ""
}
```
**Código esperado:** 400 Bad Request
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:40:00Z",
    "status": 400,
    "error": "Validation Error",
    "message": "Errores de validación en los datos de entrada",
    "path": "/api/auth/change-password",
    "validationErrors": {
        "currentPassword": "La contraseña actual es requerida",
        "newPassword": "La nueva contraseña es requerida"
    }
}
```

---

## 45. Cambiar Contraseña - Sin Autenticación (Error)

**Nombre:** Change Password - No Authentication
**URL:** `http://localhost:8081/api/auth/change-password`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "currentPassword": "[CONTRASEÑA_ACTUAL_VÁLIDA]",
    "newPassword": "[NUEVA_CONTRASEÑA_VÁLIDA]"
}
```
**Código esperado:** 401 Unauthorized
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T16:40:00Z",
    "status": 401,
    "error": "Unauthorized",
    "message": "No estás autenticado",
    "path": "/api/auth/change-password"
}
```

---

## Gestión de Administradores

### Descripción

El sistema de gestión de administradores permite crear, consultar, actualizar, desactivar y reactivar cuentas de administradores. Los administradores tienen acceso completo al sistema y pueden gestionar todos los usuarios (clientes, proveedores y otros administradores).

**Características importantes:**
- **Rol exclusivo:** Solo usuarios con rol ADMIN pueden acceder a estos endpoints.
- **Seguimiento de creador:** Cada administrador tiene un campo `creadoPor` que indica quién lo creó.
- **Soft delete:** Los administradores se desactivan en lugar de eliminarse físicamente.
- **Código de empleado:** Los administradores pueden tener un código de empleado opcional.
- **Estado activo:** Los administradores tienen un estado `activo` que puede ser true o false.

### Endpoints de Gestión de Administradores

## 46. Inicializar Primer Administrador (Éxito)

**Nombre:** Initialize First Admin - Success
**URL:** `http://localhost:8081/api/auth/admins/initialize`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "admin@empresa.com",
    "password": "[CONTRASEÑA_VÁLIDA]",
    "nombreCompleto": "Admin Principal",
    "telefono": "3001234567",
    "codigoEmpleado": "ADM001"
}
```
**Código esperado:** 201 Created
**Response esperado:**
```json
{
    "idUsuario": "[UUID_DEL_ADMIN]",
    "email": "admin@empresa.com",
    "tipoUsuario": "ADMIN",
    "estado": "ACTIVO",
    "fechaRegistro": "2026-04-16T10:00:00",
    "nombreCompleto": "Admin Principal",
    "codigoEmpleado": "ADM001",
    "telefono": "3001234567",
    "fechaAsignacion": "2026-04-16T10:00:00",
    "activo": true,
    "creadoPor": null
}
```
**Nota:** Este endpoint es público y solo funciona cuando NO existen administradores en la base de datos. Una vez creado el primer admin, este endpoint ya no funcionará y deberás usar el endpoint regular de creación con autenticación.

---

## 47. Inicializar Primer Administrador - Ya Existen Admins (Error)

**Nombre:** Initialize First Admin - Admins Already Exist
**URL:** `http://localhost:8081/api/auth/admins/initialize`
**Método:** POST
**Headers:**
```
Content-Type: application/json
```
**Body:**
```json
{
    "email": "admin2@empresa.com",
    "password": "[CONTRASEÑA_VÁLIDA]",
    "nombreCompleto": "Admin Secundario",
    "telefono": "3007654321"
}
```
**Código esperado:** 409 Conflict
**Response esperado:**
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "Ya existen administradores en el sistema. Use el endpoint de creación regular."
}
```
**Nota:** Esta prueba debe ejecutarse DESPUÉS de la prueba #46 (cuando ya existe al menos un admin).

---

## 48. Crear Administrador Exitoso

**Nombre:** Create Admin - Success
**URL:** `http://localhost:8081/api/auth/admins?creadoPor=[UUID_ADMIN_CREADOR]`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "email": "admin2@empresa.com",
    "password": "[CONTRASEÑA_VÁLIDA]",
    "nombreCompleto": "Admin Secundario",
    "telefono": "3007654321",
    "codigoEmpleado": "ADM002"
}
```
**Código esperado:** 201 Created
**Response esperado:**
```json
{
    "idUsuario": "[UUID_DEL_ADMIN]",
    "email": "admin2@empresa.com",
    "tipoUsuario": "ADMIN",
    "estado": "ACTIVO",
    "fechaRegistro": "2026-04-16T11:00:00",
    "nombreCompleto": "Admin Secundario",
    "codigoEmpleado": "ADM002",
    "telefono": "3007654321",
    "fechaAsignacion": "2026-04-16T11:00:00",
    "activo": true,
    "creadoPor": "[UUID_ADMIN_CREADOR]"
}
```
**Nota:** El access token debe ser de un usuario con rol ADMIN. El parámetro `creadoPor` debe ser el UUID del admin que está creando el nuevo admin.

---

## 49. Crear Administrador con Email Ya Existente

**Nombre:** Create Admin - Email Already Exists
**URL:** `http://localhost:8081/api/auth/admins?creadoPor=[UUID_ADMIN_CREADOR]`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "email": "admin@empresa.com",
    "password": "[CONTRASEÑA_VÁLIDA]",
    "nombreCompleto": "Admin Principal",
    "telefono": "3001234567"
}
```
**Código esperado:** 409 Conflict
**Response esperado:**
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "El correo electrónico ya está en uso"
}
```

---

## 50. Obtener Todos los Administradores

**Nombre:** Get All Admins - Success
**URL:** `http://localhost:8081/api/auth/admins`
**Método:** GET
**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:** (vacío)
**Código esperado:** 200 OK
**Response esperado:**
```json
[
    {
        "idUsuario": "[UUID_ADMIN_1]",
        "email": "admin@empresa.com",
        "tipoUsuario": "ADMIN",
        "estado": "ACTIVO",
        "fechaRegistro": "2026-04-16T10:00:00",
        "nombreCompleto": "Admin Principal",
        "codigoEmpleado": "ADM001",
        "telefono": "3001234567",
        "fechaAsignacion": "2026-04-16T10:00:00",
        "activo": true,
        "creadoPor": null
    },
    {
        "idUsuario": "[UUID_ADMIN_2]",
        "email": "admin2@empresa.com",
        "tipoUsuario": "ADMIN",
        "estado": "ACTIVO",
        "fechaRegistro": "2026-04-16T11:00:00",
        "nombreCompleto": "Admin Secundario",
        "codigoEmpleado": "ADM002",
        "telefono": "3007654321",
        "fechaAsignacion": "2026-04-16T11:00:00",
        "activo": true,
        "creadoPor": "[UUID_ADMIN_CREADOR]"
    }
]
```
**Nota:** El access token debe ser de un usuario con rol ADMIN.

---

## 51. Obtener Administrador por ID

**Nombre:** Get Admin by ID - Success
**URL:** `http://localhost:8081/api/auth/admins/[UUID_ADMIN]`
**Método:** GET
**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:** (vacío)
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "idUsuario": "[UUID_ADMIN]",
    "email": "admin@empresa.com",
    "tipoUsuario": "ADMIN",
    "estado": "ACTIVO",
    "fechaRegistro": "2026-04-16T10:00:00",
    "nombreCompleto": "Admin Principal",
    "codigoEmpleado": "ADM001",
    "telefono": "3001234567",
    "fechaAsignacion": "2026-04-16T10:00:00",
    "activo": true,
    "creadoPor": null
}
```

---

## 52. Obtener Administrador por ID - No Encontrado

**Nombre:** Get Admin by ID - Not Found
**URL:** `http://localhost:8081/api/auth/admins/[UUID_INEXISTENTE]`
**Método:** GET
**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:** (vacío)
**Código esperado:** 404 Not Found
**Response esperado:**
```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Administrador con ID '[UUID_INEXISTENTE]' no encontrado"
}
```

---

## 53. Actualizar Administrador

**Nombre:** Update Admin - Success
**URL:** `http://localhost:8081/api/auth/admins/[UUID_ADMIN]`
**Método:** PUT
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "nombreCompleto": "Admin Principal Actualizado",
    "telefono": "3009998888",
    "codigoEmpleado": "ADM001-UPD",
    "activo": true
}
```
**Código esperado:** 200 OK
**Response esperado:**
```json
{
    "idUsuario": "[UUID_ADMIN]",
    "email": "admin@empresa.com",
    "tipoUsuario": "ADMIN",
    "estado": "ACTIVO",
    "fechaRegistro": "2026-04-16T10:00:00",
    "nombreCompleto": "Admin Principal Actualizado",
    "codigoEmpleado": "ADM001-UPD",
    "telefono": "3009998888",
    "fechaAsignacion": "2026-04-16T10:00:00",
    "activo": true,
    "creadoPor": null
}
```

---

## 54. Desactivar Administrador (Soft Delete)

**Nombre:** Deactivate Admin - Success
**URL:** `http://localhost:8081/api/auth/admins/[UUID_ADMIN]`
**Método:** DELETE
**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:** (vacío)
**Código esperado:** 204 No Content
**Response esperado:** (vacío)
**Nota:** El admin se marca como inactivo en la base de datos pero no se elimina físicamente.

---

## 55. Activar Administrador

**Nombre:** Activate Admin - Success
**URL:** `http://localhost:8081/api/auth/admins/[UUID_ADMIN]/activate`
**Método:** PATCH
**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:** (vacío)
**Código esperado:** 204 No Content
**Response esperado:** (vacío)
**Nota:** Reactiva un administrador que fue desactivado anteriormente.

---

## 56. Crear Administrador - Sin Rol ADMIN (Forbidden)

**Nombre:** Create Admin - Forbidden (No Admin Role)
**URL:** `http://localhost:8081/api/auth/admins?creadoPor=[UUID_ADMIN_CREADOR]`
**Método:** POST
**Headers:**
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
**Body:**
```json
{
    "email": "admin@empresa.com",
    "password": "[CONTRASEÑA_VÁLIDA]",
    "nombreCompleto": "Admin Principal",
    "telefono": "3001234567"
}
```
**Código esperado:** 403 Forbidden
**Response esperado:**
```json
{
    "timestamp": "2026-04-16T10:00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "No tienes permisos para acceder a este recurso",
    "path": "/api/auth/admins"
}
```
**Nota:** El access token debe ser de un usuario con rol diferente a ADMIN (CLIENTE o PROVEEDOR).

---

### Notas Importantes

- **Obtención de tokens de reset:** Los tokens de reset se pueden obtener de los emails enviados o directamente de la base de datos en la tabla `token_reset_password`.
- **Revocación de sesiones:** Después de cambiar la contraseña (pruebas #35, #40), todos los refresh tokens del usuario son revocados. Deberás hacer login nuevamente para obtener un nuevo access token.
- **Emails de confirmación:** Las pruebas exitosas de reset y cambio de contraseña envían emails de confirmación a los usuarios.
- **Formato de contraseña:** El sistema requiere que las contraseñas tengan al menos 8 caracteres, una mayúscula, una minúscula, y un número.
- **Permisos de administrador:** Todos los endpoints de administración requieren que el usuario tenga rol ADMIN. Los usuarios con roles CLIENTE o PROVEEDOR recibirán un error 403 Forbidden al intentar acceder a estos endpoints.
