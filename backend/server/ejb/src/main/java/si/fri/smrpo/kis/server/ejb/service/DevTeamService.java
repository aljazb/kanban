package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.UUID;

@PermitAll
@Stateless
@Local(DevTeamServiceLocal.class)
public class DevTeamService implements DevTeamServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    public DevTeam create(DevTeam devTeam, UUID userId) throws DatabaseException {

        UserAccount ua = database.getEntityManager().getReference(UserAccount.class, userId);

        devTeam = database.create(devTeam);

        UserAccountMtmDevTeam member = new UserAccountMtmDevTeam();
        member.setMemberType(MemberType.KANBAN_MASTER);
        member.setDevTeam(devTeam);
        member.setUserAccount(ua);

        member = database.create(member);

        HashSet<UserAccountMtmDevTeam> members = new HashSet<>();
        members.add(member);

        devTeam.setJoinedUsers(members);

        return devTeam;
    }

}
