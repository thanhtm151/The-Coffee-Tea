package com.backend.dream.rest;

import com.backend.dream.dto.CategoryDTO;
import com.backend.dream.service.CategoryService;
import com.backend.dream.util.ErrorResponse;
import com.backend.dream.util.ValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/rest/category")
@RestController
public class CategoryRestController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ValidationService validateService;

    @GetMapping()
    public List<CategoryDTO> getAll() throws Exception {
        return categoryService.findAll();
    }


    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            validateService.validation(bindingResult);
            return ResponseEntity.badRequest().body(validateService.validation(bindingResult));
        }

        return ResponseEntity.ok(categoryService.create(categoryDTO));
    }

    @PutMapping("/update/{id}")
    public CategoryDTO update(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") Long id) {
        return categoryService.update(categoryDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }

}
