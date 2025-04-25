/*
package com.practica.practica;

import java.sql.Date;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import com.practica.practica.model.*;

public class BibliotecaService {
    private WebClient clienteWeb = WebClient.builder().baseUrl("http://localhost:8080/api/v1").build();

    public void postUsuario(String nombre, String matricula, Date fechaNacimiento, String correoElectronico) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setMatricula(matricula);
        usuario.setFecha_nacimiento(fechaNacimiento);
        usuario.setCorreo_electronico(correoElectronico);

        try {
                String referencia = clienteWeb.post()
                                .uri("/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(usuario), Usuario.class)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                                .bodyToMono(String.class)
                                                .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                                .then(Mono.empty()) // Permite continuar la ejecución
                                )
                                .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                                .bodyToMono(String.class)
                                                .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                                .then(Mono.empty()))
                                .toBodilessEntity() // Obtiene solo la respuesta HTTP sin cuerpo
                                .map(response -> {
                                        if (response.getHeaders().getLocation() != null) {
                                                return response.getHeaders().getLocation().toString();
                                        } else {
                                                throw new RuntimeException(
                                                                "No se recibió una URL en la cabecera Location");
                                        }
                                })
                                .block();// Bloquea para obtener el resultado sincrónicamente
                if (referencia != null) {
                        System.out.println(referencia);
                }
        } catch (RuntimeException e) {
                System.err.println("Error: " + e.getMessage());
        }
    }

    public void getUsuario(int idUsuario) {

        // Realizamos la petición GET y deserializamos la respuesta en
        // Empleado
        Usuario usuario = clienteWeb.get()
                        .uri("/usuarios/" + idUsuario)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                        .then(Mono.empty()) // Permite continuar la ejecución
                        )
                        .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                        .then(Mono.empty()))
                        .bodyToMono(Usuario.class)
                        .block(); // Usamos block() para obtener la respuesta de forma síncrona

        if (usuario != null) {
                String selfLink = usuario.get__links().getSelf().getHref();
                if (selfLink != null) {
                        System.out.println("El usuario con id: " + usuario.getId() + " y nombre: "
                                        + usuario.getNombre()
                                        + " se encuentra disponible en el enlace: " + selfLink);
                } else {
                        System.out.println("Enlace 'self' no encontrado.");
                }
        }

    }

    public void putUsuario(String nombre, String matricula, Date fechaNacimiento, String correoElectronico) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setMatricula(matricula);
        usuario.setFecha_nacimiento(fechaNacimiento);
        usuario.setCorreo_electronico(correoElectronico);

        clienteWeb.put()
                        .uri("/usuarios/{id}", Usuario.get(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(usuario), Usuario.class)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                        .then(Mono.empty()) // Permite continuar la ejecución
                        )
                        .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                        .then(Mono.empty()))
                        .toBodilessEntity() // Obtiene solo la respuesta HTTP sin cuerpo
                        .block(); // Bloquea hasta recibir la respuesta
    }
    
    public void deleteUsuario(int idUsuario) {
        clienteWeb.delete()
                        .uri("/usuarios/{id}", idUsuario)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                        .then(Mono.empty()) // Permite continuar la ejecución
                        )
                        .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                        .then(Mono.empty()))
                        .toBodilessEntity() // Obtiene solo la respuesta HTTP sin cuerpo
                        .block();// Bloquea para obtener el resultado sincrónicamente
    }

    public void getUsuarios(int page, int size) {
        Page<Usuario> usuarios = clienteWeb.get()
                        .uri("/usuarios?page={page}&size={size}", page, size)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                        .then(Mono.empty()) // Permite continuar la ejecución
                        )
                        .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                        .bodyToMono(String.class)
                                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                        .then(Mono.empty()))
                        .bodyToMono(Usuario.class)
                        .block();

        System.out.println("Total de usuarios: " + usuarios.getPage().getTotalElements());
        System.out.println("Página actual: " + usuarios.getPage().getNumber());
        System.out.println("Tamaño página: " + usuarios.getPage().getSize());
        System.out.println("Número de páginas: " + usuarios.getPage().getTotalPages());
        System.out.println("**********************");
        System.out.println("Links");
        System.out.println("First: " + usuarios.get_links().getFirst().getHref());
        System.out.println("Self: " + usuarios.get_links().getSelf().getHref());
        System.out.println("Next: " + usuarios.get_links().getNext().getHref());
        System.out.println("Last: " + usuarios.get_links().getLast().getHref());
        System.out.println("**********************");
        System.out.println("Usuarios");
        for (Usuario usuario : usuarios.get_embedded().getUsuarioList()) {
                System.out.println(
                                "El usuario con id: " + usuario.getId() + " y nombre: " + usuario.getNombre()
                                                + " se encuentra disponible en el enlace: "
                                                + usuario.get_links().getSelf().getHref());
        }
    }
}
*/
