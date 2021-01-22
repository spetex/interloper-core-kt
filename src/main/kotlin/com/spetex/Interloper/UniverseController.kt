package com.spetex.Interloper

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UniverseController {
    @GetMapping("/")
    public fun root(): String {
        return "Hello, World!"
    }
}