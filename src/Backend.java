import definition.Assignment;
import definition.Print;
import definition.SourceCode;
import definition.Statement;
import lexer.Lexer;
import lexer.Parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xiaofine
 */
public class Backend {

    Map<String, String> variables = new HashMap<String, String>();


    public void resolveAST(SourceCode sourceCode) {
        resolveSourceCode(sourceCode);
    }

    public void resolveSourceCode(SourceCode sourceCode) {
        List<Statement> statementList = sourceCode.statements;
        if (statementList.size() > 0) {
            for (Statement statement : statementList) {
                resolveStatement(statement);
            }
        } else {
            throw new RuntimeException("resolveSourceCode:no code to execute please check");
        }
    }

    public void resolveStatement(Statement statement) {
        if (statement instanceof Print) {
            resolvePrint((Print) statement);
        } else if (statement instanceof Assignment) {
            resolveAssignment((Assignment) statement);
        } else {
            throw new RuntimeException("resolveStatement:undefined statement type");
        }
    }

    public void resolveAssignment(Assignment assignment) {
        variables.put(assignment.variable.name, assignment.string);
    }

    public void resolvePrint(Print print) {
        System.out.println(variables.get(print.variable.name));
    }


    public static void main(String[] args) throws IOException {
        String fileName = args[0];
//        System.out.println("fileName:" + fileName);

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                result.append(str + "\n");
            }

            Lexer lexer = new Lexer(result.toString(), null);
            SourceCode sourceCode = Parse.parseSourceCode(lexer);
//            System.out.println("sourceCode:\n" + sourceCode);
            Backend backend = new Backend();
            backend.resolveAST(sourceCode);

        } catch (IOException e) {
            System.out.println("read file error:" + e.getMessage());
        } finally {
            in.close();
        }
    }
}
