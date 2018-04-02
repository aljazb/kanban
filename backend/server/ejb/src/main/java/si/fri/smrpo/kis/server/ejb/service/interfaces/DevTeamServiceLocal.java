package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import java.util.UUID;

public interface DevTeamServiceLocal {

    DevTeam create(DevTeam devTeam, UUID userId) throws LogicBaseException;
    DevTeam update(DevTeam devTeam, UUID userId) throws LogicBaseException;

    Paging<UserAccount> getDevelopers(UUID devTeamId);

    UserAccount getKanbanMaster(UUID devTeamId);

    UserAccount getProductOwner(UUID devTeamId);

    UserAccount kickMember(UUID devTeamId, UUID memberId, UUID authId) throws LogicBaseException;

    DevTeam getWithUsers(UUID id) throws LogicBaseException;
}
