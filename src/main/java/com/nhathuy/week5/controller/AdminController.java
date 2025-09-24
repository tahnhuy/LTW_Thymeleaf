package com.nhathuy.week5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("pageTitle", "Dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/ajax-demo")
    public String ajaxDemo(Model model) {
        model.addAttribute("title", "AJAX Demo - Admin");
        model.addAttribute("pageTitle", "AJAX Demo");
        return "admin/ajax-demo";
    }
}
