package com.dajati.horse

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HorseController {
    @PostMapping("/solve")
    fun solve(): Roster {
        return Roster();
    }
}
