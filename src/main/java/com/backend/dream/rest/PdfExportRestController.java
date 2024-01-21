package com.backend.dream.rest;

import com.backend.dream.dto.ProductDTO;
import com.backend.dream.service.ProductService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/export")
public class PdfExportRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<ProductDTO> data = productService.findAll();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=data.pdf");

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document doc = new Document(pdfDoc);
        Paragraph title = new Paragraph("Danh sách sản phẩm");

        // Thêm tiêu đề vào document
        doc.add(title);
        Table table = new Table(8);
        // Thêm header vào table
        table.addCell("Number");
        table.addCell("ID");
        table.addCell("Image");
        table.addCell("Name");
        table.addCell("Describe");
        table.addCell("Category");
        table.addCell("Date");
        table.addCell("Active");

        for (ProductDTO item : data) {
            table.addCell(String.valueOf(data.indexOf(item) + 1));
            table.addCell(String.valueOf(item.getId()));
            table.addCell(item.getImage());
            table.addCell(item.getName());
            table.addCell(item.getDescribe());
            table.addCell(item.getName_category());
            table.addCell(String.valueOf(item.getCreateDate()));
            table.addCell(String.valueOf(item.getActive()));
        }
        doc.add(table);

        doc.close();
        pdfDoc.close();
    }
}

