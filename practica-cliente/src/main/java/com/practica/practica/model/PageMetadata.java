package com.practica.practica.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageMetadata {
    private int size;
    private int totalElements;
    private int totalPages;
    private int number;
}

