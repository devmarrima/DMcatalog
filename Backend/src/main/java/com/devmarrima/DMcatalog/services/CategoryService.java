package com.devmarrima.DMcatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devmarrima.DMcatalog.dto.CategoryDTO;
import com.devmarrima.DMcatalog.entities.Category;
import com.devmarrima.DMcatalog.repositories.CategoryRepository;
import com.devmarrima.DMcatalog.services.exceptions.DadabaseException;
import com.devmarrima.DMcatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPeged(PageRequest pageRequest) {
        Page<Category> page = categoryRepository.findAll(pageRequest);
        return page.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> entity = categoryRepository.findById(id);
        Category cat = entity.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(cat);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getOne(id);
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("id " + id + " not found!");
        }

    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("id " + id + " not found!");
        } catch (DataIntegrityViolationException e) {
            throw new DadabaseException("Integrity violation of id:" + id);
        }
    }

}
