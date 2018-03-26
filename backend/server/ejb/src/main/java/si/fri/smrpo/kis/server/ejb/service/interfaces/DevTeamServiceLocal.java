package si.fri.smrpo.kis.server.ejb.service.interfaces;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.exceptions.NoContentException;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

public interface DevTeamServiceLocal {

    DevTeam create(DevTeam devTeam, UUID userId) throws DatabaseException;

    List<UserAccount> getDevelopers(UUID devTeamId);

    UserAccount getKanbanMaster(UUID devTeamId) throws NoContentException;

    UserAccount getProductOwner(UUID devTeamId) throws NoContentException;
}
