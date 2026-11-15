package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.controller.REST.AuthenticationREST;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.security.CustomUserDetailsService;
import com.malb.gimnasio.security.JwtService;
import com.malb.gimnasio.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.malb.gimnasio.controller.DTO.request.CrearLoginDTO;
import com.malb.gimnasio.controller.DTO.response.LoginDTO;
import com.malb.gimnasio.controller.DTO.response.UsuarioLoginDTO;
import com.malb.gimnasio.modelo.usuario.Rol;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.BadCredentialsException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationREST.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    private static final String URL = "/auth/registro";

    private static final String json = """
    {
        "nombre": "Maria",
        "apellido": "Perez",
        "mail": "maria@gmail.com",
        "password": "micontra123",
        "telefono": "1123456789",
        "dni": 12345678
    }
    """;
    private static final String URL_LOGIN = "/auth/login";

    private static final String JSON_LOGIN = """
{
    "email": "ana@gmail.com",
    "password": "password123"
}
""";
    @Test
    void registroConDatosValidosDevuelve201() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        verify(authenticationService).registrar(any(RegistroClienteDTO.class));
    }

    @Test
    void registroConMailInvalidoDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "maria@gmail.com", "mail-invalido"
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConContraseñaCortaDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "micontra123", "abc123"
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConTelefonoCortoDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "1123456789", "12345"
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConNombreVacioDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "\"nombre\": \"Maria\"",
                "\"nombre\": \"\""
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConApellidoVacioDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "\"apellido\": \"Perez\"",
                "\"apellido\": \" \""
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConMailExistenteDevuelve409() throws Exception {
        doThrow(new RecursoDuplicadoException("Ya existe un usuario registrado con ese mail"))
                .when(authenticationService).registrar(any(RegistroClienteDTO.class));

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());

        verify(authenticationService).registrar(any(RegistroClienteDTO.class));
    }

    @Test
    void loginConDatosValidosDevuelve200() throws Exception {

        LoginDTO respuesta = new LoginDTO(
                "token-de-prueba",
                "Bearer",
                7200L,
                new UsuarioLoginDTO(
                        1L,
                        "Ana Perez",
                        "ana@gmail.com",
                        Rol.CLIENTE
                )
        );

        when(authenticationService.login(any(CrearLoginDTO.class)))
                .thenReturn(respuesta);

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-de-prueba"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.vencimiento").value(7200))
                .andExpect(jsonPath("$.usuario.id").value(1))
                .andExpect(jsonPath("$.usuario.nombre").value("Ana Perez"))
                .andExpect(jsonPath("$.usuario.email").value("ana@gmail.com"))
                .andExpect(jsonPath("$.usuario.rol").value("CLIENTE"));

        verify(authenticationService).login(any(CrearLoginDTO.class));
    }

    @Test
    void loginConEmailInvalidoDevuelve400() throws Exception {

        String json = JSON_LOGIN.replace(
                "ana@gmail.com",
                "email-invalido"
        );

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void loginConEmailVacioDevuelve400() throws Exception {

        String json = JSON_LOGIN.replace(
                "\"email\": \"ana@gmail.com\"",
                "\"email\": \"\""
        );

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void loginConPasswordVaciaDevuelve400() throws Exception {

        String json = JSON_LOGIN.replace(
                "\"password\": \"password123\"",
                "\"password\": \"\""
        );

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void loginConCredencialesIncorrectasDevuelve401() throws Exception {

        when(authenticationService.login(any(CrearLoginDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        mockMvc.perform(post(URL_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_LOGIN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));

        verify(authenticationService).login(any(CrearLoginDTO.class));
    }

}