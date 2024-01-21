package com.backend.dream.service.imp;

import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.dto.ProductDTO;
import com.backend.dream.entity.Product;
import com.backend.dream.entity.ProductSize;
import com.backend.dream.mapper.ProductMapper;
import com.backend.dream.repository.ProductRepository;
import com.backend.dream.repository.ProductSizeRepository;
import com.backend.dream.service.*;
import com.backend.dream.util.ExcelUltils;
import com.backend.dream.util.PdfUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Transactional
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductSizeRepository productSizeRepository;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductSizeService productSizeService;

    @Autowired
    private FeedbackService feedbackService;

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.getReferenceById(id);
        return product != null ? productMapper.productToProductDTO(product) : null;
    }

    @Override
    public Page<ProductDTO> findByNamePaged(String name, Pageable pageable) {
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCase(name, pageable);
        return productPage.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public Product create(ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        return productRepository.save(product);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductDTO(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductDTO> findByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryID(categoryId, pageable);
        return productPage.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::productToProductDTO);
    }

    @Override
    public Page<ProductDTO> sortByPriceAsc(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(categoryId, pageable);
        return productPage.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public Page<ProductDTO> sortByPriceDesc(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceDesc(categoryId, pageable);
        return productPage.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public Page<ProductDTO> findSaleProducts(Pageable pageable) {
        Page<Product> products = productRepository.findSaleProducts(pageable);
        return products.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public double getDiscountedPrice(Long productID, Long categoryID) {
        CategoryDTO categoryDTO = categoryService.getDiscountByCategoryId(categoryID);
        double originalPrice = getOriginalProductPrice(productID);
        if (categoryDTO != null) {
            double discountPercent = categoryDTO.getPercent_discount();
            double discountedPrice = originalPrice - (originalPrice * discountPercent);
            return discountedPrice;
        } else {
            return originalPrice;
        }
    }

    @Override
    public double getOriginalProductPrice(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get().getPrice();
        }
        return 0.0;
    }

    @Override
    public double getProductPriceBySize(Long categoryID, Long sizeId) {
        double originalPrice = getOriginalProductPrice(categoryID);

        // Fetch the size-specific price from the repository
        Optional<ProductSize> productSize = productSizeRepository.findByProductIdAndSizeId(categoryID, sizeId);

        if (productSize.isPresent()) {
            double sizeSpecificPrice = productSize.get().getPrice();
            CategoryDTO categoryDTO = categoryService.getDiscountByCategoryId(categoryID);

            if (categoryDTO != null) {
                double discountPercent = categoryDTO.getPercent_discount();
                double discountedPrice = sizeSpecificPrice - (sizeSpecificPrice * discountPercent);
                return discountedPrice;
            } else {
                return sizeSpecificPrice;
            }
        } else {
            return originalPrice;
        }
    }

    public double getDiscountPercentByCategoryId(Long categoryID) {
        CategoryDTO categoryDTO = categoryService.getDiscountByCategoryId(categoryID);
        if (categoryDTO != null) {
            return categoryDTO.getPercent_discount();
        }
        return 0.0;
    }

    @Override
    public Page<ProductDTO> findByTopRated(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByTopRating(categoryId, pageable);
        return productPage.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public Page<ProductDTO> findByBestSeller(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByBestseller(categoryId, pageable);
        return productPage.map(product -> {
            ProductDTO productDTO = productMapper.productToProductDTO(product);
            productDTO.setAverageRating(feedbackService.getAverageRating(product.getId()));
            return productDTO;
        });
    }

    @Override
    public ByteArrayInputStream getdataProduct() throws IOException {
        List<Product> products = productRepository.findAll();
        ByteArrayInputStream data = ExcelUltils.dataToExcel(products, ExcelUltils.SHEET_NAMEPRODUCT,
                ExcelUltils.HEADER_PRODUCT);
        return data;
    }

    @Override
    public List<ProductDTO> searchProductByName(String name) {
        List<Product> products = productRepository.searchByName(name);
        return products.stream().map(productMapper::productToProductDTO).collect(Collectors.toList());
    }

    @Override
    public Double getMinPrice(Long productId) {
        List<ProductSize> productSizes = productSizeRepository.findAllByProductId(productId);
        if (!productSizes.isEmpty()) {
            return productSizes.stream()
                    .mapToDouble(ProductSize::getPrice)
                    .min()
                    .orElse(0.0);
        }
        return 0.0;
    }

}
