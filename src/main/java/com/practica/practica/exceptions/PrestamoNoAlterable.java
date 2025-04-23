package com.practica.practica.exceptions;

public class PrestamoNoAlterable extends RuntimeException{
    public PrestamoNoAlterable(){
        super("El prestamo ya fue devuelto. No se puede ampliar");
    }
}


