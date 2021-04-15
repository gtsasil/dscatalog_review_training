package com.gtsasil.dscatalogReview.services.validation;

import com.gtsasil.dscatalogReview.dto.UserInsertDTO;
import com.gtsasil.dscatalogReview.dto.UserUpdateDTO;
import com.gtsasil.dscatalogReview.enties.User;
import com.gtsasil.dscatalogReview.repositories.UserRepository;
import com.gtsasil.dscatalogReview.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private ServletRequest request;// quarda as infomacoes da requisição

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        var uriVars = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(uriVars.get("id"));

        List<FieldMessage> list = new ArrayList<>();
        User user = repository.findByEmail(dto.getEmail());

        if(user != null && userId != user.getId()){
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