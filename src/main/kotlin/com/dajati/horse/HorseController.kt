package com.dajati.horse

import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class HorseController {
    @PostMapping("/solve")
    fun solve(@RequestBody problem: Roster): Roster {
        val employeeList = problem.employeeList
        val taskList = problem.taskList
        return Roster(employeeList, taskList)
    }
}
