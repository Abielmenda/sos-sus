package com.practica.practica.exceptions;



public class UsuarioTienePrestamo extends RuntimeException{
    public UsuarioTienePrestamo(int id){
        super("Usuario " + id + " no se puede eliminar hasta que devuelva sus prestamos");
    }
}


