package club.beingsoft.restaurants.model;

import club.beingsoft.restaurants.util.FieldUtil;
import club.beingsoft.restaurants.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
// http://stackoverflow.com/questions/594597/hibernate-annotations-which-is-better-field-or-property-access
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity implements Cloneable {
    public static final int START_SEQ = 100000;
    private static final Logger log = LoggerFactory.getLogger(AbstractBaseEntity.class);
    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Column(name = "start_date", nullable = false, columnDefinition = "timestamp default sysdate")
    @NotNull
    protected LocalDateTime startDate = FieldUtil.getSysdate();

    @Column(name = "end_date", columnDefinition = "timestamp default sysdate")
    protected LocalDateTime endDate = FieldUtil.getEndDate();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EntityStatus status = EntityStatus.ACTIVE;

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
        return this.status == EntityStatus.DELETED;
    }

    public void delete() {
        this.status = EntityStatus.DELETED;
    }

    public AbstractBaseEntity changeHistory() {
        AbstractBaseEntity nextEntityRec = null;
        try {
            nextEntityRec = (AbstractBaseEntity) this.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Cloning exception: ", e);
            System.exit(-1);
        }
        LocalDateTime time = FieldUtil.getSysdate();
        this.endDate = time;
        nextEntityRec.startDate = time;
        nextEntityRec.endDate = FieldUtil.getEndDate();
        return nextEntityRec;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "id=" + id +
                ", user=" + user +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", entityStatus=" + status;
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