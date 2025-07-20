package com.devmarrima.DMcatalog.tests;

import java.time.Instant;

import com.devmarrima.DMcatalog.dto.ProductDTO;
import com.devmarrima.DMcatalog.entities.Category;
import com.devmarrima.DMcatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "camera DSLR Canon T7i",
				"Câmera digital profissional com sensor CMOS de 24.2MP, gravação em Full HD, Wi-Fi e NFC integrado",
				3499.99, "https://example.com/img/canon-t7i.jpg", Instant.parse("2024-12-15T14:30:00Z"));
		product.getCategories().add(new Category(2L, "Eletrônicos"));
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

}
