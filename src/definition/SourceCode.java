package definition;

import java.util.List;

/**
 * @author: xiaofine
 */
public class SourceCode {
    public Integer LineNum;
    public List<Statement> statements;

    public SourceCode(Integer lineNum, List<Statement> statements) {
        LineNum = lineNum;
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "SourceCode{" +
                "LineNum=" + LineNum +
                ", statements=" + statements +
                '}';
    }
}
