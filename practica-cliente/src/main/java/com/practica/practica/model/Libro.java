package  com.practica.practica.model;

import lombok.*;

@Data //genera los getters y setters
@NoArgsConstructor 
@AllArgsConstructor 

						
public class Libro {
    private int id;
    private String titulo;
    private String autores;
    private String edicion;
    private String editorial;
    private String isbn;
    private int copias;
    private ResourceLink _links;
}
