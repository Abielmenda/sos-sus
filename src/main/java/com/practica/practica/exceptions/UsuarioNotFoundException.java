package com.practica.practica.exceptions;



public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(int id){
        super("Usuario " + id +" no encontrado");
    }
}
