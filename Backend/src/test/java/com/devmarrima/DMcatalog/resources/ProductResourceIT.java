package com.devmarrima.DMcatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devmarrima.DMcatalog.dto.ProductDTO;
import com.devmarrima.DMcatalog.services.ProductService;
import com.devmarrima.DMcatalog.tests.Factory;
import com.devmarrima.DMcatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("unused")
	@Autowired
	private ProductService service;

	private Long existId;
	private Long noExistId;
	private Long countTotalProducts;
	private String username, password, bearerToken;

	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		noExistId = 1000L;
		countTotalProducts = 25L;
		
		username = "maria@gmail.com";
		password = "123456";
		
		bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
	}

	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception {
		ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

	}

	@Test
	public void updateShouldReturnProductDTOWhenExistsId() throws Exception {
		ProductDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();

		ResultActions result = mockMvc.perform(put("/products/{id}", existId)
				.header("Authorization", "Bearer " + bearerToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
	}

	@Test
	public void updateShouldReturnNotFoundWhenDoesNoExistsId() throws Exception {
		ProductDTO productDTO = Factory.createProductDTO();
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform(put("/products/{id}", noExistId)
				.header("Authorization", "Bearer " + bearerToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isNotFound());

	}

}
