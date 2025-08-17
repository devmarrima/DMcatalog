package com.devmarrima.DMcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devmarrima.DMcatalog.entities.Product;
import com.devmarrima.DMcatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;
	private long existing;
	private long countTotalAmount;
	private long notExisting;

	@BeforeEach
	void setUp() throws Exception {
		existing = 1L;
		countTotalAmount = 25L;
		notExisting = 1000L;
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		productRepository.deleteById(existing);
		Optional<Product> r = productRepository.findById(existing);

		Assertions.assertFalse(r.isPresent());
	}

	@Test
	public void saveShouldPersistWhitAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);

		product = productRepository.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalAmount + 1, product.getId());
	}

	@Test
	public void findByIdShouldReturnNotEmptyOptionalWhenIdExists() {
		Optional<Product> r = productRepository.findById(existing);

		Assertions.assertTrue(r.isPresent());

	}

	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		Optional<Product> r = productRepository.findById(notExisting);

		Assertions.assertTrue(r.isEmpty());

	}
}
