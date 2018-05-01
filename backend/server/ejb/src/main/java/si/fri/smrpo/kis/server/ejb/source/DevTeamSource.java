package si.fri.smrpo.kis.server.ejb.source;

import si.fri.smrpo.kis.core.logic.dto.Paging;
import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.lynx.beans.QueryParameters;
import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.core.rest.source.CrudSource;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.models.HistoryEvent;
import si.fri.smrpo.kis.server.ejb.service.interfaces.DevTeamServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.ejb.source.interfaces.DevTeamSourceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.From;
import java.util.List;
import java.util.UUID;

@PermitAll
@Stateless
@Local(DevTeamSourceLocal.class)
public class DevTeamSource extends CrudSource<DevTeam, UUID> implements DevTeamSourceLocal {

    @EJB
    private DatabaseServiceLocal databaseService;

    @EJB
    private DevTeamServiceLocal service;

    @EJB
    private RequestServiceLocal requestService;


    private UserAccount authUser;


    @PostConstruct
    private void init() {
        setDatabase(databaseService);
    }


    @Override
    public Paging<DevTeam> getList(Class<DevTeam> c, QueryParameters param) throws Exception {
        CriteriaFilter<DevTeam> filter = null;
        if(!authUser.getInRoleAdministrator()) {
            filter = (p, cb, r) -> {
                From membership = r.join("joinedUsers");
                return cb.and(p, cb.and(
                        cb.equal(membership.get("isDeleted"), false),
                        cb.equal(membership.join("userAccount").get("id"), authUser.getId())));
            };
        }

        return super.getList(c, param, filter, filter != null);
    }

    @Override
    public DevTeam get(Class<DevTeam> c, UUID id) throws Exception {
        DevTeam entity = database.getEntityManager().createNamedQuery("devTeam.fetch.members", DevTeam.class)
                .setParameter("devTeamId", id).getResultList().stream().findFirst().orElse(null);

        if(entity == null) throw new OperationException("Entity does not exist", ExceptionType.ENTITY_DOES_NOT_EXISTS);

        entity.setMembership(entity.findMember(authUser.getId()));

        if (!authUser.getInRoleAdministrator() && entity.getMembership() == null) {
            throw new DatabaseException("User does not have permission.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        return entity;
    }


    @Override
    public DevTeam create(DevTeam newEntity) throws Exception {
        return service.create(newEntity, authUser.getId());
    }


    @Override
    public DevTeam update(DevTeam newEntity) throws Exception {
        return service.update(newEntity, authUser.getId());

    }


    public List<HistoryEvent> getEvents(UUID devTeamId) throws Exception {
        return service.getDevTeamEvents(devTeamId);
    }

    public UserAccount kickMember(UUID devTeamId, UUID userId) throws Exception {
        return service.kickMember(devTeamId, userId, authUser.getId());
    }

    public void demoteProductOwner(UUID devTeamId, UUID userId) throws Exception {
        requestService.demotePO(devTeamId, authUser.getId());
    }



    public UserAccount getAuthUser() {
        return authUser;
    }

    public void setAuthUser(UserAccount authUser) {
        this.authUser = authUser;
    }
}
