package  com.practica.practica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.practica.practica.model.Usuario;
import com.practica.practica.repository.UsuarioRepository;

import lombok.AllArgsConstructor;


@Service // Marcamos la clase compo componente de servicio
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public boolean existeUsuarioPorId(int id) {
        return repository.existsById(id);
    }

    public Usuario crearUsuario(Usuario user) {
        return repository.save(user);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return repository.findById(id);
    }

    public List<Usuario> getAllUsers(){
        return repository.findAll();
    }

    public Page<Usuario> buscarUsuarios(int page, int size) {

        Pageable paginable = PageRequest.of(page, size);
        return repository.findAll(paginable);
    }

    public void eliminarUsuario(int id) {
        repository.deleteById(id);
    }

}

