package  com.practica.practica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "libros") 
@Data //genera los getters y setters

@NoArgsConstructor 
@AllArgsConstructor 
@JsonIgnoreProperties(ignoreUnknown = true) 
						

public class Libro extends RepresentationModel<Libro>{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // valor generado por la base de datos
	@Schema(description = "Id del libro", required = true, example = "1")
	private int id;

    
	@Schema(description = "Titulo del libro", required = true, example = "El señor de las moscas")
	// @NotNull(message = "El nombre es obligatorio y no puede ser null")
	private String titulo;

    @Schema(description = "Autor/es del libro", required = false, example = "William Golding")
	private String autores;

    @Schema(description = "Edicion del libro", required = false, example = "tercera edicion")
	private String edicion;

    @Schema(description = "Editorial del libro", required = false, example = "Circulo de lectores")
	private String editorial;

    @Schema(description = "ISBN del libro", required = false, example = "8435009513")
	private int isbn;

    @Schema(description = "Copias disponibles del libro", required = true, example = "2")
    // @NotNull(message = "Es obligatorio indicar las copias disponibles y no pueden ser 0")
	private int copias;
}
