package edu.oosd.restservices.RestApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the REST API Home!";
    }

    @GetMapping("/status")
    public String status() {
        return "API is running successfully!";
    }

    @GetMapping("/routes")
    public String routes() {
        return """
Available routes:
/
 /greeting
 /status
 /routes
""";
    }
}