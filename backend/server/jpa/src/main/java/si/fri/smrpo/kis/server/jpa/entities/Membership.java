package si.fri.smrpo.kis.server.jpa.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import si.fri.smrpo.kis.server.jpa.entities.base.UUIDEntity;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.persistence.*;

@Entity
@Table(name="membership")
@Cacheable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Membership extends UUIDEntity<Membership> {

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @JsonIgnore
    public boolean isKanbanMaster() {
        return memberType == MemberType.KANBAN_MASTER || memberType == MemberType.DEVELOPER_AND_KANBAN_MASTER;
    }

    @JsonIgnore
    public boolean isDeveloper() {
        return memberType == MemberType.DEVELOPER || memberType == MemberType.DEVELOPER_AND_KANBAN_MASTER || memberType == MemberType.DEVELOPER_AND_PRODUCT_OWNER;
    }

    @JsonIgnore
    public boolean isProductOwner() {
        return memberType == MemberType.PRODUCT_OWNER || memberType == MemberType.DEVELOPER_AND_PRODUCT_OWNER;
    }

}
