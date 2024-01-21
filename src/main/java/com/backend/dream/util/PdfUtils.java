package com.backend.dream.util;

import com.backend.dream.entity.*;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.persistence.criteria.Order;

import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfUtils {

    public static ByteArrayInputStream dataToPdf(List<?> dataList, String[] headers, String tableTitle) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(byteArrayOutputStream));
            Document doc = new Document(pdfDoc);
            Paragraph title = new Paragraph(tableTitle).setFontSize(30).setTextAlignment(TextAlignment.CENTER);
            doc.add(title);

            Table table = new Table(headers.length);

            for (String header : headers) {
                table.addCell(header);
            }

            for (Object obj : dataList) {
                fillRowWithData(table, obj);
            }

            doc.add(table);

            doc.close();
            pdfDoc.close();

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }
    }

    private static void fillRowWithData(Table table, Object obj) {
        if (obj instanceof Product) {
            Product product = (Product) obj;
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getImage());
            table.addCell(product.getDescribe());
            table.addCell(String.valueOf(product.getCreateDate()));
            table.addCell(String.valueOf(product.getActive()));
            table.addCell(product.getCategory().getName());
        } else if (obj instanceof ProductSize) {
            ProductSize productSize = (ProductSize) obj;
            table.addCell(String.valueOf(productSize.getId()));
            table.addCell(productSize.getProduct().getName());
            table.addCell(productSize.getSize().getName());
            table.addCell(String.valueOf(productSize.getPrice()));
            table.addCell(String.valueOf(productSize.getProduct().getCategory().getName()));
        } else if (obj instanceof Discount) {
            Discount discount = (Discount) obj;
            table.addCell(String.valueOf(discount.getId()));
            table.addCell(discount.getName());
            table.addCell(discount.getNumber());
            table.addCell(String.valueOf(discount.getPercent()));
            table.addCell(String.valueOf(discount.isActive()));
            table.addCell(String.valueOf(discount.getActiveDate()));
            table.addCell(String.valueOf(discount.getExpiredDate()));
//            table.addCell(discount.getCategory().getName());
        } else if (obj instanceof Voucher) {
            Voucher voucher = (Voucher) obj;
            table.addCell(String.valueOf(voucher.getId()));
            table.addCell(voucher.getName());
            table.addCell(voucher.getNumber());
            table.addCell(String.valueOf(voucher.getPrice()));
            table.addCell(String.valueOf(voucher.getCondition()));
            table.addCell(voucher.getStatus().getName());
            table.addCell(String.valueOf(voucher.getCreateDate()));
            table.addCell(String.valueOf(voucher.getExpiredDate()));
            table.addCell(voucher.getIcon());
            table.addCell(voucher.getAccount().getFullname());
        } else if (obj instanceof Account) {
            Account account = (Account) obj;
            table.addCell(String.valueOf(account.getId()));
            table.addCell(account.getAvatar());
            table.addCell(account.getUsername());
            table.addCell(account.getFirstname());
            table.addCell(account.getLastname());
            table.addCell(account.getFullname());
            table.addCell(account.getEmail());
            table.addCell(account.getPhone());
            table.addCell(account.getAddress());
        } else if (obj instanceof Authority) {
            Authority authority = (Authority) obj;
            table.addCell(String.valueOf(authority.getId()));
            table.addCell(authority.getAccount().getFullname());
            table.addCell(authority.getRole().getName());
        } else if (obj instanceof Orders) {
            Orders orders = (Orders) obj;
            table.addCell(String.valueOf(orders.getId()));
            table.addCell(orders.getAccount().getFullname());
            table.addCell(orders.getAddress());
            table.addCell(String.valueOf(orders.getTotalAmount()));
            table.addCell(String.valueOf(orders.getCreateDate()));
            table.addCell(String.valueOf(orders.getCreateTime()));
            table.addCell(orders.getStatus().getName());
        }

    }
}
