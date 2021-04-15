package com.gtsasil.dscatalogReview.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.gtsasil.dscatalogReview.dto.UserInsertDTO;
import com.gtsasil.dscatalogReview.enties.User;
import com.gtsasil.dscatalogReview.repositories.UserRepository;
import com.gtsasil.dscatalogReview.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();
        User user = repository.findByEmail(dto.getEmail());

        if(user != null){
            list.add(new FieldMessage("email", "This e-mail already exists"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}