package si.fri.smrpo.kis.server.jpa.entities.base;

import si.fri.smrpo.kis.core.jpa.BaseEntity;
import si.fri.smrpo.kis.core.jpa.anotations.Database;
import si.fri.smrpo.kis.server.jpa.enums.MemberType;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;



@MappedSuperclass
public abstract class UUIDEntity<E extends UUIDEntity> extends BaseEntity<E, UUID> {

    @Database(update = false)
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

    public static  <U extends UUIDEntity> HashMap<UUID, U> buildMap(Collection<U> entitySet) {
        HashMap<UUID, U> map = new HashMap<>();

        for(U u : entitySet) {
            map.put(u.getId(), u);
        }

        return map;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UUIDEntity) {
            return id.equals(((UUIDEntity) obj).getId());
        }
        return false;
    }
}
