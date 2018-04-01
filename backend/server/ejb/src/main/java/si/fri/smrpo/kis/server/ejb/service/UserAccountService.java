package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.KeycloakAdminServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.UserAccountServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.UUID;


@PermitAll
@Stateless
@Local(UserAccountServiceLocal.class)
public class UserAccountService implements UserAccountServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    @EJB
    private KeycloakAdminServiceLocal keycloak;

    private void validate(UserAccount userAccount, boolean isPasswordSet) throws TransactionException {
        if(isPasswordSet && userAccount.getPassword() == null){
            throw new TransactionException("Password not specified.");
        } else if(userAccount.getUsername() == null) {
            throw new TransactionException("Username not specified.");
        } else if(userAccount.getEmail() == null) {
            throw new TransactionException("Email not specified.");
        } else if(userAccount.getFirstName() == null) {
            throw new TransactionException("First name not specified.");
        } else if(userAccount.getLastName() == null) {
            throw new TransactionException("Last name not specified.");
        } else if(userAccount.getInRoleAdministrator() == null) {
            throw new TransactionException("Role administrator not specified.");
        } else if(userAccount.getInRoleDeveloper() == null) {
            throw new TransactionException("Role developer not specified.");
        } else if(userAccount.getInRoleKanbanMaster() == null) {
            throw new TransactionException("Role kanban master not specified.");
        } else if(userAccount.getInRoleProductOwner() == null) {
            throw new TransactionException("Role product owner not specified.");
        }
    }

    @Override
    public UserAccount login(UserAccount authEntity) throws LogicBaseException {
        return database.get(UserAccount.class, authEntity.getId());
    }

    @Override
    public UserAccount create(UserAccount authEntity) throws LogicBaseException {
        validate(authEntity, true);

        UUID id = UUID.fromString(keycloak.create(authEntity));
        authEntity.setId(id);
        return database.create(authEntity);
    }

    @Override
    public UserAccount update(UserAccount authEntity) throws LogicBaseException {
        validate(authEntity, false);

        keycloak.update(authEntity);
        return database.update(authEntity);
    }

    @Override
    public UserAccount setEnabled(UUID id, Boolean isDeleted) throws LogicBaseException {
        UserAccount ua = database.get(UserAccount.class, id);

        if(isDeleted == null){
            isDeleted = !ua.getIsDeleted();
        }

        ua.setIsDeleted(isDeleted);
        ua = database.update(ua);

        keycloak.setEnabled(id.toString(), !isDeleted);

        return ua;
    }

}
