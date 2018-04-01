package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.TransactionException;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface KeycloakAdminServiceLocal {

    String create(UserAccount userAccount) throws TransactionException;
    void update(UserAccount userAccount) throws TransactionException;
    void setEnabled(String id, boolean enabled) throws TransactionException;

}
