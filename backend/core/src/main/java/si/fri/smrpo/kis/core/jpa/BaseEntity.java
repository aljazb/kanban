package si.fri.smrpo.kis.core.jpa;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.ws.rs.core.EntityTag;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@MappedSuperclass
public abstract class BaseEntity<E extends BaseEntity, I extends Serializable> implements Serializable {

    public abstract I getId();
    public abstract void setId(I id);

    @Version
    protected Integer version;

    @Column(name = "is_deleted", nullable = false)
    protected Boolean isDeleted;

    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdOn;

    @Column(name = "edited_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date editedOn;



    @JsonIgnore
    public void update(E object, EntityManager em) throws IllegalAccessException {
        genericUpdate(object, em);
    }

    @JsonIgnore
    public void patch(E object, EntityManager em) throws IllegalAccessException {
        genericPatch(object, em);
    }

    @JsonIgnore
    public void prePersist(){
        version = null;
        isDeleted = false;

        Date date = new Date();
        createdOn = date;
        editedOn = date;
    }

    @JsonIgnore
    public void preUpdate(){
        Date date = new Date();
        editedOn = date;
    }

    @JsonIgnore
    protected List<Field> getAllClassFields(){
        List<Field> fieldList = new ArrayList<>();
        Class tmpClass = getClass();
        while (tmpClass != null) {
            fieldList.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }
        return fieldList;
    }

    @JsonIgnore
    public BaseEntity cloneObject(){
        try{
            BaseEntity clone = getClass().newInstance();

            for (Field field : getAllClassFields()) {
                field.setAccessible(true);

                Class<?> type = field.getType();
                if(!Set.class.isAssignableFrom(type)) {
                    if (BaseEntity.class.isAssignableFrom(type)) {
                        if (field.getAnnotation(JoinColumn.class) != null) {
                            field.set(clone, field.get(this));
                        }
                    } else {
                        field.set(clone, field.get(this));
                    }
                }
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }

    @JsonIgnore
    protected boolean baseSkip(Field field){
        switch (field.getName()) {
            case "id":
            case "isDeleted":
            case "editedOn":
            case "createdOn":
            case "version":
                return true;
            default:
                return false;
        }
    }

    @JsonIgnore
    protected boolean genericUpdateSkip(Field field){
        return baseSkip(field);
    }

    @JsonIgnore
    public void genericUpdate(E object, EntityManager em) throws IllegalAccessException {
        for (Field field : getAllClassFields()) {
            if(genericUpdateSkip(field)){
                continue;
            }

            field.setAccessible(true);

            Class<?> classType = field.getType();

            if (BaseEntity.class.isAssignableFrom(classType)) {
                BaseEntity obj = (BaseEntity) field.get(object);
                if (obj != null && obj.getId() != null) {
                    if(!em.contains(obj)) {
                        Object setObj = em.getReference(classType, obj.getId());
                        field.set(this, setObj);
                    }
                } else {
                    field.set(this, null);
                }
            } else {
                if(Set.class.isAssignableFrom(classType)) {
                    field.set(this, null);
                } else {
                    field.set(this, field.get(object));
                }
            }
        }
    }

    @JsonIgnore
    protected boolean genericPatchSkip(Field field){
        return baseSkip(field);
    }

    @JsonIgnore
    public void genericPatch(E object, EntityManager em) throws IllegalAccessException {
        for (Field field : getAllClassFields()) {
            if(genericPatchSkip(field)){
                continue;
            }

            field.setAccessible(true);

            Class<?> classType = field.getType();

            if (BaseEntity.class.isAssignableFrom(classType)) {
                BaseEntity obj = (BaseEntity) field.get(object);
                if (obj != null && obj.getId() != null) {
                    if(!em.contains(obj)) {
                        Object setObj = em.getReference(obj.getClass(), obj.getId());
                        field.set(this, setObj);
                    }
                }
            } else {
                if(Set.class.isAssignableFrom(classType)){
                    field.set(this, null);
                } else {
                    Object obj = field.get(object);
                    if (obj != null){
                        field.set(this, obj);
                    }
                }
            }
        }
    }

    @JsonIgnore
    public boolean genericIsDifferentSkip(Field field){
        return baseSkip(field);
    }

    private boolean areObjectDifferent(Class type, Field field, Object e1, Object e2){
        if (BaseEntity.class.isAssignableFrom(type)) {
            if (field.getAnnotation(JoinColumn.class) != null) {
                if(!((BaseEntity) e1).getId().equals(((BaseEntity) e2).getId())){
                    return true;
                }
            }
        } else {
            if(BigDecimal.class.isAssignableFrom(type)){
                if(((BigDecimal)e1).compareTo((BigDecimal) e2) != 0){
                    return true;
                }
            } else {
                if(!e1.equals(e2)){
                    return true;
                }
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isUpdateDifferent(BaseEntity entity) throws IllegalAccessException {
        for (Field field : getAllClassFields()) {
            if(genericIsDifferentSkip(field)){
                continue;
            }

            field.setAccessible(true);

            Class<?> type = field.getType();
            if(!Set.class.isAssignableFrom(type)) {
                Object e1 = field.get(this);
                Object e2 = field.get(entity);

                if(e1 == null && e2 == null) {
                    continue;
                } else if(e1 != null && e2 != null) {
                    if(areObjectDifferent(type, field, e1, e2)){
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    @JsonIgnore
    public boolean isPatchDifferent(BaseEntity entity) throws IllegalAccessException {
        for (Field field : getAllClassFields()) {
            if(genericIsDifferentSkip(field)){
                continue;
            }

            field.setAccessible(true);

            Class<?> type = field.getType();
            if(!Set.class.isAssignableFrom(type)) {
                Object e1 = field.get(this);
                Object e2 = field.get(entity);

                if(e2 == null) {
                    continue;
                } if (e1 == null) {
                    return true;
                } else {
                    if(areObjectDifferent(type, field, e1, e2)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @JsonIgnore
    public void setAllBasePropertiesToNull(){
        setId(null);
        version = null;
        isDeleted = null;
        createdOn = null;
        editedOn = null;
    }

    @JsonIgnore
    public String isValidDatabaseItem() throws IllegalAccessException {
        StringBuilder sbError = new StringBuilder();

        for (Field field : getAllClassFields()) {
            field.setAccessible(true);

            Class<?> type = field.getType();
            if (!Set.class.isAssignableFrom(type)) {
                if (BaseEntity.class.isAssignableFrom(type)) {
                    if (field.getAnnotation(JoinColumn.class) != null) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        if(joinColumn != null){
                            Object value = field.get(this);
                            if(!joinColumn.nullable()){
                                if(value == null){
                                    sbError.append(String.format("%s:NotNull:%s,", field.getName(), "Can not be null"));
                                } else if(((BaseEntity) value).getId() == null) {
                                    sbError.append(String.format("%s:NotNull:%s id,", field.getName(), "Can not be null"));
                                }
                            }
                        }
                    }
                } else {
                    Column column = field.getAnnotation(Column.class);
                    if(column != null){
                        Object value = field.get(this);
                        if(!column.nullable() && value == null){
                            sbError.append(String.format("%s:NotNull:%s,", field.getName(), "Can not be null"));
                        }

                        if(value instanceof String && !column.columnDefinition().equals("TEXT")){
                            if(column.length() < ((String)value).length()){
                                sbError.append(String.format("%s:MaxLength(%d):%s,", field.getName(), column.length(), "String is too long"));
                            }
                        }
                    }
                }
            }
        }

        if(sbError.toString().equals("")){
            return null;
        } else {
            return String.format("[%s] %s", getClass().getSimpleName(), sbError.toString());
        }
    }

    @JsonIgnore
    public EntityTag getEntityTag(){
        return new EntityTag(String.valueOf(this.editedOn.getTime()));
    }




    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean active) {
        isDeleted = active;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(Date editedOn) {
        this.editedOn = editedOn;
    }


}
