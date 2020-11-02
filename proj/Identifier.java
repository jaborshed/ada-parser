package proj;

public class Identifier {
    private String type;
    private String name;

    public Identifier(String name) {
        this.name = name;
    }

    public Identifier(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getName() + "  " + this.getType();
    }

    @Override
    public boolean equals(Object o) {
        // System.out.println("/" + o + "/");
        // if (!(o instanceof Identifier)) return false;
        // System.out.println(o);
        // if (o instanceof String) {
        // System.out.println("haha");
        // return this.getName() == o;
        // }
        // System.out.println(((Identifier) o).getName());
        return o instanceof Identifier && ((Identifier) o).getName().equals(this.getName());
    }
}