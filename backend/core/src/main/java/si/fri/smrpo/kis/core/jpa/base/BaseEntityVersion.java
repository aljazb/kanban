package si.fri.smrpo.kis.core.jpa.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;

@MappedSuperclass
public abstract class BaseEntityVersion<T extends BaseEntityVersion, K extends Serializable> extends BaseEntity<T, K> {

    public abstract K getOriginId();
    public abstract void setOriginId(K originId);


    @Column(name = "version_order", nullable = false)
    protected Integer versionOrder;

    @Column(name = "is_latest", nullable = false)
    protected Boolean isLatest;


    @JsonIgnore
    @Override
    public void prePersist() {
        super.prePersist();
        this.isLatest = true;
        this.versionOrder = 1;
    }

    @JsonIgnore
    public void prePersist(int versionOrder) {
        super.prePersist();
        this.isLatest = true;
        this.versionOrder = versionOrder;
    }

    @JsonIgnore
    public void setAllBaseVersionPropertiesToNull() {
        setOriginId(null);
        versionOrder = null;
        isLatest = null;
    }

    @JsonIgnore
    @Override
    protected boolean baseSkip(Field field){
        boolean skip = super.baseSkip(field);
        if(skip) {
            return skip;
        } else {
            switch (field.getName()){
                case "originId":
                case "versionOrder":
                case "isLatest":
                    return true;
                default:
                    return false;
            }
        }
    }

    public Integer getVersionOrder() {
        return versionOrder;
    }

    public void setVersionOrder(int versionOrder) {
        this.versionOrder = versionOrder;
    }

    public Boolean getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(Boolean latest) {
        isLatest = latest;
    }

}

