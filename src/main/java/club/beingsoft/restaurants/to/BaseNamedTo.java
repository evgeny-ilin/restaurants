package club.beingsoft.restaurants.to;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public abstract class BaseNamedTo extends BaseTo {

    @NotBlank
    @Size(min = 2, max = 100)
    protected String name;

    protected BaseNamedTo() {
    }

    public BaseNamedTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseNamedTo)) return false;
        if (!super.equals(o)) return false;

        BaseNamedTo that = (BaseNamedTo) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
