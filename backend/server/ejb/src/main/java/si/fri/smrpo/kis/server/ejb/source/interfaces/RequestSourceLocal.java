package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.GetSourceImpl;
import si.fri.smrpo.kis.server.jpa.entities.Request;

import java.util.List;
import java.util.UUID;

public interface RequestSourceLocal extends GetSourceImpl<Request, UUID>, AuthImpl {

    List<Request> getUserRequests();
    Request create(Request request) throws Exception;
    Request update(UUID requestId, boolean status) throws Exception;

}
