package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
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
import java.util.List;
import java.util.UUID;


@PermitAll
@Stateless
@Local(UserAccountServiceLocal.class)
public class UserAccountService implements UserAccountServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    @EJB
    private KeycloakAdminServiceLocal keycloak;



    @Override
    public UserAccount login(UserAccount authEntity) throws LogicBaseException {
        return database.get(UserAccount.class, authEntity.getId());
    }

    @Override
    public UserAccount create(UserAccount authEntity) throws LogicBaseException {
        validate(authEntity);

        String id = keycloak.create(authEntity);
        authEntity.setId(UUID.fromString(id));

        return database.create(authEntity);
    }

    @Override
    public UserAccount update(UserAccount authEntity) throws LogicBaseException {
        validate(authEntity);

        keycloak.update(authEntity);

        return database.update(authEntity);
    }

    @Override
    public UserAccount setEnabled(UUID id, Boolean isDeleted) throws LogicBaseException {
        UserAccount ua = database.get(UserAccount.class, id);

        if(isDeleted == null) isDeleted = !ua.getIsDeleted();
        ua.setIsDeleted(isDeleted);

        keycloak.setEnabled(id.toString(), !isDeleted);

        return database.update(ua);
    }

    @Override
    public void setPassword(UUID id, String password) throws LogicBaseException {
        keycloak.setPassword(id.toString(), password);
    }

    private boolean isEmailAvailable(String email, UUID id) {
        List<UserAccount> users = database.getEntityManager().createNamedQuery("user-account.where.email", UserAccount.class)
                .setParameter("email", email).setMaxResults(1).getResultList();
        if(users.isEmpty()) {
            return true;
        } else if(id != null) {
            return users.get(0).getId().equals(id);
        } else {
            return false;
        }
    }

    private boolean isUsernameAvailable(String username, UUID id) {
        List<UserAccount> users = database.getEntityManager().createNamedQuery("user-account.where.username", UserAccount.class)
                .setParameter("username", username).setMaxResults(1).getResultList();
        if(users.isEmpty()) {
            return true;
        } else if(id != null) {
            return users.get(0).getId().equals(id);
        } else {
            return false;
        }
    }

    @Override
    public void checkAvailability(UserAccount userAccount) throws LogicBaseException {
        if(userAccount.getEmail() != null) {
            if(!isEmailAvailable(userAccount.getEmail(), userAccount.getId())) {
                throw new OperationException("Email is taken");
            }
        } else if(userAccount.getUsername() != null){
            if(!isUsernameAvailable(userAccount.getUsername(), userAccount.getId())) {
                throw new OperationException("Username is taken");
            }
        }
    }

    private void validate(UserAccount userAccount) throws LogicBaseException {
        if(userAccount.getUsername() == null) {
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
        } else {
            checkAvailability(userAccount);
        }
    }

}
