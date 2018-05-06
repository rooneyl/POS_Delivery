package pos_delivery.model;

/**
 * Menu Entity Represent Menu Items.
 */
public class Menu {

    /**
     * Uid is Unique Identifier of Menu
     **/
    private int uid;

    /**
     * Name Of Menu
     **/
    private String name;

    /**
     * Decription Is Used To Represent Name In UI
     **/
    private String description;

    /**
     * Category Of Menu
     **/
    private Category category;

    /**
     * Printer That Need To Print When Order Is Placed
     **/
    private String printer;

    /**
     * Menu Gets Invalid When It is Deleted
     **/
    private boolean valid;

    /**
     * Position Of Menu Button On POS Application
     **/
    private int position;

    /**
     * Color Of Menu Button On POS Application For UI
     **/
    private String color;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (uid != menu.uid) return false;
        return name.equals(menu.name);
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}