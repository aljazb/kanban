package si.fri.smrpo.kis.server.entities.mtm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.UUIDEntity;
import si.fri.smrpo.kis.core.jpa.base.BaseEntity;
import si.fri.smrpo.kis.server.entities.DevTeam;
import si.fri.smrpo.kis.server.entities.UserAccount;
import si.fri.smrpo.kis.server.enums.MemberType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="user_account_mtm_dev_team")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class UserAccountMtmDevTeam extends UUIDEntity<UserAccountMtmDevTeam> {

    @Enumerated(EnumType.STRING)
    private MemberType memberType;


    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "dev_team_id", nullable = false)
    private DevTeam devTeam;



    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public DevTeam getDevTeam() {
        return devTeam;
    }

    public void setDevTeam(DevTeam devTeam) {
        this.devTeam = devTeam;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }
}
