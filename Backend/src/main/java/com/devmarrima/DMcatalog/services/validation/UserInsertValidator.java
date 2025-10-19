package com.devmarrima.DMcatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.devmarrima.DMcatalog.dto.UserInsertDTO;
import com.devmarrima.DMcatalog.entities.User;
import com.devmarrima.DMcatalog.repositories.UserRepository;
import com.devmarrima.DMcatalog.resources.exceptions.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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

        if (user != null) {
            list.add(new FieldMessage("email", "Este email já existe"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
