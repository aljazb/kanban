package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.models.HistoryEvent;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.List;
import java.util.UUID;

public interface DevTeamServiceLocal {

    DevTeam create(DevTeam devTeam, UUID userId) throws LogicBaseException;
    DevTeam update(DevTeam devTeam, UUID userId) throws LogicBaseException;

    UserAccount kickMember(UUID devTeamId, UUID memberId, UUID authId) throws LogicBaseException;

    List<HistoryEvent> getDevTeamEvents(UUID devTeamId) throws LogicBaseException;
}
