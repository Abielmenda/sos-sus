package com.practica.practica.exceptions;



public class UsuarioPenalizadoException extends RuntimeException{
    public UsuarioPenalizadoException(int id){
        super("Usuario " + id +" no puede realizar prestamos");
    }
}

