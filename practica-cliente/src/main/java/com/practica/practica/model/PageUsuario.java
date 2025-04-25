package com.practica.practica.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageUsuario {
    private Usuarios _embedded;
    private PageLinks _links;
    private PageMetadata page;
}

