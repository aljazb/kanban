package si.fri.smrpo.kis.server.ejb.source.interfaces;

import si.fri.smrpo.kis.core.rest.source.interfaces.CrudSourceImpl;
import si.fri.smrpo.kis.server.ejb.models.HistoryEvent;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;


public interface DevTeamSourceLocal extends CrudSourceImpl<DevTeam, UUID>, AuthImpl {

    List<HistoryEvent> getEvents(UUID devTeamId) throws Exception;
    void demoteProductOwner(UUID devTeamId, UUID userId) throws Exception;
    UserAccount kickMember(UUID devTeamId, UUID userId) throws Exception;

}
