package com.gtsasil.dscatalogReview.services;

import com.gtsasil.dscatalogReview.dto.CategoryDTO;
import com.gtsasil.dscatalogReview.enties.Category;
import com.gtsasil.dscatalogReview.repositories.CategoryRepository;
import com.gtsasil.dscatalogReview.services.exception.DataBaseException;
import com.gtsasil.dscatalogReview.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;//responsável por acessar o banco de dados

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page <Category> list = repository.findAll(pageRequest);// vai no repository e busca todasa caretogorias e atibui para list. ou seja: list contem todas as categorias
        return list.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);

    }

    @Transactional // não impl readOnly pois método faz a inserção na base de dados
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try{
            Category entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);

        }
    }

    // não colocams Transaction pois queremos capturar um exception e com transaction isso não aconteceria
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
        catch(DataIntegrityViolationException e){
            throw new DataBaseException("Integrity Violation");

        }
    }
}
