package com.practica.practica.exceptions;



public class LibroNotFoundException extends RuntimeException{
    public LibroNotFoundException(int id){
        super("Libro " + id +" no encontrado");
    }
}

