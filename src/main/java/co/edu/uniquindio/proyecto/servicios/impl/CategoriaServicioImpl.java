package co.edu.uniquindio.proyecto.servicios.impl;

import co.edu.uniquindio.proyecto.dto.moderadores.CategoriaDTO;
import co.edu.uniquindio.proyecto.modelo.documentos.Categoria;
import co.edu.uniquindio.proyecto.repositorios.CategoriaRepo;
import co.edu.uniquindio.proyecto.servicios.CategoriaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServicioImpl implements CategoriaServicio {

    private final CategoriaRepo categoriaRepo;

    @Override
    public List<CategoriaDTO> obtenerCategorias() {
        List<Categoria> categorias = categoriaRepo.findAll();

        return categorias.stream()
                .map(c -> new CategoriaDTO(c.getId().toString(), c.getNombre(), c.getDescripcion()))
                .collect(Collectors.toList());
    }
}
