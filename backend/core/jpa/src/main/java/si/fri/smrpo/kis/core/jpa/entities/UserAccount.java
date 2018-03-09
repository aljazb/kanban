package si.fri.smrpo.kis.core.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntity;
import si.fri.smrpo.kis.core.jpa.entities.base.BaseEntityVersion;
import si.fri.smrpo.kis.core.jpa.utility.Constants;

import javax.persistence.*;
import java.lang.reflect.Field;

@Entity
@Table(name="user_account")
@Cacheable
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class UserAccount extends BaseEntity<UserAccount> {

    @Column(length = Constants.DEF_STRING_LEN, nullable = false)
    private String email;

    @Column(length = Constants.DEF_STRING_LEN)
    private String name;

    @Column(length = Constants.DEF_STRING_LEN)
    private String surname;


    @JsonIgnore
    protected boolean baseSkip(Field field){
        boolean skip = super.baseSkip(field);
        if(skip){
            return skip;
        } else {
            switch (field.getName()) {
                //case "email":
                case "authenticationId":
                    return true;
                default:
                    return false;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
