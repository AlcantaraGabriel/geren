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
package br.com.webbudget.domain.model.repository.financial;

import br.com.webbudget.domain.model.entity.financial.Apportionment;
import br.com.webbudget.domain.model.entity.miscellany.FinancialPeriod;
import br.com.webbudget.domain.model.entity.financial.FixedMovement;
import br.com.webbudget.domain.model.entity.financial.Movement;
import br.com.webbudget.domain.model.entity.entries.MovementClass;
import br.com.webbudget.domain.model.entity.financial.MovementStateType;
import br.com.webbudget.domain.model.repository.GenericRepository;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 1.0.0, 22/04/2014
 */
public class ApportionmentRepository extends GenericRepository<Apportionment, Long> implements IApportionmentRepository {

    /**
     *
     * @param movementClass
     * @return
     */
    @Override
    public BigDecimal totalMovementsPerClass(MovementClass movementClass) {

        final Criteria criteria = this.createCriteria();

        criteria.setProjection(Projections.sum("value"));
        criteria.add(Restrictions.eq("movementClass", movementClass));

        return (BigDecimal) criteria.uniqueResult();
    }

    /**
     *
     * @param movement
     * @return
     */
    @Override
    public List<Apportionment> listByMovement(Movement movement) {

        final Criteria criteria = this.getSession().createCriteria(this.getPersistentClass());

        criteria.createAlias("movement", "mv");
        criteria.add(Restrictions.eq("mv.id", movement.getId()));

        return criteria.list();
    }

    /**
     *
     * @param fixedMovement
     * @return
     */
    @Override
    public List<Apportionment> listByFixedMovement(FixedMovement fixedMovement) {

        final Criteria criteria = this.createCriteria();

        criteria.createAlias("fixedMovement", "fm");
        criteria.add(Restrictions.eq("fm.id", fixedMovement.getId()));

        return criteria.list();
    }

    /**
     * 
     * @param period
     * @param movementClass
     * @return 
     */
    @Override
    public BigDecimal totalMovementsPerClassAndPeriod(FinancialPeriod period, MovementClass movementClass) {

        final Criteria criteria = this.createCriteria();

        criteria.createAlias("movement", "mv");
        criteria.createAlias("mv.financialPeriod", "fp");
        
        criteria.add(Restrictions.eq("fp.id", period.getId()));
        criteria.add(Restrictions.in("mv.movementStateType", 
                new Object[]{MovementStateType.PAID, MovementStateType.CALCULATED}));
        criteria.add(Restrictions.eq("movementClass", movementClass));
        
        criteria.setProjection(Projections.sum("value"));

        return (BigDecimal) criteria.uniqueResult();
    }
}
