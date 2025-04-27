package com.practica.practica;

import java.util.Date;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import com.practica.practica.model.*;

public class LibroService{
    private WebClient clienteWeb = WebClient.builder().baseUrl("http://localhost:8080/biblioteca/gestion").build();

    public void postLibro(String titulo, String autores, String edicion, String editorial, String isbn, int copias) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutores(autores);
        libro.setEdicion(edicion);
        libro.setEditorial(editorial);
        libro.setIsbn(isbn);
        libro.setCopias(copias);

        try {
                String referencia = clienteWeb.post()
                                .uri("/libros")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(libro), Libro.class)
                                .retrieve()
                                .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                                .bodyToMono(String.class)
                                                .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                                .then(Mono.empty()) 
                                )
                                .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                                .bodyToMono(String.class)
                                                .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                                .then(Mono.empty()))
                                .toBodilessEntity() 
                                .map(response -> {
                                        if (response.getHeaders().getLocation() != null) {
                                                return response.getHeaders().getLocation().toString();
                                        } else {
                                                throw new RuntimeException(
                                                                "No se recibió una URL en la cabecera Location");
                                        }
                                })
                                .block();
                if (referencia != null) {
                        System.out.println(referencia);
                }
        } catch (RuntimeException e) {
                System.err.println("Error: " + e.getMessage());
        }
    }

    public void getLibro(int idlibro) {

        // Realizamos la petición GET y deserializamos la respuesta en
        // Empleado
        Libro libro = clienteWeb.get()
                        .uri("/libros/" + idlibro)
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
                        .bodyToMono(Libro.class)
                        .block(); // Usamos block() para obtener la respuesta de forma síncrona

        if (libro != null) {
                String selfLink = libro.get_links().getSelf().getHref();
                if (selfLink != null) {
                        System.out.println("El libro con id: " + libro.getId() + " y titulo: "
                                        + libro.getTitulo()
                                        + " se encuentra disponible en el enlace: " + selfLink);
                } else {
                        System.out.println("Enlace 'self' no encontrado.");
                }
        }

    }

    public void putlibro(int libroId,String titulo,String autores,String edicion,String editorial,String isbn,int copias){
        Libro libro = new Libro();
        libro.setId(libroId);
        libro.setTitulo(titulo);
        libro.setAutores(autores);
        libro.setEdicion(edicion);
        libro.setEditorial(editorial);
        libro.setIsbn(isbn);
        libro.setCopias(copias);
    

        clienteWeb.put()
                        .uri("/libros/{id}", libro.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(libro), Libro.class)
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
    
    public void deleteLibro(int idlibro) {
        clienteWeb.delete()
                        .uri("/libros/{id}", idlibro)
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

    public void getlibros(int page, int size) {
        PageLibro libros = clienteWeb.get()
                        .uri("/libros?page={page}&size={size}", page, size)
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
                        .bodyToMono(PageLibro.class)
                        .block();

        System.out.println("Total de libros: " + libros.getPage().getTotalElements());
        System.out.println("Página actual: " + libros.getPage().getNumber());
        System.out.println("Tamaño página: " + libros.getPage().getSize());
        System.out.println("Número de páginas: " + libros.getPage().getTotalPages());
        System.out.println("**********************");
        System.out.println("Links");
        System.out.println("First: " + libros.get_links().getFirst().getHref());
        System.out.println("Self: " + libros.get_links().getSelf().getHref());
        System.out.println("Next: " + libros.get_links().getNext().getHref());
        System.out.println("Last: " + libros.get_links().getLast().getHref());
        System.out.println("**********************");
        System.out.println("libros");

        for (Libro libro : libros.get_embedded().getLibroList()) {
                System.out.println(
                                "El libro con id: " + libro.getId() + " y titulo: " + libro.getTitulo()
                                                + " se encuentra disponible en el enlace: "
                                                + libro.get_links().getSelf().getHref());
        }
    }
}

