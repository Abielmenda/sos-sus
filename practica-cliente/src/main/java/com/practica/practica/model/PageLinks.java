package com.practica.practica.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageLinks {
    private Href first;
    private Href self;
    private Href next;
    private Href last;
}

