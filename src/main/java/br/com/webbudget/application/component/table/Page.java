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
package br.com.webbudget.application.component.table;

import br.com.webbudget.domain.model.entity.IPersistentEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @param <T>
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 11/09/2015
 */
public class Page<T extends IPersistentEntity> {

    public Optional<List<T>> optionalContent;
    public Optional<Long> optionalTotalPages;

    /**
     * 
     */
    public Page() {
        this.optionalContent = Optional.empty();
        this.optionalTotalPages = Optional.empty();
    }

    /**
     * 
     * @param content
     * @param totalPages 
     */
    public Page(List<T> content, Long totalPages) {
        this.optionalContent = Optional.of(content);
        this.optionalTotalPages = Optional.of(totalPages);
    }
    
    /**
     * 
     * @return 
     */
    public List<T> getContent() {
        return this.optionalContent.orElse(new ArrayList<>());
    }

    /**
     * 
     * @param content 
     */
    public void setContent(List<T> content) {
        this.optionalContent = Optional.of(content);
    }

    /**
     * 
     * @return 
     */
    public Long getTotalPages() {
        return this.optionalTotalPages.orElse(0L);
    }
    
    /**
     * 
     * @return 
     */
    public int getTotalPagesInt() {
        return this.optionalTotalPages.orElse(0L).intValue();
    }

    /**
     * 
     * @param totalPages 
     */
    public void setTotalPages(Long totalPages) {
        this.optionalTotalPages = Optional.of(totalPages);
    }
}
