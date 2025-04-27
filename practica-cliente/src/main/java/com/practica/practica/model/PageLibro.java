package com.practica.practica.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageLibro{
    private Libros _embedded;
    private PageLinks _links;
    private PageMetadata page;
}


