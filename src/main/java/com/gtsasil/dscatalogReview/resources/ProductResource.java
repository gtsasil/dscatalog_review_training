package com.gtsasil.dscatalogReview.resources;

import com.gtsasil.dscatalogReview.dto.ProductDTO;
import com.gtsasil.dscatalogReview.services.ProductService;
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
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping //isto é um end point webservice
    public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                     @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                     @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
                                                     ){
        //List<Product> list = new ArrayList<>();
        //list.add(new Product(1l,"Book" ));   to add and test a new id and name

        //Teh PageRequest.of needs 4 arguments: page, number of line per page, direction and orderBy
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ProductDTO> list = service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable  Long id){
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);//ok() é código 200
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto){
         dto = service.insert(dto);
         URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id")
                 .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);//created() tem código 201
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable  Long id,@Valid @RequestBody ProductDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);//ok() é código 200
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable  Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();//noContent() é código 204. Usamos buil pois em deletar não se passa corpo na resposta
    }

}
