package com.example.anphuc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.anphuc.repository.AccountRepository;

@RequestMapping("/login")
public class LoginController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("")
    public String getLogin() {
        return "site/login";
    }

}
