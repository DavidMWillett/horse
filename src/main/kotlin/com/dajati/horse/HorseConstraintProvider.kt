package com.dajati.horse

import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider
import org.optaplanner.core.api.score.stream.Joiners

class HorseConstraintProvider : ConstraintProvider {
    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            simultaneousTasks(constraintFactory),
            employeeNotAvailable(constraintFactory),
        )
    }

    private fun simultaneousTasks(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .join(
                Task::class.java,
                Joiners.equal(Task::shift),
                Joiners.equal(Task::employee),
                Joiners.lessThan(Task::id)
            )
            .penalize("Employee assigned to multiple simultaneous tasks", HardMediumSoftScore.ONE_HARD)
    }

    private fun employeeNotAvailable(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .filter { task -> !task.employee!!.availability.entries[task.shift!!.ordinal][task.duty!!.ordinal] }
            .penalize("Employee not available for shift/duty", HardMediumSoftScore.ONE_HARD)
    }
}
