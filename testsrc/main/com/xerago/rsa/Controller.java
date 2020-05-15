package com.xerago.rsa;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@org.springframework.stereotype.Controller
public class Controller {
	
	@RequestMapping("/")
    public String home() {
        return "Welcome to Two Wheeler - GetQuote MicroServices";
    }
}
