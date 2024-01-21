package com.backend.dream.rest;

import com.backend.dream.dto.SizeDTO;
import com.backend.dream.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SizeRestController {
    @Autowired
    private SizeService sizeService;
    @GetMapping("/rest/size")
    public List<SizeDTO> getAll() throws Exception{
        return sizeService.findAll();
    }
}
