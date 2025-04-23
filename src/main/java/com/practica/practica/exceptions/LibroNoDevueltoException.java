package com.practica.practica.exceptions;



public class LibroNoDevueltoException extends RuntimeException{
    public LibroNoDevueltoException(int id){
        super("El Libro " + id + " no ha sido devuelto. Hay que devolverlo antes de eliminarlo"); 
    }
}



