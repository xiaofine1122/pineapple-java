package lexer;

import definition.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xiaofine
 */
public class Parse {

    //// Name ::= [_A-Za-z][_0-9A-Za-z]*
//    public static String parseName(Lexer lexer){
//        TokenInfo tokenInfo = lexer.NextTokenIs(TokenType.TOKEN_NAME);
//        return tokenInfo.token;
//    }

    //
    public static void parseIgnored(Lexer lexer) {
        if (lexer.LookAhead().tokenType == TokenType.TOKEN_IGNORED) {
            lexer.NextTokenIs(TokenType.TOKEN_IGNORED);
        }
    }

    //Variable ::= "$" Name Ignored
    public static Variable parseVariable(Lexer lexer) {
        Integer lineNum = lexer.NextTokenIs(TokenType.TOKEN_VAR_PREFIX).lineNum;
        String name = lexer.NextTokenIs(TokenType.TOKEN_NAME).token;
        parseIgnored(lexer);
        return new Variable(lineNum, name);
    }

    //String ::= '"' '"' Ignored | '"' StringCharacter '"' Ignored
    public static String parseString(Lexer lexer) {
        if (lexer.LookAhead().tokenType == TokenType.TOKEN_DUOQUOTE) {
            lexer.NextTokenIs(TokenType.TOKEN_DUOQUOTE);
            return "";
        }
        lexer.NextTokenIs(TokenType.TOKEN_QUOTE);
        String s = lexer.scanBeforeToken("\"");
        lexer.NextTokenIs(TokenType.TOKEN_QUOTE);
        return s;
    }

    //Assignment  ::= Variable Ignored "=" Ignored String Ignored
    public static Assignment parseAssignment(Lexer lexer) {
        Variable variable = parseVariable(lexer);
        parseIgnored(lexer);
        lexer.NextTokenIs(TokenType.TOKEN_EQUAL);
        parseIgnored(lexer);
        String string = parseString(lexer);
        parseIgnored(lexer);
        return new Assignment(variable.line_num, variable, string);
    }

    //Print ::= "print" "(" Ignored Variable Ignored ")" Ignored
    public static Print parsePrint(Lexer lexer) {
        Integer lineNum = lexer.NextTokenIs(TokenType.TOKEN_PRINT).lineNum;
        lexer.NextTokenIs(TokenType.TOKEN_LEFT_PAREN);
        parseIgnored(lexer);
        Variable variable = parseVariable(lexer);
        parseIgnored(lexer);
        lexer.NextTokenIs(TokenType.TOKEN_RIGHT_PAREN);
        parseIgnored(lexer);
        return new Print(lineNum, variable);
    }

    //Statement ::= Print | Assignment
    public static Statement parseStatement(Lexer lexer) {

        switch (lexer.LookAhead().tokenType) {
            case TOKEN_PRINT:
                return parsePrint(lexer);
            case TOKEN_VAR_PREFIX:
                return parseAssignment(lexer);
            default:
                throw new RuntimeException("parseStatement:unknown Statement");
        }
    }

    public static List<Statement> parseStatements(Lexer lexer) {
        List<Statement> statements = new ArrayList<Statement>();
        Integer lineNum = lexer.lineNum;
        while (!isSourceCodeEnd(lexer.LookAhead().tokenType)) {
            statements.add(parseStatement(lexer));
        }
        return statements;
    }

    //SourceCode ::= Statement+
    public static SourceCode parseSourceCode(Lexer lexer) {
        List<Statement> statements = parseStatements(lexer);
        return new SourceCode(null, statements);
    }


    public static boolean isSourceCodeEnd(TokenType tokenType) {
        if (tokenType == TokenType.TOKEN_EOF) {
            return true;
        } else {
            return false;
        }
    }

}
