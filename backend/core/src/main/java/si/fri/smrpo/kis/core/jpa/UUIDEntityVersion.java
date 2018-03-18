package si.fri.smrpo.kis.core.jpa;

import si.fri.smrpo.kis.core.jpa.base.BaseEntityVersion;

import java.util.UUID;

public abstract class UUIDEntityVersion<T extends UUIDEntityVersion> extends BaseEntityVersion<T, UUID> {
    @Override
    public void prePersist() {
        super.prePersist();
        if(this.id == null){
            id = UUID.randomUUID();
        }
    }
}
