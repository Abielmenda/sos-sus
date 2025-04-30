package  com.practica.practica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.practica.model.Libro;
import com.practica.practica.repository.LibroRepository;
import com.practica.practica.repository.PrestamoRepository;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Service // Marcamos la clase compo componente de servicio
@AllArgsConstructor
public class LibroService{

    private final LibroRepository repository;
    private final PrestamoRepository prestamo_repository;

    public boolean existeLibroPorId(int id) {
        return repository.existsById(id);
    }

    public Libro crearLibro(Libro libro) {
        return repository.save(libro);
    }

    public Optional<Libro> buscarPorId(int id) {
        return repository.findById(id);
    }

    public List<Libro> getAllBooks(){
        return repository.findAll();
    }

    public Page<Libro> buscarLibros(String contiene, Boolean disponible, int page, int size) {

        Pageable paginable = PageRequest.of(page, size);
        if (contiene == null && disponible == null) {
            return repository.findAll(paginable);
        } else if(disponible == null){
            return repository.findByTituloContains(contiene, paginable);
        }else if(contiene == null){
            Page<Libro> libros = repository.findAll(paginable);
            List<Libro> disponibles = libros.getContent().stream()

            .filter(libro -> {
                int prestados = prestamo_repository.myFindById_libro(libro.getId());
                return (libro.getCopias() - prestados) > 0;
            })
            .collect(Collectors.toList());
            System.out.println(disponibles);
            return new PageImpl<>(disponibles, paginable, disponibles.size());
        }
        else {
            Page<Libro> libros = repository.findByTituloContains(contiene,paginable);
            List<Libro> disponibles = libros.getContent().stream()

            .filter(libro -> {
                int prestados = prestamo_repository.myFindById_libro(libro.getId());
                return (libro.getCopias() - prestados) > 0;
            })
            .collect(Collectors.toList());
            System.out.println(disponibles);
            return new PageImpl<>(disponibles, paginable, disponibles.size());
        }
    }

    public void eliminarLibro(int id) {
        repository.deleteById(id);
    }
}


