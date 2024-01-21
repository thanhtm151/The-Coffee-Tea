package com.backend.dream.rest;

import com.backend.dream.dto.OrderDTO;
import com.backend.dream.dto.OrderStatusDTO;
import com.backend.dream.entity.Orders;
import com.backend.dream.entity.Product;
import com.backend.dream.repository.OrderRepository;
import com.backend.dream.service.AccountService;
import com.backend.dream.service.OrderService;
import com.backend.dream.service.OrderStatusService;
import com.backend.dream.util.ValidationService;
import com.backend.dream.util.ExcelUltils;
import com.backend.dream.util.PdfUtils;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ValidationService validateService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid JsonNode orderData, BindingResult bindingResult)
            throws ParseException {
        if (bindingResult.hasErrors()) {
            validateService.validation(bindingResult);
            return ResponseEntity.badRequest().body(validateService.validation(bindingResult));
        }

        return ResponseEntity.ok(orderService.create(orderData));
    }

    @GetMapping("/address")
    @ResponseBody
    public String getUserAddress(HttpServletRequest request) {
        String address = accountService.getAddressByUsername(request.getRemoteUser());
        return "{\"address\": \"" + address + "\"}";
    }

    @GetMapping("/status")
    public List<OrderStatusDTO> getAll() {
        return orderStatusService.getAll();
    }

    @GetMapping()
    public List<OrderDTO> getListOrder() throws ClassNotFoundException {
        List<OrderDTO> listOrder = orderService.getListOrder();
        Collections.sort(listOrder, Comparator.comparing(OrderDTO::getCreateDate));
        return listOrder;
    }

    @GetMapping("/confirm")
    public List<OrderDTO> getListOrderConfirm() throws ClassNotFoundException {
        List<OrderDTO> listOrderConfirm = orderService.getListOrderConfirm();
        Collections.sort(listOrderConfirm, Comparator.comparing(OrderDTO::getCreateDate));
        return listOrderConfirm;
    }

    @GetMapping("/ship")
    public List<OrderDTO> getListOrderIsShipping() throws ClassNotFoundException {
        List<OrderDTO> listOrderIsShipping = orderService.getListOrderIsShipping();
        Collections.sort(listOrderIsShipping, Comparator.comparing(OrderDTO::getCreateDate));
        return listOrderIsShipping;
    }

    @GetMapping("/success")
    public List<OrderDTO> getListOrderSuccess() throws ClassNotFoundException {
        List<OrderDTO> listOrderSuccess = orderService.getListOrderSuccess();
        Collections.sort(listOrderSuccess, Comparator.comparing(OrderDTO::getCreateDate));
        return listOrderSuccess;
    }

    @GetMapping("/cancel")
    public List<OrderDTO> getListOrderCancel() throws ClassNotFoundException {
        List<OrderDTO> listOrderCancel = orderService.getListOrderCancel();
        Collections.sort(listOrderCancel, Comparator.comparing(OrderDTO::getCreateDate));
        return listOrderCancel;
    }

    @PutMapping("/{id}")
    public OrderDTO changeToConfirmStatus(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO)
            throws ClassNotFoundException {
        return orderService.updateOrder(orderDTO);
    }

    @PutMapping("/ship/{id}")
    public OrderDTO changeOrderShipStatus(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO)
            throws ClassNotFoundException {
        return orderService.updateOrder(orderDTO);
    }

    @PutMapping("/success/{id}")
    public OrderDTO changeOrderSuccessStatus(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO)
            throws ClassNotFoundException {
        return orderService.updateOrder(orderDTO);
    }

    @PutMapping("/cancel/{id}")
    public OrderDTO changeOrderCancelStatus(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO)
            throws ClassNotFoundException {
        return orderService.updateOrder(orderDTO);
    }

    @PutMapping("/reset/{id}")
    public OrderDTO resetOrderStatus(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO)
            throws ClassNotFoundException {
        return orderService.updateOrder(orderDTO);
    }

    @GetMapping("/searchByStatusAndUsername")
    public List<OrderDTO> searchByStatusAndUsername(
            @RequestParam(value = "statusID") Long statusID,
            @RequestParam(value = "username") String username) {
        List<OrderDTO> searchedOrders = orderService.searchOrders(username, statusID);
        return searchedOrders;
    }

    @GetMapping("/download")
    private ResponseEntity<InputStreamResource> download() throws IOException {
        String fileName = "Data-Orders.xlsx";
        ByteArrayInputStream inputStream = orderService.getdataOrder();
        InputStreamResource response = new InputStreamResource(inputStream);

        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(response);
        return responseEntity;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf() {
        try {
            List<Orders> orders = orderRepository.findAll();
            String title = "Data Orders";
            ByteArrayInputStream pdfStream = PdfUtils.dataToPdf(orders, ExcelUltils.HEADER_ORDER, title);

            // Chuyển đổi ByteArrayInputStream sang byte array
            byte[] pdfContents = new byte[pdfStream.available()];
            pdfStream.read(pdfContents);

            // Đặt headers để trình duyệt hiểu được định dạng của file PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Data-Orders.pdf");
            headers.setCacheControl("must-revalidate, no-store");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
