package de.famst;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController
{
    @RequestMapping("/hello")
    public String index()
    {
        return "Greetings from Spring Boot! With changes";
    }

}
