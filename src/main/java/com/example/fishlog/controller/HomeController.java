package com.example.fishlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /** トップは釣果一覧にリダイレクトする。 */
    @GetMapping("/")
    public String home() {
        return "redirect:/catches";
    }
}
