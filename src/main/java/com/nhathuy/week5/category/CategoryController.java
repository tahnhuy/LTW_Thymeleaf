package com.nhathuy.week5.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    @ResponseBody
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size) {
        System.out.println("[WEB] GET /admin/categories q=" + q + ", page=" + page + ", size=" + size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("categoryId").descending());
        Page<Category> result = service.search(q, pageable);
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Categories</title></head><body>");
        html.append("<h1>Categories (" + result.getTotalElements() + ")</h1>");
        html.append("<a href='/admin/categories/create'>Create New</a><br><br>");
        
        html.append("<table border='1'><tr><th>ID</th><th>Category Name</th><th>Icon</th><th>Created At</th><th>Actions</th></tr>");
        for (Category c : result.getContent()) {
            html.append("<tr>");
            html.append("<td>").append(c.getId()).append("</td>");
            html.append("<td>").append(c.getCategoryName() != null ? c.getCategoryName() : "").append("</td>");
            html.append("<td>").append(c.getIcon() != null ? c.getIcon() : "No icon").append("</td>");
            html.append("<td>").append(c.getCreatedAt() != null ? c.getCreatedAt().toString() : "").append("</td>");
            html.append("<td>");
            html.append("<a href='/admin/categories/").append(c.getId()).append("/edit'>Edit</a> | ");
            html.append("<form style='display:inline' method='post' action='/admin/categories/").append(c.getId()).append("/delete'>");
            html.append("<button type='submit' onclick='return confirm(\"Delete?\")'>Delete</button>");
            html.append("</form>");
            html.append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        
        // Pagination
        if (result.hasPrevious()) {
            html.append("<a href='/admin/categories?page=").append(result.getNumber() - 1).append("'>Previous</a> ");
        }
        html.append("Page ").append(result.getNumber() + 1).append(" of ").append(result.getTotalPages());
        if (result.hasNext()) {
            html.append(" <a href='/admin/categories?page=").append(result.getNumber() + 1).append("'>Next</a>");
        }
        
        html.append("</body></html>");
        return html.toString();
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Category c = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("category", c);
        return "category/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("category") Category category,
                       BindingResult binding,
                       RedirectAttributes ra) {
        if (binding.hasErrors()) return "category/form";
        service.save(category);
        ra.addFlashAttribute("msg", "Lưu Category thành công");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("msg", "Đã xóa Category");
        return "redirect:/admin/categories";
    }
}
