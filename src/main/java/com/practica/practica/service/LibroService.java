package  com.practica.practica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.practica.model.Libro;
import com.practica.practica.repository.LibroRepository;

import lombok.AllArgsConstructor;


@Service // Marcamos la clase compo componente de servicio
@AllArgsConstructor
public class LibroService{

    private final LibroRepository repository;

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
            return repository.findByCopiasGreaterThan(0,paginable);
        }
        else {
            return repository.findByTituloContainingAndCopiasGreaterThan(contiene,0, paginable);
        }
    }

    public void eliminarLibro(int id) {
        repository.deleteById(id);
    }
}


