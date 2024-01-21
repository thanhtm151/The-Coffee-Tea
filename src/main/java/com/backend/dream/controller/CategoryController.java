package com.backend.dream.controller;


import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public String listCategories(Model model) {
        List<CategoryDTO> categoryDTOs = categoryService.findAll();
        model.addAttribute("categories", categoryDTOs);
        return "/user/product/products-list";
    }
}
