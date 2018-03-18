package si.fri.smrpo.kis.core.jpa;

import si.fri.smrpo.kis.core.jpa.base.BaseEntityVersion;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

public abstract class UUIDEntityVersion<T extends UUIDEntityVersion> extends BaseEntityVersion<T, UUID> {

    @Id
    @Column(name = "id")
    protected UUID id;

    public UUID getId(){
        return this.id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    @Override
    public void prePersist() {
        super.prePersist();
        if(this.id == null){
            id = UUID.randomUUID();
        }
    }

    @Column(name = "origin_id")
    protected UUID originId;

    public UUID getOriginId(){
        return originId;
    }

    public void setOriginId(UUID originId){
        this.originId = originId;
    }
}
