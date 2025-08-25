package com.devmarrima.DMcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devmarrima.DMcatalog.dto.ProductDTO;
import com.devmarrima.DMcatalog.repositories.ProductRepository;
import com.devmarrima.DMcatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	private Long existId;
	private Long noExistId;
	private Long countTotalProducts;
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	@BeforeEach
	void setUp() throws Exception{
		existId = 1L;
		noExistId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existId);
		Assertions.assertEquals(countTotalProducts -1, repository.count());

	}
	
	@Test
	public void deleteShouldDeleteResourceWhenDoesNotExistsId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(noExistId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		
		PageRequest page = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPeged(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
		
	}

	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNoExists() {
		PageRequest page = PageRequest.of(100, 10);
		Page<ProductDTO> result = service.findAllPeged(page);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortbyName() {
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAllPeged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
		
	}
}
