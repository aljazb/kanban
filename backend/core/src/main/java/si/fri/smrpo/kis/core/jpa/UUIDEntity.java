package si.fri.smrpo.kis.core.jpa;

import si.fri.smrpo.kis.core.jpa.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class UUIDEntity<T extends UUIDEntity> extends BaseEntity<T, UUID> {

    @Id
    @Column(name = "id")
    protected UUID id;

    @Override
    public void prePersist() {
        super.prePersist();
        if(this.id == null){
            id = UUID.randomUUID();
        }
    }

    public UUID getId(){
        return this.id;
    }

    public void setId(UUID id){
        this.id = id;
    }

}
