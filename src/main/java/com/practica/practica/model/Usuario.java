package com.practica.practica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuarios") 
@Data //genera los getters y setters

@NoArgsConstructor 
@AllArgsConstructor 
@JsonIgnoreProperties(ignoreUnknown = true) 
											
public class Usuario extends RepresentationModel<Usuario> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // valor generado por la base de datos
	@Schema(description = "Id del usuario", required = true, example = "1")
	private int id;

	@Schema(description = "Nombre del usuario", required = true, example = "Lionel")
	// @NotNull(message = "El nombre es obligatorio y no puede ser null")
	private String nombre;

	@Schema(description = "Matricula del usuario", required = false, example = "220082")
    private String matricula;

	@Schema(description = "fecha nacimiento", required = false, example = "28/12/2004")
    private Date fecha_nacimiento;

	@Schema(description = "correo del usuario", required = false, example = "lionel.scopice@alumnos.upm.es")
    private String correo_electronico;

}

