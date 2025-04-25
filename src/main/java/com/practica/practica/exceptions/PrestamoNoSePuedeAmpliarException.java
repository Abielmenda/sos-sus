package com.practica.practica.exceptions;

public class PrestamoNoSePuedeAmpliarException extends RuntimeException{
    public PrestamoNoSePuedeAmpliarException(){
        super("No se puede ampliar el prestamo porque esta fuera de fecha");
    }
}



