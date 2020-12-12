package club.beingsoft.restaurants.to;


import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.util.HasId;

import java.time.LocalDateTime;

public abstract class BaseTo implements HasId {
    protected User deleteUser;
    protected LocalDateTime deleteDate;
    protected Integer id;

    protected BaseTo() {
    }

    protected BaseTo(Integer id) {
        this.id = id;
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
}
