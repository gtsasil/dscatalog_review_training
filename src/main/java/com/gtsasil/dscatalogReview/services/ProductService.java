package com.gtsasil.dscatalogReview.services;

import com.gtsasil.dscatalogReview.dto.CategoryDTO;
import com.gtsasil.dscatalogReview.dto.ProductDTO;
import com.gtsasil.dscatalogReview.enties.Category;
import com.gtsasil.dscatalogReview.enties.Product;
import com.gtsasil.dscatalogReview.repositories.CategoryRepository;
import com.gtsasil.dscatalogReview.repositories.ProductRepository;
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
public class ProductService {

    @Autowired
    private ProductRepository repository;//responsável por acessar o banco de dados

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page <Product> list = repository.findAll(pageRequest);// vai no repository e busca todasa caretogorias e atibui para list. ou seja: list contem todas as categorias

        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new ProductDTO(entity, entity.getCategories());// using entity.getCategories() will also bring a list with the categories
    }

    @Transactional // não impl readOnly pois método faz a inserção na base de dados
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try{
            Product entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new ProductDTO(entity);
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
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();//para limpar o conjunto de categorias que por ventura esteja na entidade

        for(CategoryDTO catDto : dto.getCategories()){
            Category category = categoryRepository.getOne(catDto.getId());//busca por id a categoria para inserir no produto
            entity.getCategories().add(category);
        }
    }

}
