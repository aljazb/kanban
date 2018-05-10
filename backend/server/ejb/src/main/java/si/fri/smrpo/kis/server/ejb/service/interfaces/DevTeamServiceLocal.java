package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.models.HistoryEvent;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.List;
import java.util.UUID;

public interface DevTeamServiceLocal {

    DevTeam create(DevTeam devTeam, UserAccount authUser) throws LogicBaseException;
    DevTeam update(DevTeam devTeam, UserAccount authUser) throws LogicBaseException;

    UserAccount kickMember(UUID devTeamId, UUID memberId, UserAccount authUser) throws LogicBaseException;

    List<HistoryEvent> getDevTeamEvents(UUID devTeamId, UserAccount authUser) throws LogicBaseException;

}
