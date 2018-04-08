package si.fri.smrpo.kis.server.ejb.managers;

import si.fri.smrpo.kis.core.lynx.interfaces.CriteriaFilter;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthManager;
import si.fri.smrpo.kis.server.ejb.managers.base.AuthUser;
import si.fri.smrpo.kis.server.jpa.entities.Board;

public class BoardAuthManager extends AuthManager<Board> {

    public BoardAuthManager(AuthUser userAccount) {
        super(userAccount);
    }

    @Override
    public CriteriaFilter<Board> authCriteria() {
        return (p, cb, r) -> {
            if(!isUserInRole(ROLE_ADMINISTRATOR)) {
                return cb.and(p, cb.or(
                        cb.equal(r.join("projects").join("devTeam")
                                .join("joinedUsers").join("userAccount").get("id"), getUserId()),
                        cb.equal(r.join("owner").get("id"),  getUserId())
                ));
            } else {
                return p;
            }
        };
    }
}
