package com.gtsasil.dscatalogReview.resources;

import com.gtsasil.dscatalogReview.dto.UserDTO;
import com.gtsasil.dscatalogReview.dto.UserInsertDTO;
import com.gtsasil.dscatalogReview.dto.UserUpdateDTO;
import com.gtsasil.dscatalogReview.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @GetMapping //isto é um end point webservice
    public ResponseEntity<Page<UserDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                     @RequestParam(value = "orderBy", defaultValue = "firstName") String orderBy
                                                     ){
        //List<User> list = new ArrayList<>();
        //list.add(new User(1l,"Book" ));   to add and test a new id and name

        //Teh PageRequest.of needs 4 arguments: page, number of line per page, direction and orderBy
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<UserDTO> list = service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable  Long id){
        UserDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);//ok() é código 200
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
        // cria se um novo UserDto pois exite a herança para chamar o password do UserInsert (comparar com ProductResource)
         UserDTO newDto = service.insert(dto);
         URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id")
                 .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);//created() tem código 201
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable  Long id, @Valid @RequestBody UserUpdateDTO dto){
        UserDTO newdto = service.update(id, dto);
        return ResponseEntity.ok().body(newdto);//ok() é código 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable  Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();//noContent() é código 204. Usamos buil pois em deletar não se passa corpo na resposta
    }

}
