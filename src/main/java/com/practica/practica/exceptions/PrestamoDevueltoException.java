package com.practica.practica.exceptions;

public class PrestamoDevueltoException extends RuntimeException{
    public PrestamoDevueltoException(int id){
        super("El prestamo ya fue devuelto");
    }
}


