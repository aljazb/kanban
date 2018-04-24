package si.fri.smrpo.kis.core.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import si.fri.smrpo.kis.core.jpa.anotations.Database;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Field;

@MappedSuperclass
public abstract class BaseEntityVersion<E extends BaseEntityVersion, I extends Serializable> extends BaseEntity<E, I> {

    public abstract I getOriginId();
    public abstract void setOriginId(I originId);


    @Database(update = false)
    @Column(name = "version_order", nullable = false)
    protected Integer versionOrder;

    @Database(update = false)
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

