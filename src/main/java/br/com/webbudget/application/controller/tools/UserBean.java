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
package br.com.webbudget.application.controller.tools;

import br.com.webbudget.application.controller.AbstractBean;
import br.com.webbudget.domain.misc.ex.InternalServiceError;
import br.com.webbudget.domain.model.security.Group;
import br.com.webbudget.domain.model.service.AccountService;
import br.com.webbudget.domain.model.security.User;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.Identity;
import org.picketlink.idm.model.Attribute;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 02/03/2014
 */
@Named
@ViewScoped
public class UserBean extends AbstractBean {

    @Getter
    private User user;

    @Getter
    private List<User> users;
    @Getter
    private List<Group> groups;

    @Inject
    private Identity identity;
    
    @Inject
    private AccountService accountService;

    /**
     *
     */
    public void initializeListing() {
        this.viewState = ViewState.LISTING;
        this.users = this.accountService.listUsers(null);
    }

    /**
     * Inicializa a parte do profile do usuario
     */
    public void initializeProfile() {
        
        // setamos no usuario o usuario autenticado e seu perfil
        this.user = (User) this.identity.getAccount();
    }
    
    /**
     * @param userId
     */
    public void initializeForm(String userId) {
            
        this.groups = this.accountService.listGroups(null);

        if (userId.isEmpty()) {
            this.viewState = ViewState.ADDING;
            this.user = new User();
        } else {
            this.viewState = ViewState.EDITING;
            this.user = this.accountService.findUserById(userId);
        }
    }

    /**
     * @return o form de inclusao
     */
    public String changeToAdd() {
        return "formUser.xhtml?faces-redirect=true";
    }

    /**
     * @param userId
     * @return
     */
    public String changeToEdit(String userId) {
        return "formUser.xhtml?faces-redirect=true&userId=" + userId;
    }

    /**
     * @param userId
     */
    public void changeToDelete(String userId) {
        this.user = this.accountService.findUserById(userId);
        this.updateAndOpenDialog("deleteUserDialog", "dialogDeleteUser");
    }

    /**
     *
     */
    public void doSave() {

        try {
            this.accountService.save(this.user);
            this.user = new User();
            this.addInfo(true, "user.saved");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doUpdate() {

        try {
            this.accountService.update(this.user);

            this.addInfo(true, "user.updated");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }

    /**
     *
     */
    public void doDelete() {

        try {
            this.accountService.delete(this.user);
            this.users = this.accountService.listUsers(null);

            this.addInfo(true, "user.deleted");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }finally {
            this.updateComponent("usersList");
            this.closeDialog("dialogDeleteUser");
        }
    }
    
    /**
     *
     */
    public void doProfileUpdate() {
        
        try {
            this.accountService.updateProfile(this.user);
            this.addInfo(true, "user.profile-updated");
        } catch (InternalServiceError ex) {
            this.addError(true, ex.getMessage(), ex.getParameters());
        } catch (Exception ex) {
            this.logger.error(ex.getMessage(), ex);
            this.addError(true, "error.undefined-error", ex.getMessage());
        }
    }
    
    /**
     * Invoca a troca do tema selecionado pelo usuario
     * 
     * @param theme o tema que sera usado
     */
    public void changeTheme(String theme) {

        // remove o tema atual
        this.executeScript("$(\"body\").removeClass('"
                + this.user.getTheme() + "')");
        
        // coloca o novo
        this.executeScript("$(\"body\").addClass('" + theme + "')");

        // seta no usuario para quando for salvo
        this.user.setTheme(theme);
    }

    /**
     * @return
     */
    public String doCancel() {
        return "listUsers.xhtml?faces-redirect=true";
    }

    /**
     * @return
     */
    public String toDashboard() {
        return "/main/dashboard.xhtml?faces-redirect=true";
    }
}