package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.Request;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.mtm.UserAccountMtmDevTeam;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;
import si.fri.smrpo.kis.server.jpa.enums.RequestStatus;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@PermitAll
@Stateless
@Local(RequestServiceLocal.class)
public class RequestService implements RequestServiceLocal {

    @EJB
    private DatabaseServiceLocal database;

    public Request create(Request request, UUID authId) throws LogicBaseException {
        validateRequest(request);

        UserAccount sender = database.getEntityManager().getReference(UserAccount.class, authId);
        request.setSender(sender);

        switch (request.getRequestType()) {
            case DEV_TEAM_KAMBAN_MASTER_PROMOTION:
                return createKanbanMasterPromotion(request);
            case DEV_TEAM_INVITE:
            default:
                return createDevTeamInvite(request);
        }
    }

    private void validateRequest(Request request) throws LogicBaseException {
        if(request.getReferenceId() == null){
            throw new OperationException("Request reference id not specified.");
        }
        if(request.getRequestType() == null){
            throw new OperationException("No request type specified.");
        }
        if(request.getReceiver() == null || request.getReceiver().getId() == null){
            throw new OperationException("Request receiver specified.");

        } else {
            UserAccount receiver = database.get(UserAccount.class, request.getReceiver().getId());
            if(receiver == null || receiver.getIsDeleted()){
                throw new OperationException("Request receiver does not exist or is marked as deleted.");
            }
        }
    }

    private boolean isAuthUserKanbanMaster(UUID authUserId, UUID devTeamId){
        List<UserAccount> kanbanMaster = database.getEntityManager().createNamedQuery("devTeam.getKanbanMaster", UserAccount.class)
                .setParameter("id", devTeamId).getResultList();
        return kanbanMaster.stream().anyMatch(e -> e.getId().equals(authUserId));
    }

    private boolean isRecieverMemberOfDevTeam(UUID recieverID, UUID devTeamId){
        List<UserAccount> user = database.getEntityManager().createNamedQuery("devTeam.isMember", UserAccount.class)
                .setParameter("devTeamId", devTeamId).setParameter("userId", recieverID).getResultList();
        return !user.isEmpty();
    }

    private Request createDevTeamInvite(Request request) throws LogicBaseException {
        UUID devTeamId = request.getReferenceId();

         if(isAuthUserKanbanMaster(request.getSender().getId(), devTeamId)) {
             if(!isRecieverMemberOfDevTeam(request.getReceiver().getId(), devTeamId)) {
                 request.setRequestStatus(RequestStatus.PENDING);
                 return database.create(request);
             } else {
                 throw new OperationException("Receiver is already member.");
             }
         } else {
            throw new OperationException("User is has insufficient rights.", LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
         }
    }


    private Request createKanbanMasterPromotion(Request request) throws LogicBaseException {
        UUID devTeamId = request.getReferenceId();
        if(isAuthUserKanbanMaster(request.getSender().getId(), devTeamId)) {
            if(isRecieverMemberOfDevTeam(request.getReceiver().getId(), devTeamId)) {
                return database.create(request);
            } else {
                throw new OperationException("Receiver is not member of dev team.");
            }
        } else {
            throw new OperationException("User is not kanban master of dev team.", LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
        }
    }

    public Request update(UUID requestId, UUID authId, boolean statusDecision) throws LogicBaseException {
        Request request = database.get(Request.class, requestId);

        if(request != null) {
            if(request.getRequestStatus() == RequestStatus.PENDING) {
                if(request.getReceiver().getId().equals(authId)) {
                    return processUpdate(request, statusDecision ? RequestStatus.ACCEPTED : RequestStatus.DECLINED);
                } else if(request.getSender().getId().equals(authId) && !statusDecision) {
                    return processUpdate(request, RequestStatus.CANCELED);
                } else {
                    throw new OperationException("User is not reciever or sender of request.", LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
                }
            } else {
                throw new OperationException("Request was already updated.");
            }
        } else {
            throw new DatabaseException(String.format("Request with id %s does not exist", requestId.toString()), LogicBaseException.Metadata.ENTITY_DOES_NOT_EXISTS);
        }
    }

    private Request processUpdate(Request request, RequestStatus status) throws DatabaseException {
        if(request.getRequestStatus() == RequestStatus.ACCEPTED){
            switch (request.getRequestType()){
                case DEV_TEAM_INVITE:
                    processDevTeamInvite(request); break;
                case DEV_TEAM_KAMBAN_MASTER_PROMOTION:
                    processKanbanMasterPromotion(request); break;
            }
        }

        request.setRequestStatus(status);
        request = database.update(request);

        return request;
    }

    private void processDevTeamInvite(Request request) throws DatabaseException {
        DevTeam devTeam = database.getEntityManager().getReference(DevTeam.class, request.getReferenceId());
        UserAccount user = database.getEntityManager().getReference(UserAccount.class, request.getReceiver().getId());

        UserAccountMtmDevTeam mtm = new UserAccountMtmDevTeam();
        mtm.setMemberType(MemberType.DEVELOPER);
        mtm.setUserAccount(user);
        mtm.setDevTeam(devTeam);

        mtm = database.create(mtm);
    }

    private void processKanbanMasterPromotion(Request request) throws DatabaseException {
        DevTeam devTeam = database.getEntityManager().find(DevTeam.class, request.getReferenceId());
        Set<UserAccountMtmDevTeam> members = devTeam.getJoinedUsers();


        UserAccountMtmDevTeam kanbanMaster = members.stream()
                .filter(e -> e.getUserAccount().getId().equals(request.getSender().getId()))
                .findFirst().orElse(null);

        if(kanbanMaster != null){
            kanbanMaster.setMemberType(MemberType.DEVELOPER);
            database.update(kanbanMaster);
        } else {
            throw new DatabaseException("Error during updating demoting kanban master to developer.");
        }

        UserAccountMtmDevTeam member = members.stream()
                .filter(e -> e.getUserAccount().getId().equals(request.getReceiver().getId()))
                .findFirst().orElse(null);

        if(member != null) {
            member.setMemberType(MemberType.DEVELOPER_AND_KANBAN_MASTER);
            database.update(member);
        } else {
            throw new DatabaseException("Error during updating developer to kanban master.");
        }

    }

}
