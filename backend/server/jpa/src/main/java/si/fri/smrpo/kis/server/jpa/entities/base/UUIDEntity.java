package si.fri.smrpo.kis.server.jpa.entities.base;

import si.fri.smrpo.kis.core.jpa.BaseEntity;

import javax.persistence.*;
import java.util.UUID;



@MappedSuperclass
public abstract class UUIDEntity<T extends UUIDEntity> extends BaseEntity<T, UUID> {

    @Id
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    private UUID id;


    public UUID getId() {
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
}
