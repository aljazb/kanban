package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.Request;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.List;
import java.util.UUID;

public interface RequestSourceLocal extends GetSourceImpl<Request, UUID, UserAccount> {

    List<Request> getUserRequests(UserAccount authUser);
    Request create(Request request, UserAccount authUser) throws Exception;
    Request update(UUID requestId, boolean status, UserAccount authUser) throws Exception;

}
