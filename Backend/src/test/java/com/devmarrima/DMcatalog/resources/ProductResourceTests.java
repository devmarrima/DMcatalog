package com.devmarrima.DMcatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devmarrima.DMcatalog.dto.ProductDTO;
import com.devmarrima.DMcatalog.services.ProductService;
import com.devmarrima.DMcatalog.services.exceptions.DadabaseException;
import com.devmarrima.DMcatalog.services.exceptions.ResourceNotFoundException;
import com.devmarrima.DMcatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@SuppressWarnings("removal")
	@MockBean
	private ProductService service;

	private ProductDTO productDtO;
	private PageImpl<ProductDTO> page;
	private Long existsId;
	private Long noExistsId;
	private Long dependentId;

	@BeforeEach
	void setUp() throws Exception {
		productDtO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDtO));
		existsId = 1L;
		noExistsId = 2L;
		dependentId = 3L;
		
		
		when(service.findAllPeged(any())).thenReturn(page);
		when(service.findById(existsId)).thenReturn(productDtO);
		when(service.findById(noExistsId)).thenThrow(ResourceNotFoundException.class);
		when(service.update(eq(existsId), any())).thenReturn(productDtO);
		when(service.update(eq(noExistsId), any())).thenThrow(ResourceNotFoundException.class);
		when(service.insert(any())).thenReturn(productDtO);
		
		doNothing().when(service).delete(existsId);
		doThrow(ResourceNotFoundException.class).when(service).delete(noExistsId);
		doThrow(DadabaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		//mockMvc.perform(get("/products")).andExpect(status().isOk()); //"forma mais simplificada"
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isOk());

	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenExistsId() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existsId)
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
		result.andExpect(jsonPath("$.date").exists());
		result.andExpect(jsonPath("$.categories").exists());
		
		
	}
	
	@Test
	public void findByIdShouldReturnNotfoundWhenDoesNoExistsId() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", noExistsId)
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isNotFound());
		
	}

	@Test
	public void updateShouldReturnProductDTOWhenExistsId() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDtO);
		ResultActions result = mockMvc.perform(put("/products/{id}",existsId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
		result.andExpect(jsonPath("$.date").exists());
		result.andExpect(jsonPath("$.categories").exists());
		
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenDoesNoExistsId() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDtO);
		ResultActions result = mockMvc.perform(put("/products/{id}", noExistsId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isNotFound());
	}

	@Test
	public void insertShouldReturnProductDTOCreated() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(productDtO);
		ResultActions result = mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON.toString())
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
		result.andExpect(jsonPath("$.imgUrl").exists());
		result.andExpect(jsonPath("$.date").exists());
		result.andExpect(jsonPath("$.categories").exists());
		
		}
	
	@Test
	public void deleteShouldReturnNoContentWhenExistsId() throws Exception {
		ResultActions result = mockMvc.perform(delete("/products/{id}",existsId)
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isNoContent());
				
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenDoesNoExistsId()throws Exception{
		ResultActions result = mockMvc.perform(delete("/products/{id}",noExistsId)
				.accept(MediaType.APPLICATION_JSON.toString()));
		result.andExpect(status().isNotFound());
	}

}
