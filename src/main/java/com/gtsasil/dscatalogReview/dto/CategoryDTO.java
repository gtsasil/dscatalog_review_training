package com.gtsasil.dscatalogReview.dto;

import com.gtsasil.dscatalogReview.enties.Category;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private long Id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(long id, String name) {
        Id = id;
        this.name = name;
    }

    //Construtor para povoar o CategoryDTO ao passar entity como argumento
    public CategoryDTO(Category entity){
        Id = entity.getId();
        this.name = entity.getName();
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
