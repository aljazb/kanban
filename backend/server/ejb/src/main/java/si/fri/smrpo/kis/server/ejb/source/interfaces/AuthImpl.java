package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

public interface AuthImpl {

    void setAuthUser(UserAccount authUser);
    UserAccount getAuthUser();
}
