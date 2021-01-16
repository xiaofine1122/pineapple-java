package definition;

/**
 * @author: xiaofine
 */
public class Print implements Statement {
    public Integer LineNum;
    public Variable variable;

    public Print(Integer lineNum, Variable variable) {
        LineNum = lineNum;
        this.variable = variable;
    }

    @Override
    public String toString() {
        return "Print{" +
                "LineNum=" + LineNum +
                ", variable=" + variable +
                '}';
    }
}
