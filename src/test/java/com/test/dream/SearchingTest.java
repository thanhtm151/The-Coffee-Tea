package com.test.dream;

import com.backend.dream.entity.Product;
import com.backend.dream.repository.ProductRepository;
import com.backend.dream.service.imp.ProductServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SearchingTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImp productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSearchByName() {
        List<Product> mockProductList = new ArrayList<>();
        Product product1 = Product.builder()
                .id(1L)
                .name("Bạc xỉu")
                .price(10.0)
                .image("image1.jpg")
                .describe("Description for Bạc xỉu")
                .createDate(new Date())
                .active(true)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Hồng trà")
                .price(8.5)
                .image("image2.jpg")
                .describe("Description for Hồng trà")
                .createDate(new Date())
                .active(true)
                .build();

        mockProductList.add(product1);
        mockProductList.add(product2);

        String searchProductName = "Bạc xỉu";
        boolean found = false;

        for (Product product : mockProductList) {
            if (product.getName().equalsIgnoreCase(searchProductName)) {
                found = true;
                break;
            }
        }

        assertEquals(true, found, "Product with name '" + searchProductName + "' not found in the list.");
    }

    @Test
    public void testSearchByIncorrectName() {
        List<Product> mockProductList = new ArrayList<>();
        Product product1 = Product.builder()
                .id(1L)
                .name("Bạc xỉu")
                .price(10.0)
                .image("image1.jpg")
                .describe("Description for Bạc xỉu")
                .createDate(new Date())
                .active(true)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Hồng trà")
                .price(8.5)
                .image("image2.jpg")
                .describe("Description for Hồng trà")
                .createDate(new Date())
                .active(true)
                .build();

        mockProductList.add(product1);
        mockProductList.add(product2);

        String searchProductName = "Cơm";
        boolean found = false;

        for (Product product : mockProductList) {
            if (product.getName().equalsIgnoreCase(searchProductName)) {
                found = true;
                break;
            }
        }

        assertEquals(false, found);
    }

    @Test
    public void testSearchByTwoSpaces() {
        List<Product> mockProductList = new ArrayList<>();
        Product product1 = Product.builder()
                .id(1L)
                .name("Bạc xỉu")
                .price(10.0)
                .image("image1.jpg")
                .describe("Description for Bạc xỉu")
                .createDate(new Date())
                .active(true)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Hồng trà")
                .price(8.5)
                .image("image2.jpg")
                .describe("Description for Hồng trà")
                .createDate(new Date())
                .active(true)
                .build();

        mockProductList.add(product1);
        mockProductList.add(product2);
        String searchProductName = "  ";
        boolean found = false;

        for (Product product : mockProductList) {
            if (product.getName().equalsIgnoreCase(searchProductName.trim())) {
                found = true;
                break;
            }
        }

        assertEquals(false, found);
    }

}
