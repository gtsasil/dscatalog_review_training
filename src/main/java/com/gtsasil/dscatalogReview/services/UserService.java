package com.gtsasil.dscatalogReview.services;

import com.gtsasil.dscatalogReview.dto.*;
import com.gtsasil.dscatalogReview.enties.Category;
import com.gtsasil.dscatalogReview.enties.Role;
import com.gtsasil.dscatalogReview.enties.User;
import com.gtsasil.dscatalogReview.repositories.CategoryRepository;
import com.gtsasil.dscatalogReview.repositories.RoleRepository;
import com.gtsasil.dscatalogReview.repositories.UserRepository;
import com.gtsasil.dscatalogReview.services.exception.DataBaseException;
import com.gtsasil.dscatalogReview.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;//responsável por acessar o banco de dados

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(PageRequest pageRequest){
        Page <User> list = repository.findAll(pageRequest);// vai no repository e busca todasa caretogorias e atibui para list. ou seja: list contem todas as categorias

        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new UserDTO(entity);
    }

    @Transactional // não usamos "@Transactional(readOnly = true)" pois método faz a inserção na base de dados
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try{
            User entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new UserDTO(entity);
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
    private void copyDtoToEntity(UserDTO dto, User entity) {

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();//para limpar o conjunto de roles que por ventura esteja na entidade

        for(RoleDTO roleDto : dto.getRoles()){
            Role role = roleRepository.getOne(roleDto.getId());//busca por id a categoria para inserir no produto
            entity.getRoles().add(role);
        }
    }

}
