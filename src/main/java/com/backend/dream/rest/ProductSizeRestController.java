package com.backend.dream.rest;

import com.backend.dream.dto.NotificationDTO;
import com.backend.dream.dto.ProductDTO;
import com.backend.dream.dto.ProductSizeDTO;
import com.backend.dream.entity.Product;
import com.backend.dream.entity.ProductSize;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.NotificationService;
import com.backend.dream.service.ProductService;
import com.backend.dream.repository.ProductSizeRepository;
import com.backend.dream.service.ProductSizeService;
import jakarta.servlet.http.HttpServletRequest;
import com.backend.dream.util.ValidationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.backend.dream.util.ExcelUltils;
import com.backend.dream.util.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RequestMapping("/rest/productsizes")
@RestController
public class ProductSizeRestController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProductSizeService productSizeService;
    @Autowired
    private ValidationService validateService;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @GetMapping()
    public List<ProductSizeDTO> getAllSizes() {
        return productSizeService.findAll();
    }

    @PostMapping()
    public ProductSize create(@RequestBody ProductSizeDTO productSizeDTO, HttpServletRequest request) {
        String username = request.getRemoteUser();
        Long idAccount = accountService.findIDByUsername(username);
        Long idRole = accountService.findRoleIdByUsername(username);

        if (idRole == 1 || idRole == 2) {
            ProductSize createdProductSize = productSizeService.create(productSizeDTO);

            String productName = productSizeService.getProductNameByProductSizeID(productSizeDTO.getId_product());

            String notificationTitle = "Có sự thay đổi giá sản phẩm";
            String notificationText = "Giá của sản phẩm '" + productName + "' đã được thêm bởi '" + username + "'";

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setIdAccount(idAccount);
            notificationDTO.setNotificationTitle(notificationTitle);
            notificationDTO.setNotificationText(notificationText);
            notificationDTO.setId_role(idRole);
            notificationDTO.setImage("size-change.jpg");
            notificationDTO.setCreatedTime(Timestamp.from(Instant.now()));
            notificationService.createNotification(notificationDTO);

            return createdProductSize;
        }

        return null;
    }

    @PutMapping("{id}")
    public ProductSizeDTO update(@RequestBody ProductSizeDTO productSizeDTO, @PathVariable("id") Long id,
            HttpServletRequest request) {
        String username = request.getRemoteUser();
        Long idAccount = accountService.findIDByUsername(username);
        Long idRole = accountService.findRoleIdByUsername(username);

        if (idRole == 1 || idRole == 2) {
            ProductSizeDTO updatedProductSize = productSizeService.update(productSizeDTO, id);

            String productName = productSizeService.getProductNameByProductSizeID(id);
            String notificationTitle = "Có sự thay đổi giá sản phẩm";
            String notificationText = "Giá của sản phẩm '" + productName + "' đã được cập nhật bởi '" + username + "'";

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setIdAccount(idAccount);
            notificationDTO.setNotificationTitle(notificationTitle);
            notificationDTO.setNotificationText(notificationText);
            notificationDTO.setId_role(idRole);
            notificationDTO.setImage("size-change.jpg");
            notificationDTO.setCreatedTime(Timestamp.from(Instant.now()));
            notificationService.createNotification(notificationDTO);

            return updatedProductSize;
        }

        return null;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) {
        String username = request.getRemoteUser();
        Long idAccount = accountService.findIDByUsername(username);
        Long idRole = accountService.findRoleIdByUsername(username);

        if (idRole == 1 || idRole == 2) {
            String productName = productSizeService.getProductNameByProductSizeID(id);
            if (productName != null) {
                String notificationTitle = "Có sự thay đổi trong sản phẩm";
                String notificationText = "Một giá của sản phẩm '" + productName + "' đã được xóa bởi '" + username
                        + "'";

                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setIdAccount(idAccount);
                notificationDTO.setNotificationTitle(notificationTitle);
                notificationDTO.setNotificationText(notificationText);
                notificationDTO.setId_role(idRole);
                notificationDTO.setImage("size-change.jpg");
                notificationDTO.setCreatedTime(Timestamp.from(Instant.now()));
                notificationService.createNotification(notificationDTO);
                productSizeService.delete(id);
            }
        }
    }

    @GetMapping("/search")
    public List<ProductDTO> searchByProductIdAndSizeId(@RequestParam String name) {
        return productService.searchProductByName(name);
    }

    @GetMapping("/download")
    private ResponseEntity<InputStreamResource> download() throws IOException {
        String fileName = "Data-productSizes.xlsx";
        ByteArrayInputStream inputStream = productSizeService.getdataProductSize();
        InputStreamResource response = new InputStreamResource(inputStream);

        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(response);
        return responseEntity;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf() {
        try {
            List<ProductSize> productSizes = productSizeRepository.findAll();
            String title = "Data Product Size";
            ByteArrayInputStream pdfStream = PdfUtils.dataToPdf(productSizes, ExcelUltils.HEADERPRODUCTSIZE, title);

            byte[] pdfContents = new byte[pdfStream.available()];
            pdfStream.read(pdfContents);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Data-productSize.pdf");
            headers.setCacheControl("must-revalidate, no-store");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
