package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;

import java.util.UUID;

public interface DevTeamServiceLocal {

    DevTeam create(DevTeam devTeam, UUID userId) throws DatabaseException;

}
