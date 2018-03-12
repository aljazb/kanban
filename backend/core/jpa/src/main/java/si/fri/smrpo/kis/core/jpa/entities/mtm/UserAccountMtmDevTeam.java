package si.fri.smrpo.kis.core.jpa.entities.mtm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.DevTeam;
import si.fri.smrpo.kis.core.jpa.entities.UserAccount;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="user_account_mtm_dev_team")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class UserAccountMtmDevTeam extends BaseEntity<UserAccountMtmDevTeam> {


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
}
