/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.webbudget.domain.model.entity.entries;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 04/03/2014
 */
@Entity
@Table(name = "movement_classes")
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true, of = "name")
public class MovementClass extends PersistentEntity {

    @Getter
    @Setter
    @NotEmpty(message = "{movement-class.name}")
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @Column(name = "budget")
    @NotNull(message = "{movement-class.budget}")
    private BigDecimal budget;
    @Getter
    @Setter
    @Column(name = "blocked")
    private boolean blocked;
    @Getter
    @Setter
    @Enumerated
    @NotNull(message = "{movement-class.movement-class-type}")
    @Column(name = "movement_class_type", nullable = false)
    private MovementClassType movementClassType;

    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{movement-class.cost-center}")
    @JoinColumn(name = "id_cost_center", nullable = false)
    private CostCenter costCenter;

    @Getter
    @Setter
    @Transient
    private BigDecimal totalMovements;

    /**
     * 
     */
    public MovementClass() {
        this.budget = BigDecimal.ZERO;
        this.totalMovements = BigDecimal.ZERO;
    }
    
    /**
     * @return se o orcamento desta classe ja estourou ou nao
     */
    public boolean isOverBudget() {
        return this.totalMovements.compareTo(this.budget) >= 0;
    }
    
    /**
     * @return a porcentagem que o consumo desta classe representa no total 
     * do orcamento
     */
    public int budgetCompletionPercentage() {
        
        BigDecimal percentage = BigDecimal.ZERO;
        
        if (this.isOverBudget()) {
            return 100;
        } else if (this.budget != BigDecimal.ZERO) {
            percentage = this.totalMovements.multiply(new BigDecimal(100))
                            .divide(this.budget, 2, RoundingMode.HALF_UP);
        }
        
        return percentage.intValue() > 100 ? 100 : percentage.intValue();
    }
}
