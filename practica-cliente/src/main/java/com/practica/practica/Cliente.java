package com.practica.practica;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class Cliente {

    static UsuarioService usuario_service = new UsuarioService();
    static LibroService libro_service = new LibroService();

    public static void main(String[] args) {

        System.out.println("Añadir 20 empleados para las pruebas");
        String[] nombres = {
                "Ana", "Luis", "María", "Carlos", "Elena",
                "Miguel", "Sandra", "Javier", "Sara", "Pedro",
                "Paula", "Sofía", "José", "Martina", "Diego",
                "Rosa", "Fernando", "Lucía", "Andrés", "Carmen"
        };

        int it=1;
        for (;it<50;) {
            usuario_service.deleteUsuario(it++);
        }

        System.out.println();
        System.out.println("#### Añadir empleado Laura ####");
        usuario_service.postUsuario("Laura","291202",new Date(),"aura-laura@boee.com");
        // System.out.println();
        // System.out.println("#### Añadir empleado Laura ####");
        // usuario_service.postEmpleado("Laura");
        System.out.println();
        usuario_service.putUsuario(1, "Leo Messi","123123",new Date(),null);


        System.out.println();
        System.out.println("### Obtener usuario existente ####");
        usuario_service.getUsuario(1);

        System.out.println();
        System.out.println("#### Obtener usuario no existente ####");
        usuario_service.getUsuario(99);

        System.out.println();
        System.out.println("#### Actualizar empleado 1  ####");
        usuario_service.putUsuario(1, "Niki Lauda","123123",new Date(),null);

        System.out.println();
        System.out.println("### Obtener usuario existente (Laura) ####");
        usuario_service.getUsuario(1);

        System.out.println();
        System.out.println("#### Eliminar empleado 30 ####");
        usuario_service.deleteUsuario(30);


        System.out.println();
        System.out.println("#### Intentar eliminar un usuario con prestamos activos");
        usuario_service.deleteUsuario(1);
        System.out.println();

        System.out.println("#### Listar todos los usuarios con paginación page:0 size:3 ####");
        usuario_service.getUsuarios(0, 1);


        ///LIBROSS - Anadir mas ejemplos como actualizar libro, eliminar libro, listar todos,
        System.out.println();
        System.out.println("#### Lista de todos los libros de la bilbioteca ");
        libro_service.getlibros(0, 5,null,null);

        System.out.println();
        System.out.println("#### Lista de todos los libros de la bilbioteca que tengan 'algebra' en el titulo");
        libro_service.getlibros(0, 2,"algebra",null);


        System.out.println();
        System.out.println("#### Lista de todos los libros de la bilbioteca que tengan 'algebra' en el titulo y esten disponibles");
        libro_service.getlibros(0, 1,"algebra",1);


        System.out.println();
        System.out.println("#### Anadir libro a la biblioteca");
        libro_service.postLibro("calculus", "thomas", "14th International Edition", "pearson", "192309238-2", 30);


        System.out.println();
        System.out.println("#### Actualizar un libro existente en la biblioteca");
        libro_service.putLibro(7,"calculus", "thomas", "14th International Edition", "pearson", "192309238-2", 30);

        System.out.println();
        System.out.println("#### Intento Actualizar un libro de la biblioteca inexistente");
        libro_service.putLibro(-1,"calculus", "thomas", "14th International Edition", "pearson", "192309238-2", 30);

        System.out.println();
        System.out.println("#### Intento de eliminar un libro de la biblioteca");
        libro_service.deleteLibro(7);


        //Add deleteLibro con prestamo


        System.out.println();
        System.out.println("#### Intento de eliminar un libro de la biblioteca que se encuentra en un prestamo");
        libro_service.deleteLibro(1);








    }

}

