package com.practica.practica;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping("/biblioteca/gestion")
public class PracticaController {

    @GetMapping("/hello")
    public String hello() {
        return "jijis";
    }
}
