package com.test.dream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import com.backend.dream.dto.ProductDTO;
import com.backend.dream.entity.Product;
import com.backend.dream.rest.ProductRestController;
import com.backend.dream.service.ProductService;
import com.backend.dream.service.ProductSizeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TestCRUDProduct {
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private ProductSizeService productSizeService;

    @InjectMocks
    private ProductRestController productRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productRestController).build();
    }


    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Cà phê chồn");
        productDTO.setPrice(5000.0);

        when(productService.create(any(ProductDTO.class))).thenReturn(new Product());

        mockMvc.perform(post("/rest/products")
                        .contentType("application/json")
                        .content("{\"name\":\"Cà phê chồn\",\"price\":5000.0}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Cà phê siêu chồn");
        productDTO.setPrice(50000.0);

        when(productService.update(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/rest/products/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"id\":1,\"name\":\"Cà phê siêu chồn\",\"price\":50000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(100.0)));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/rest/products/{id}", productId))
                .andExpect(status().isOk());

        verify(productService, times(1)).delete(productId);
    }

}
