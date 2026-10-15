package com.farmacia.medicamentos.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/medicamentos";
    }

    /** Temporal: fija el "usuario actual" para la auditoría hasta integrar auth-core. */
    @PostMapping("/usuario")
    public String fijarUsuario(@RequestParam String usuario, HttpServletResponse response) {
        Cookie cookie = new Cookie("usuario", usuario.trim());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return "redirect:/medicamentos";
    }
}
