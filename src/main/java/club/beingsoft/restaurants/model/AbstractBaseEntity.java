package club.beingsoft.restaurants.model;

import club.beingsoft.restaurants.util.SecurityUtil;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
// http://stackoverflow.com/questions/594597/hibernate-annotations-which-is-better-field-or-property-access
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity {
    public static final int START_SEQ = 100000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Column(name = "edit_date", nullable = false)
    @NotNull
    protected LocalDateTime editDate = LocalDateTime.now();


    @ManyToOne
    @JoinColumn(name = "delete_user_id")
    protected User delete_user;

    @Column(name = "delete_date")
    protected LocalDateTime deleteDate;


    protected AbstractBaseEntity() {
    }

    protected AbstractBaseEntity(Integer id) {
        this.id = id;
        this.user = SecurityUtil.getAuthUser();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // doesn't work for hibernate lazy proxy
    public int id() {
        Assert.notNull(id, "Entity must has id");
        return id;
    }

    public void setUser() {
        this.user = SecurityUtil.getAuthUser();
    }

    public boolean isNew() {
        return this.id == null;
    }

    public boolean isDeleted() {
        return this.delete_user != null;
    }

    public void delete() {
        this.delete_user = SecurityUtil.getAuthUser();
        this.deleteDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" +
                "id=" + id +
                ", editUser=" + user.name +
                ", editDate=" + editDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
}