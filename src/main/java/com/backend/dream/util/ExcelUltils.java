package com.backend.dream.util;

import com.backend.dream.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class ExcelUltils {

    public static String[] HEADER_PRODUCT = {"Id", "Name", "Image", "Describe", "Create Date", "Active", "Category"};
    public static String SHEET_NAMEPRODUCT = "sheetForProductData";
    public static String HEADERPRODUCTSIZE[] = {"Id", "Name", "Size", "Price", "Category"};
    public static String SHEET_NAMEPRODUCTSIZE = "sheetForProductSizeData";
    public static String HEADERDISCOUNT[] = {"Id", "Name", "Number", "Precent", "Active", "Actice Date", "Expired Date", "Category"};
    public static String SHEET_NAMEDISCOUNT = "sheetForDiscountData";
    public static String HEADERVOUCHER[] = {"Id", "Name", "Number", "Precent","Condition", "Voucher Status", "Actice Date", "Expired Date","Icon", "Name Account"};
    public static String SHEET_NAMEVOUCHER = "sheetForVoucherData";
    public static String HEADERSTAFF[] = {"Id", "Avatar", "Username","Firstname","Lastname","Fullname","Email","Phone","Address"};
    public static String SHEET_NAMESTAFF = "sheetForStaffData";
    public static String HEADERAUTHORITY[] = {"Id", "Name Account", "Role"};
    public static String SHEET_NAMEAUTHORITY = "sheetForAuthorityData";
    public static String HEADER_ORDER[] = {"Id", "Name Account", "Address","TotalAmount","Create Date","Create Time","Status"};
    public static String SHEET_ORDER = "sheetForOrderData";
    public static ByteArrayInputStream dataToExcel(List<?> dataList, String sheetName, String[] headers) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);
            Row row = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for (Object obj : dataList) {
                Row rowData = sheet.createRow(rowIndex++);
                fillRowWithData(rowData, obj);
            }

            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }
    }

    private static void fillRowWithData(Row row, Object obj) {
        // Logic to fill data based on the type of object (e.g., Product, ProductSize, etc.)
        // Example:
        if (obj instanceof Product) {
            Product product = (Product) obj;
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getImage());
            row.createCell(3).setCellValue(product.getDescribe());
            row.createCell(4).setCellValue(product.getCreateDate());
            row.createCell(5).setCellValue(product.getActive());
            row.createCell(6).setCellValue(product.getCategory().getName());
        } else if (obj instanceof ProductSize) {
            ProductSize productSize = (ProductSize) obj;
            row.createCell(0).setCellValue(productSize.getId());
            row.createCell(1).setCellValue(productSize.getProduct().getName());
            row.createCell(2).setCellValue(productSize.getSize().getName());
            Cell cell = row.createCell(3);
            cell.setCellValue(productSize.getPrice());
            CellStyle style = row.getSheet().getWorkbook().createCellStyle();
            DataFormat format = row.getSheet().getWorkbook().createDataFormat();
            style.setDataFormat(format.getFormat("#,##0")); // Định dạng hiển thị số với phân cách ngàn, không có số thập phân
            cell.setCellStyle(style);
            row.createCell(4).setCellValue(productSize.getProduct().getCategory().getName());
        } else if (obj instanceof Discount) {
            Discount discount = (Discount) obj;
            row.createCell(0).setCellValue(discount.getId());
            row.createCell(1).setCellValue(discount.getName());
            row.createCell(2).setCellValue(discount.getNumber());
            row.createCell(3).setCellValue(discount.getPercent());
            row.createCell(4).setCellValue(discount.isActive());
            Cell cellDateActive = row.createCell(5);
            cellDateActive.setCellValue(discount.getActiveDate());

            // Định dạng ngày tháng trong Excel
            CellStyle dateCellActiveStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper createActiveHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateCellActiveStyle.setDataFormat(createActiveHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            cellDateActive.setCellStyle(dateCellActiveStyle);
            Cell cellDateExpired = row.createCell(6);
            cellDateExpired.setCellValue(discount.getExpiredDate());

            // Định dạng ngày tháng trong Excel
            CellStyle dateCellExpiredStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper createExpiredHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateCellExpiredStyle.setDataFormat(createExpiredHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            cellDateExpired.setCellStyle(dateCellExpiredStyle);
//            row.createCell(7).setCellValue(discount.getCategory().getName());
        } else if (obj instanceof Voucher) {
            Voucher voucher = (Voucher) obj;
            row.createCell(0).setCellValue(voucher.getId());
            row.createCell(1).setCellValue(voucher.getName());
            row.createCell(2).setCellValue(voucher.getNumber());
            row.createCell(3).setCellValue(voucher.getPrice());
            row.createCell(4).setCellValue(voucher.getCondition());
            row.createCell(5).setCellValue(voucher.getStatus().getName());
            Cell cellDateActive = row.createCell(6);
            cellDateActive.setCellValue(voucher.getCreateDate());

            // Định dạng ngày tháng trong Excel
            CellStyle dateCellActiveStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper createActiveHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateCellActiveStyle.setDataFormat(createActiveHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            cellDateActive.setCellStyle(dateCellActiveStyle);
            Cell cellDateExpired = row.createCell(7);
            cellDateExpired.setCellValue(voucher.getExpiredDate());

            // Định dạng ngày tháng trong Excel
            CellStyle dateCellExpiredStyle = row.getSheet().getWorkbook().createCellStyle();
            CreationHelper createExpiredHelper = row.getSheet().getWorkbook().getCreationHelper();
            dateCellExpiredStyle.setDataFormat(createExpiredHelper.createDataFormat().getFormat("dd-MM-yyyy"));
            cellDateExpired.setCellStyle(dateCellExpiredStyle);
            row.createCell(8).setCellValue(voucher.getIcon());
            row.createCell(9).setCellValue(voucher.getAccount().getFullname());
        } else if (obj instanceof Account) {
            Account account = (Account) obj;
            row.createCell(0).setCellValue(account.getId());
            row.createCell(1).setCellValue(account.getAvatar());
            row.createCell(2).setCellValue(account.getUsername());
            row.createCell(3).setCellValue(account.getFirstname());
            row.createCell(4).setCellValue(account.getLastname());
            row.createCell(5).setCellValue(account.getFullname());
            row.createCell(6).setCellValue(account.getEmail());
            row.createCell(7).setCellValue(account.getPhone());
            row.createCell(8).setCellValue(account.getAddress());
        } else if (obj instanceof Authority) {
            Authority authority = (Authority) obj;
            row.createCell(0).setCellValue(authority.getId());
            row.createCell(1).setCellValue(authority.getAccount().getFullname());
            row.createCell(2).setCellValue(authority.getRole().getName());
        } else if (obj instanceof Orders) {
            Orders orders = (Orders) obj;
            row.createCell(0).setCellValue(orders.getId());
            row.createCell(1).setCellValue(orders.getAccount().getFullname());
            row.createCell(2).setCellValue(orders.getAddress());
            row.createCell(3).setCellValue(orders.getTotalAmount());
            row.createCell(4).setCellValue(orders.getCreateDate());
            row.createCell(5).setCellValue(orders.getCreateTime());
            row.createCell(6).setCellValue(orders.getStatus().getName());
        }
        // Add more conditions based on different object types
    }

    // Add methods for different data types as needed
}

