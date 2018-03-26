package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.Request;

import java.util.List;
import java.util.UUID;

public interface RequestServiceLocal {

    List<Request> getUserRequests(UUID userId);
    Request create(Request request, UUID senderId) throws LogicBaseException;
    Request update(UUID requestId, UUID recieverId, boolean status) throws LogicBaseException;

}
