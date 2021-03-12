package com.dajati.horse

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.entity.PlanningPin
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
    val employee: Employee? = null,
) {
    val id = nextId++
    @PlanningPin
    val pinned = employee != null

    val dayOfWeek: Int
        get() = shift!!.ordinal / 2

    companion object {
        var nextId = 1
    }
}

data class Employee(
    val name: String,
    val team: Team,
    val statuses: Array<Status>,
    val availability: Availability,
    val priorShifts: Int,
    val priorTasks: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Employee
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun canPerform(task: Task): Boolean {
        val shift = task.shift!!
        val duty = task.duty!!
        return statuses[shift.ordinal] == Status.AT_WORK && availability[shift, duty]
    }
}

data class Availability(val entries: List<List<Boolean>>) {
    operator fun get(shift: Shift, duty: Duty): Boolean {
        return entries[shift.ordinal][duty.ordinal]
    }
}

enum class Team {
    PRINCIPALS,
    AML_MDS,
    MPN_CML,
    LYMPHOID,
}

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

enum class Status {
    AT_WORK,
    WORKING_FROM_HOME,
    ANNUAL_LEAVE,
    DOES_NOT_WORK,
}