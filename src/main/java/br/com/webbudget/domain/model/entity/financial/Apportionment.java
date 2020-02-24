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
package br.com.webbudget.domain.model.entity.financial;

import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.entity.entries.CostCenter;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.entries.MovementClassType;
import br.com.webbudget.domain.model.entity.PersistentEntity;
import br.com.webbudget.infraestructure.configuration.ApplicationUtils;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 02/02/2015
 */
@Entity
@Table(name = "apportionments")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Apportionment extends PersistentEntity {

    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private String code;
    @Getter
    @Setter
    @NotNull(message = "{apportionment.value}")
    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_movement")
    private Movement movement;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_fixed_movement")
    private FixedMovement fixedMovement;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{apportionment.cost-center}")
    @JoinColumn(name = "id_cost_center", nullable = false)
    private CostCenter costCenter;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{apportionment.movement-class}")
    @JoinColumn(name = "id_movement_class", nullable = false)
    private MovementClass movementClass;

    /**
     *
     */
    public Apportionment() {
        this.code = ApplicationUtils.createRamdomCode(5, false);
    }
    
    /**
     * 
     * @param costCenter
     * @param movementClass
     * @param value 
     */
    public Apportionment(CostCenter costCenter, MovementClass movementClass, BigDecimal value) {
        this();
        if (!movementClass.getCostCenter().equals(costCenter)) {
            throw new InternalServiceError("error.apportionment.invalid-class-for-cc");
        } else {
            this.value = value;
            this.costCenter = costCenter;
            this.movementClass = movementClass;
        }
    }

    /**
     * @return se este e um rateio de receita
     */
    public boolean isForRevenues() {
        return this.movementClass.getMovementClassType() == MovementClassType.IN;
    }
    
    /**
     * @return se este e um rateio de despesa
     */
    public boolean isForExpenses() {
        return this.movementClass.getMovementClassType() == MovementClassType.OUT;
    }
    
    /**
     * @return uma copia deste reateio com um novo codigo
     */
    public Apportionment copy() {
        
        final Apportionment apportionment = new Apportionment();
        
        apportionment.setValue(this.value);
        apportionment.setCostCenter(this.costCenter);
        apportionment.setMovementClass(this.movementClass);
        
        return apportionment;
    }
}
