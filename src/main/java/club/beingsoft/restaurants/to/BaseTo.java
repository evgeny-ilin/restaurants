package club.beingsoft.restaurants.to;


import club.beingsoft.restaurants.util.HasId;

public abstract class BaseTo implements HasId {
    protected Integer id;

    protected BaseTo() {
    }

    protected BaseTo(Integer id
    ) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseTo)) return false;

        BaseTo baseTo = (BaseTo) o;

        return id != null ? id.equals(baseTo.id) : baseTo.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
