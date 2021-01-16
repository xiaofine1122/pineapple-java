package definition;

/**
 * @author: xiaofine
 */
public class Variable {
    public Integer line_num;
    public String name;

    public Variable(Integer line_num, String name) {
        this.line_num = line_num;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "line_num=" + line_num +
                ", name='" + name + '\'' +
                '}';
    }
}
