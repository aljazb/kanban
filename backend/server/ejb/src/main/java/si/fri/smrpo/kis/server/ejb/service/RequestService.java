package si.fri.smrpo.kis.server.ejb.service;

import si.fri.smrpo.kis.core.logic.exceptions.DatabaseException;
import si.fri.smrpo.kis.core.logic.exceptions.OperationException;
import si.fri.smrpo.kis.core.logic.exceptions.base.ExceptionType;
import si.fri.smrpo.kis.core.logic.exceptions.base.LogicBaseException;
import si.fri.smrpo.kis.server.ejb.database.DatabaseServiceLocal;
import si.fri.smrpo.kis.server.ejb.service.interfaces.RequestServiceLocal;
import si.fri.smrpo.kis.server.jpa.entities.DevTeam;
import si.fri.smrpo.kis.server.jpa.entities.Request;
import si.fri.smrpo.kis.server.jpa.entities.UserAccount;
import si.fri.smrpo.kis.server.jpa.entities.Membership;
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


    @Override
    public List<Request> getUserRequests(UUID userId) {
        return database.getEntityManager().createNamedQuery("request.getAll", Request.class)
                .setParameter("uid", userId)
                .getResultList();
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

            request.setReceiver(receiver);
        }
    }

    private boolean isUserProductOwner(UUID userId, DevTeam devTeam){
        return devTeam.getProductOwner().getId().equals(userId);
    }

    private boolean isUserKanbanMaster(UUID userId, DevTeam devTeam){
        return devTeam.getKanbanMaster().getId().equals(userId);
    }

    private boolean canBecomeRole(UserAccount userAccount, DevTeam devTeam) {
        return userAccount.getInRoleKanbanMaster() && // Has role KM
                !devTeam.getKanbanMaster().getId().equals(userAccount.getId()) && // Not in dev team role KM
                !devTeam.getProductOwner().getId().equals(userAccount.getId()); // Not in dev team role PO
    }

    private boolean isRecieverMemberOfDevTeam(UUID recieverID, DevTeam devTeam){
        Membership membership = devTeam.findMember(recieverID);
        return membership != null;
    }

    @Override
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

    private Request createRoleInvite(Request request, RequestType role) throws LogicBaseException {
        UUID senderId = request.getSender().getId();
        UUID devTeamId = request.getReferenceId();

        DevTeam devTeam = database.get(DevTeam.class, devTeamId);

        if(isUserKanbanMaster(senderId, devTeam)) {

            if(canBecomeRole(request.getReceiver(), devTeam)) {
                request.setRequestStatus(RequestStatus.PENDING);
                return database.create(request);
            } else {
                throw new OperationException("Receiver cannot become specified role.");
            }
        } else {
            throw new OperationException("User is not kanban master of dev team.", ExceptionType.INSUFFICIENT_RIGHTS);
        }
    }

    @Override
    public Request update(UUID requestId, UUID authId, boolean statusDecision) throws LogicBaseException {
        Request request = database.get(Request.class, requestId);
        DevTeam devTeam = database.get(DevTeam.class, request.getReferenceId());

        if(request.getRequestStatus() == RequestStatus.PENDING) {
            if(request.getReceiver().getId().equals(authId)) {
                return processUpdate(request, devTeam, statusDecision ? RequestStatus.ACCEPTED : RequestStatus.DECLINED);
            } else if(request.getSender().getId().equals(authId) && !statusDecision) {
                return processUpdate(request, devTeam, RequestStatus.CANCELED);
            } else {
                throw new OperationException("User is not receiver or sender of request.", ExceptionType.INSUFFICIENT_RIGHTS);
            }
        } else {
            throw new OperationException("Request was already updated.");
        }
    }

    private Request processUpdate(Request request, DevTeam devTeam, RequestStatus status) throws DatabaseException, OperationException {

        if(status == RequestStatus.ACCEPTED) {
            if (!isUserKanbanMaster(request.getSender().getId(), devTeam)) {
                request.setRequestStatus(RequestStatus.CANCELED);
                database.update(request);
                throw new OperationException("Request sender is (no longer) KanbanMaster of this development team.");
            }
            if (!request.getReceiver().getInRoleKanbanMaster()) {
                throw new OperationException("Receiver (no longer) has KanbanMaster role.");
            }
            switch (request.getRequestType()){
                case KANBAN_MASTER_INVITE:
                    if (canBecomeRole(request.getReceiver(), devTeam)) {
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



    @Override
    public void demotePO(UUID devTeamId, UUID authUserId) throws LogicBaseException {
        DevTeam devTeam = database.getEntityManager().find(DevTeam.class, devTeamId);

        if (devTeam == null) {
            throw new OperationException("Development team does not exist.");
        }

        if (!devTeam.getKanbanMaster().getId().equals(authUserId)) {
            throw new OperationException("User not KanbanMaster.", ExceptionType.INSUFFICIENT_RIGHTS);
        }

        demote(devTeam, MemberType.PRODUCT_OWNER);
    }

    private void demote(DevTeam devTeam, MemberType role) throws OperationException, DatabaseException {
        final UserAccount user;
        switch (role) {
            case KANBAN_MASTER:
                user = devTeam.getKanbanMaster();
                break;
            case PRODUCT_OWNER:
                user = devTeam.getProductOwner();
                break;
            default:
                throw new OperationException("Invalid role.");
        }

        if (user == null) {
            return;
        }

        Membership m = devTeam.findMember(user.getId());

        if (m == null) {
            throw new OperationException("Could not find mtm.");
        }

        if (m.getMemberType() == MemberType.DEVELOPER_AND_PRODUCT_OWNER ||
            m.getMemberType() == MemberType.DEVELOPER_AND_KANBAN_MASTER)
        {
            m.setMemberType(MemberType.DEVELOPER);
            database.update(m);
        } else {
            database.delete(Membership.class, m.getId());
        }
    }

    private void processRoleInvite(Request request) throws DatabaseException, OperationException {
        DevTeam devTeam = database.getEntityManager().find(DevTeam.class, request.getReferenceId());
        Set<Membership> members = devTeam.getJoinedUsers();

        switch (request.getRequestType()) {
            case KANBAN_MASTER_INVITE:
                demote(devTeam, MemberType.KANBAN_MASTER);
                break;
            default:
                throw new OperationException("Unknown request type.");
        }

        Membership receiverMtm = members.stream()
                .filter(e -> !e.getIsDeleted() && e.getUserAccount().getId().equals(request.getReceiver().getId()))
                .findFirst().orElse(null);

        boolean create = false;
        if (receiverMtm == null) {
            create = true;
            receiverMtm = new Membership();
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



    /*private void processDevTeamInvite(Request request) throws DatabaseException {
        DevTeam devTeam = database.getEntityManager().getReference(DevTeam.class, request.getReferenceId());
        UserAccount user = database.getEntityManager().getReference(UserAccount.class, request.getReceiver().getId());

        Membership mtm = new Membership();
        mtm.setMemberType(MemberType.DEVELOPER);
        mtm.setUserAccount(user);
        mtm.setDevTeam(devTeam);

        mtm = database.create(mtm);
    }*/

    /*private Request createDevTeamInvite(Request request) throws LogicBaseException {
        UUID devTeamId = request.getReferenceId();

        DevTeam devTeam = database.get(DevTeam.class, devTeamId);

        if(isUserKanbanMaster(request.getSender().getId(), devTeam)) {
            if(!isRecieverMemberOfDevTeam(request.getReceiver().getId(), devTeam)) {
                request.setRequestStatus(RequestStatus.PENDING);
                return database.create(request);
            } else {
                throw new OperationException("Receiver is already member.");
            }
        } else {
            throw new OperationException("User is has insufficient rights.", ExceptionType.INSUFFICIENT_RIGHTS);
        }
    }*/
}
