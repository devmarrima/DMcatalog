package com.devmarrima.DMcatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devmarrima.DMcatalog.dto.ProductDTO;
import com.devmarrima.DMcatalog.entities.Category;
import com.devmarrima.DMcatalog.entities.Product;
import com.devmarrima.DMcatalog.repositories.CategoryRepository;
import com.devmarrima.DMcatalog.repositories.ProductRepository;
import com.devmarrima.DMcatalog.services.exceptions.DadabaseException;
import com.devmarrima.DMcatalog.services.exceptions.ResourceNotFoundException;
import com.devmarrima.DMcatalog.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private Long existingId;
	private Long noExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		noExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		productDTO.getCategories().get(0).setId(existingId);
		page = new PageImpl<>(List.of(product));

		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(noExistingId)).thenReturn(Optional.empty());

		when(repository.existsById(existingId)).thenReturn(true);
		when(repository.existsById(noExistingId)).thenReturn(false);
		when(repository.existsById(dependentId)).thenReturn(true);

		when(repository.getReferenceById(existingId)).thenReturn(product);
		when(repository.getReferenceById(noExistingId)).thenThrow(EntityNotFoundException.class);

		when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		when(categoryRepository.getReferenceById(noExistingId)).thenThrow(EntityNotFoundException.class);

		doNothing().when(repository).deleteById(existingId);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(noExistingId);
		doThrow(DadabaseException.class).when(repository).deleteById(dependentId);

	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionDoesNoExistsId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(noExistingId, productDTO);
		});

	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = service.update(existingId, productDTO);
		Assertions.assertNotNull(dto);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionDoesNoExistsId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(noExistingId);
		});

	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = service.findById(existingId);
		Assertions.assertNotNull(dto);
	}

	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 12);
		Page<ProductDTO> result = service.findAllPeged(pageable);
		Assertions.assertNotNull(result);
		verify(repository).findAll(pageable);

	}

	@Test
	public void deleteShouldThrowDadabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DadabaseException.class, () -> {
			service.delete(dependentId);
		});
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenDoNothingId() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(noExistingId);
		});

	}

	@Test
	public void deleteShouldDoNothingWhenIdExisting() {

		Assertions.assertDoesNotThrow(() -> {

			service.delete(existingId);
		});
		Mockito.verify(repository).deleteById(existingId);
	}

}
