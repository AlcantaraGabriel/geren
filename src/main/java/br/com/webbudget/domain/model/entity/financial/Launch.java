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

import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.entity.PersistentEntity;
import br.com.webbudget.infraestructure.configuration.ApplicationUtils;
import java.time.LocalDate;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representacao do lancamento de um determinado movimento fixo na lista de 
 * movimentos de um periodo, esta entidade serve como ligacao entre o estado
 * onde o movimento fixo vira um movimento efetivamente
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 21/09/2015
 */
@Entity
@Table(name = "launches")
@ToString(callSuper = true, of = "code")
@EqualsAndHashCode(callSuper = true, of = "code")
public class Launch extends PersistentEntity {
    
    @Getter
    @Column(name = "code", nullable = false, length = 8, unique = true)
    private final String code;
    @Getter
    @Setter
    @Column(name = "quote")
    private Integer quote;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_financial_period", nullable = false)
    private FinancialPeriod financialPeriod;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_fixed_movement", nullable = false)
    private FixedMovement fixedMovement;
    @Getter
    @Setter
    @OneToOne(cascade = REMOVE)
    @JoinColumn(name = "id_movement", nullable = false)
    private Movement movement;
    
    /**
     * Inicializamos o que for necessario
     */
    public Launch() {
        this.code = ApplicationUtils.createRamdomCode(5, false);
    }

    /**
     * @param period o periodo que devemos checar
     * @return se pertence ou nao ao periodo
     */
    public boolean belongsToPeriod(FinancialPeriod period) {
        return this.financialPeriod.equals(period);
    }
    
    /**
     * @return a data de incio do movimento fixo
     */
    public LocalDate getStartDate() {
        return this.fixedMovement.getStartDate() == null 
                ? LocalDate.now() : this.fixedMovement.getStartDate();
    }
}
