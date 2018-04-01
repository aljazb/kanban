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
import si.fri.smrpo.kis.server.jpa.enums.RequestType;

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

    public List<Request> getUserRequests(UUID userId) {
        List<Request> rl = database.getEntityManager().createNamedQuery("request.getAll", Request.class)
                .setParameter("uid", userId)
                .getResultList();
        return rl;
    }

    public Request create(Request request, UUID authId) throws LogicBaseException {
        validateRequest(request);

        UserAccount sender = database.getEntityManager().getReference(UserAccount.class, authId);
        request.setSender(sender);

        switch (request.getRequestType()) {
            case KANBAN_MASTER_INVITE:
                return createRoleInvite(request, request.getRequestType());
            default:
                throw new OperationException("Unknown request type");
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

    private boolean isUserKanbanMaster(UUID userId, UUID devTeamId){
        List<UserAccount> kanbanMaster = database.getEntityManager().createNamedQuery("devTeam.getKanbanMaster", UserAccount.class)
                .setParameter("id", devTeamId).getResultList();
        return kanbanMaster.stream().anyMatch(e -> e.getId().equals(userId));
    }

    private boolean isUserProductOwner(UUID userId, UUID devTeamId){
        List<UserAccount> productOwner = database.getEntityManager().createNamedQuery("devTeam.getProductOwner", UserAccount.class)
                .setParameter("id", devTeamId).getResultList();
        return productOwner.stream().anyMatch(e -> e.getId().equals(userId));
    }

    private boolean canBecomeRole(UUID userId, UUID devTeamId) {
        // cannot become role if already has a role
        return !isUserKanbanMaster(userId, devTeamId) && !isUserProductOwner(userId, devTeamId);
    }

    private boolean isRecieverMemberOfDevTeam(UUID recieverID, UUID devTeamId){
        List<UserAccount> user = database.getEntityManager().createNamedQuery("devTeam.isDeveloper", UserAccount.class)
                .setParameter("devTeamId", devTeamId).setParameter("userId", recieverID).getResultList();
        return !user.isEmpty();
    }

    private Request createDevTeamInvite(Request request) throws LogicBaseException {
        UUID devTeamId = request.getReferenceId();

         if(isUserKanbanMaster(request.getSender().getId(), devTeamId)) {
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


    private Request createRoleInvite(Request request, RequestType role) throws LogicBaseException {
        UUID devTeamId = request.getReferenceId();
        if(isUserKanbanMaster(request.getSender().getId(), devTeamId)) {
            if(canBecomeRole(request.getReceiver().getId(), devTeamId)) {
                request.setRequestStatus(RequestStatus.PENDING);
                return database.create(request);
            } else {
                throw new OperationException("Receiver cannot become specified role.");
            }
        } else {
            throw new OperationException("User is not kanban master of dev team.", LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
        }
    }

    public Request update(UUID requestId, UUID authId, boolean statusDecision) throws LogicBaseException {
        Request request = database.get(Request.class, requestId);
        validateRequest(request);

        if(request.getRequestStatus() == RequestStatus.PENDING) {
            if(request.getReceiver().getId().equals(authId)) {
                return processUpdate(request, statusDecision ? RequestStatus.ACCEPTED : RequestStatus.DECLINED);
            } else if(request.getSender().getId().equals(authId) && !statusDecision) {
                return processUpdate(request, RequestStatus.CANCELED);
            } else {
                throw new OperationException("User is not receiver or sender of request.", LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
            }
        } else {
            throw new OperationException("Request was already updated.");
        }
    }

    private Request processUpdate(Request request, RequestStatus status) throws DatabaseException, OperationException {
        if(status == RequestStatus.ACCEPTED) {
            if (!isUserKanbanMaster(request.getSender().getId(), request.getReferenceId())) {
                request.setRequestStatus(RequestStatus.CANCELED);
                database.update(request);
                throw new OperationException("Request sender is (no longer) KanbanMaster of this development team.");
            }
            switch (request.getRequestType()){
                case KANBAN_MASTER_INVITE:
                    if (canBecomeRole(request.getReceiver().getId(), request.getReferenceId())) {
                        processRoleInvite(request);
                    } else {
                        request.setRequestStatus(RequestStatus.CANCELED);
                        database.update(request);
                        throw new OperationException("User can not become this role.");
                    }
                    break;
                default:
                    throw new OperationException("Unknown request type.");
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

    @Override
    public void demotePO(UUID devTeamId, UUID authUserId) throws LogicBaseException {
        DevTeam devTeam = database.getEntityManager().find(DevTeam.class, devTeamId);

        if (devTeam == null) {
            throw new OperationException("Development team does not exist.");
        }

        if (!isUserKanbanMaster(authUserId, devTeamId)) {
            throw new OperationException("User not KanbanMaster.", LogicBaseException.Metadata.INSUFFICIENT_RIGHTS);
        }

        demote(devTeam, MemberType.PRODUCT_OWNER);
    }

    private void demote(DevTeam devTeam, MemberType role) throws OperationException, DatabaseException {
        final UserAccount user;
        switch (role) {
            case KANBAN_MASTER:
                user = database.getEntityManager().createNamedQuery("devTeam.getKanbanMaster", UserAccount.class)
                        .setParameter("id", devTeam.getId()).getResultList().stream().findFirst().orElse(null);
                break;
            case PRODUCT_OWNER:
                user = database.getEntityManager().createNamedQuery("devTeam.getProductOwner", UserAccount.class)
                        .setParameter("id", devTeam.getId()).getResultList().stream().findFirst().orElse(null);
                break;
            default:
                throw new OperationException("Invalid role.");
        }

        if (user == null) {
            return;
        }

        UserAccountMtmDevTeam mtm = devTeam.getJoinedUsers().stream().filter(e -> !e.getIsDeleted() &&
                e.getUserAccount().getId().equals(user.getId())).findFirst().orElse(null);

        if (mtm == null) {
            throw new OperationException("Could not find mtm.");
        }

        if (mtm.getMemberType() == MemberType.DEVELOPER_AND_PRODUCT_OWNER ||
                mtm.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER) {
            mtm.setMemberType(MemberType.DEVELOPER);
            database.update(mtm);
        } else {
            database.delete(UserAccountMtmDevTeam.class, mtm.getId());
        }
    }

    private void processRoleInvite(Request request) throws DatabaseException, OperationException {
        DevTeam devTeam = database.getEntityManager().find(DevTeam.class, request.getReferenceId());
        Set<UserAccountMtmDevTeam> members = devTeam.getJoinedUsers();

        switch (request.getRequestType()) {
            case KANBAN_MASTER_INVITE:
                demote(devTeam, MemberType.KANBAN_MASTER);
                break;
            default:
                throw new OperationException("Unknown request type.");
        }

        UserAccountMtmDevTeam receiverMtm = members.stream()
                .filter(e -> !e.getIsDeleted() && e.getUserAccount().getId().equals(request.getReceiver().getId()))
                .findFirst().orElse(null);

        boolean create = false;
        if (receiverMtm == null) {
            create = true;
            receiverMtm = new UserAccountMtmDevTeam();
            receiverMtm.setUserAccount(request.getReceiver());
            receiverMtm.setDevTeam(devTeam);
        }

        boolean isAlreadyDev = receiverMtm.getMemberType() == MemberType.DEVELOPER;
        switch (request.getRequestType()) {
            case KANBAN_MASTER_INVITE:
                if (isAlreadyDev) {
                    receiverMtm.setMemberType(MemberType.DEVELOPER_AND_KANBAN_MASTER);
                } else {
                    receiverMtm.setMemberType(MemberType.KANBAN_MASTER);
                }
                break;
            default:
                throw new OperationException("Invalid request type.");
        }

        if (create) {
            database.create(receiverMtm);
        } else {
            database.update(receiverMtm);
        }
    }

}
