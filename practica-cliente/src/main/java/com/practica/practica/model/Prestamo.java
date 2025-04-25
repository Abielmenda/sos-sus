package  com.practica.practica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@Table(name = "prestamos")
@Data 

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Prestamo extends RepresentationModel<Prestamo>{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Schema(description = "Id del prestamo")
    private int id_prestamo;

    @Schema(description = "usuario asociado")
    private int id_usuario;

    @Schema(description = "libro asociado")
    private int id_libro;

    @Schema(description = "fecha prestado")
    // @NotNull(message = "la fecha en la que se hace el prestamo no puede ser vacia")
    private Date fecha_prestado;

    @Schema(description = "")
    private Date fecha_devuelto;
}
