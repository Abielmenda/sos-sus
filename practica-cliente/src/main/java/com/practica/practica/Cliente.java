package com.practica.practica;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class Cliente {

    static BibliotecaService service = new BibliotecaService();

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
            service.deleteUsuario(it++);
        }

        System.out.println("#### Añadir empleado Laura ####");
        service.postUsuario("Laura","291202",new Date(),"aura-laura@boee.com");
        // System.out.println();
        // System.out.println("#### Añadir empleado Laura ####");
        // service.postEmpleado("Laura");
        System.out.println();
        service.putUsuario(1, "Leo Messi","123123",new Date(),null);

        System.out.println("### Obtener usuario existente ####");
        service.getUsuario(1);

        System.out.println();
        System.out.println("#### Obtener usuario no existente ####");
        service.getUsuario(99);
        System.out.println();
        System.out.println("#### Actualizar empleado 1  ####");
        service.putUsuario(1, "Niki Lauda","123123",new Date(),null);
        System.out.println();
        System.out.println("### Obtener usuario existente (Laura) ####");
        service.getUsuario(1);
        // System.out.println("#### Actualizar empleado 30 ####");
        // service.putEmpleado(30, "Laura Castillo");
        // System.out.println();
        // System.out.println("#### Eliminar empleado 21 ####");
        // service.deleteEmpleado(1);
        // System.out.println();
        // System.out.println("#### Eliminar empleado 30 ####");
        // service.deleteEmpleado(30);
        // System.out.println();
        // System.out.println("#### Listar todos los empleados con paginación page:0 size:3 ####");
        // service.getEmpleados(0, 3);
        // System.out.println();
    }

}

