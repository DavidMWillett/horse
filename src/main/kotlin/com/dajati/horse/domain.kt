package com.dajati.horse

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.domain.variable.PlanningVariable
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore

@PlanningSolution
class Roster(
    @PlanningEntityCollectionProperty
    val taskList: List<Task>? = null,
    @ValueRangeProvider(id = "employeeRange")
    val employeeList: List<Employee>? = null,
) {
    @PlanningScore
    lateinit var score: HardMediumSoftScore
}

@PlanningEntity
class Task(
    val duty: Duty? = null,
    val shift: Shift? = null,
    @PlanningVariable(valueRangeProviderRefs = ["employeeRange"])
    val employee: Employee? = null
) {
    val id = nextId++

    companion object {
        var nextId = 1
    }
}

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
