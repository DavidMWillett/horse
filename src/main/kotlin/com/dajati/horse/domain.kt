package com.dajati.horse

class Roster(val employeeList: List<Employee>, val taskList: List<Task>)

class Task(val duty: Duty, val shift: Shift, val employee: Employee?)

class Employee(val name: String, val team: Team, val availability: Availability)

enum class Team {
    PRINCIPALS,
    AML_MDS,
    MPN_CML,
    LYMPHOID,
}

class Availability(val entries: List<List<Boolean>>)

enum class Duty {
    FISH,
    DS,
    LATE_DS,
    SS,
}

enum class Shift {
    MONDAY_AM,
    MONDAY_PM,
    TUESDAY_AM,
    TUESDAY_PM,
    WEDNESDAY_AM,
    WEDNESDAY_PM,
    THURSDAY_AM,
    THURSDAY_PM,
    FRIDAY_AM,
    FRIDAY_PM,
}
