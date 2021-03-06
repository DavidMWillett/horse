package com.dajati.horse

class Roster(val employeeList: List<Employee>, val taskList: List<Task>)

class Employee(val name: String, val team: Team)

enum class Team {
    PRINCIPALS,
    AML_MDS,
    MPN_CML,
    LYMPHOID;
}

class Task
