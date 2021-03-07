package com.dajati.horse

import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider

class HorseConstraintProvider : ConstraintProvider{
    override fun defineConstraints(constraintFactory: ConstraintFactory?): Array<Constraint> {
        return arrayOf()
    }
}
