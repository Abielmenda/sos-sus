package com.practica.practica.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class Usuario{
	private int id;
	private String nombre;
    private String matricula;
    private Date fecha_nacimiento;
    private String correo_electronico;
    private Date fin_penalizacion;
    private ResourceLink _links;

}

