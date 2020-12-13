package club.beingsoft.restaurants.to;


import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public abstract class BaseTo implements HasId {
    protected User deleteUser;
    protected LocalDateTime deleteDate;
    protected Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    protected String name;

    protected BaseTo() {
    }

    protected BaseTo(Integer id, String name
    ) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public User getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(User deleteUser) {
        this.deleteUser = deleteUser;
    }

    public LocalDateTime getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(LocalDateTime deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseTo)) return false;

        BaseTo baseTo = (BaseTo) o;

        if (deleteUser != null ? !deleteUser.equals(baseTo.deleteUser) : baseTo.deleteUser != null) return false;
        if (deleteDate != null ? !deleteDate.equals(baseTo.deleteDate) : baseTo.deleteDate != null) return false;
        if (id != null ? !id.equals(baseTo.id) : baseTo.id != null) return false;
        return name != null ? name.equals(baseTo.name) : baseTo.name == null;
    }

    @Override
    public int hashCode() {
        int result = deleteUser != null ? deleteUser.hashCode() : 0;
        result = 31 * result + (deleteDate != null ? deleteDate.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
