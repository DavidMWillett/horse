package com.dajati.horse

import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore
import org.optaplanner.core.api.score.stream.*

class HorseConstraintProvider : ConstraintProvider {
    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            simultaneousTasks(constraintFactory),
            employeeNotAvailable(constraintFactory),
            maxOneTaskPerDay(constraintFactory),
            fullDayFISH(constraintFactory),
            maxThreeTasksPerWeek(constraintFactory),
            excludePrincipals(constraintFactory),
            shareTasksFairly(constraintFactory),
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
            .filter { task -> !task.employee!!.availability[task.shift!!, task.duty!!] }
            .penalize("Employee not available for shift/duty", HardMediumSoftScore.ONE_HARD)
    }

    private fun maxOneTaskPerDay(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .join(
                Task::class.java,
                Joiners.lessThan(Task::id),
                Joiners.equal(Task::employee),
                Joiners.filtering { t1, t2 -> t1.duty != Duty.FISH || t2.duty != Duty.FISH })
            .filter { t1, t2 -> t1.dayOfWeek == t2.dayOfWeek }
            .penalize("Two tasks on same day (unless both FISH)", HardMediumSoftScore.ONE_MEDIUM)
    }

    private fun fullDayFISH(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .join(
                Task::class.java,
                Joiners.lessThan(Task::id),
                Joiners.equal(Task::employee),
                Joiners.filtering { t1, t2 -> t1.duty == Duty.FISH && t2.duty == Duty.FISH })
            .filter { t1, t2 -> t1.dayOfWeek == t2.dayOfWeek }
            .reward("FISH tasks on same day", HardMediumSoftScore.ONE_MEDIUM)
    }

    private fun maxThreeTasksPerWeek(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .groupBy(Task::employee, ConstraintCollectors.count())
            .filter { _, count -> count > 3 }
            .penalize("Employee with more than three tasks", HardMediumSoftScore.ONE_MEDIUM)
    }

    private fun excludePrincipals(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .filter { task -> task.employee!!.team == Team.PRINCIPALS }
            .penalize("Use of principals", HardMediumSoftScore.ONE_SOFT) { 100 }
    }

    private fun shareTasksFairly(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory.from(Task::class.java)
            .groupBy(Task::employee, ConstraintCollectors.count())
            .penalize("Tasks shared unfairly between employees",
                HardMediumSoftScore.ONE_SOFT
            ) { _, taskCount -> taskCount * taskCount }
    }
}
