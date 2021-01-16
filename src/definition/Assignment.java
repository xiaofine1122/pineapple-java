package definition;

/**
 * @author: xiaofine
 */
public class Assignment implements Statement {
    public Integer LineNum;
    public Variable variable;
    public String string;

    public Assignment(Integer lineNum, Variable variable, String string) {
        LineNum = lineNum;
        this.variable = variable;
        this.string = string;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "LineNum=" + LineNum +
                ", variable=" + variable +
                ", string='" + string + '\'' +
                '}';
    }
}
