package com.practica.practica.exceptions;



class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(int id){
        super("Usuario " + id +" no encontrado");
    }
}
