package com.example.anphuc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.anphuc.repository.AccountDAO;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    AccountDAO accountDAO;

    @GetMapping("")
    public String getLogin() {
        return "login";
    }

}
