package si.fri.smrpo.kis.core.jpa;

import si.fri.smrpo.kis.core.jpa.base.BaseEntity;

import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class UUIDEntity<T extends UUIDEntity> extends BaseEntity<T, UUID> {

    @Override
    public void prePersist() {
        super.prePersist();
        if(this.id == null){
            id = UUID.randomUUID();
        }
    }
}
