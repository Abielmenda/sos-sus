package com.practica.practica.exceptions;



public class LibroNoDisponibleException extends RuntimeException{
    public LibroNoDisponibleException(int id){
        super("Libro " + id +" no esta disponible para prestar"); 
    }
}


