package com.gtsasil.dscatalogReview.resources;

import com.gtsasil.dscatalogReview.dto.CategoryDTO;
import com.gtsasil.dscatalogReview.services.CategoryService;
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
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;

    @GetMapping //isto é um end point webservice
    public ResponseEntity<Page<CategoryDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                     @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
                                                     ){
        //List<Category> list = new ArrayList<>();
        //list.add(new Category(1l,"Book" ));   to add and test a new id and name

        //Teh PageRequest.of needs 4 arguments: page, number of line per page, direction and orderBy
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<CategoryDTO> list = service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable  Long id){
        CategoryDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);//ok() é código 200
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
         dto = service.insert(dto);
         URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id")
                 .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);//created() tem código 201
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable  Long id, @RequestBody CategoryDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);//ok() é código 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable  Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();//noContent() é código 204. Usamos buil pois em deletar não se passa corpo na resposta
    }

}
