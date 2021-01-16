package lexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: xiaofine
 */
public class Lexer {
    String sourceCode;
    //the linenum of code executed
    Integer lineNum = 1;
    //Current code location
    int head = 0;
    TokenInfo nextTokenInfo;

    static Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("print", TokenType.TOKEN_PRINT);
    }

    public Lexer(String sourceCode, TokenInfo nextTokenInfo) {
        this.sourceCode = sourceCode;
        this.nextTokenInfo = nextTokenInfo;
    }

    public boolean nextSourceCodeIs(String s) {
        return sourceCode.substring(head).startsWith(s);
    }

    public boolean isLetter(String s) {
        return Character.isLetter(s.toCharArray()[0]);
    }

    public String scanName(String s) {
        return scanPattern("^[_a-zA-Z][_a-zA-Z0-9]*");
    }

    public String scanPattern(String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(this.sourceCode.substring(head));
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            throw new RuntimeException("scanPattern:unexpected result "+this.sourceCode.substring(head)+" for"+pattern);
        }
    }

    public boolean isIgnored(String s) {
        List<String> list = Arrays.asList("\t", "\n", "\f", "\r", " ");
        return list.stream().anyMatch(item -> item.equals(s));
    }

    public String scanIgnored() {
        return scanPattern("^[\t \n \f \r]+");
    }

    public void processNewLine(String igonred) {
        while (igonred.length() > 0) {
            if (igonred.length() >= 2 && (igonred.substring(0, 1).equals("\r\n") || igonred.substring(0, 1).equals("\n\r"))) {
                igonred = igonred.substring(2, igonred.length());
                lineNum += 1;
            } else {
                if (igonred.substring(0, 1).equals("\r") || igonred.substring(0, 1).equals("\n") || igonred.substring(0, 1).equals(" ")) {
                    lineNum += 1;
                }
                igonred = igonred.substring(1, igonred.length());
            }
        }
    }

    public TokenInfo NextTokenIs(TokenType tokenType) {
        TokenInfo nextTokenInfo = this.GetNextToken();
        if (nextTokenInfo.tokenType != tokenType) {
            throw new RuntimeException("NextTokenIs：expected token:" + tokenType + "but got:" + nextTokenInfo.tokenType);
        }
        return nextTokenInfo;
    }

    public TokenInfo LookAhead() {
        // nextToken already setted
        if (this.nextTokenInfo == null) {
            this.nextTokenInfo = GetNextToken();
        }
        return this.nextTokenInfo;
    }

    public String scanBeforeToken(String token) {
        String[] split = this.sourceCode.substring(head).split(token);
        if (split == null || split.length == 0) {
            throw new RuntimeException("scanBeforeToken：unreachable content before token:"+token);
        }
        head += split[0].length();
        processNewLine(split[0]);
        return split[0];
    }

    public boolean finished() {
        return head >= sourceCode.length();
    }

    public TokenInfo GetNextToken() {

        if (this.nextTokenInfo != null) {
            TokenInfo nextTokenInfo1 = this.nextTokenInfo;
            this.nextTokenInfo = null;
            return nextTokenInfo1;
        }

        if (finished()) {
            return new TokenInfo(this.lineNum, "EOF", TokenType.TOKEN_EOF);
        }

        String nextchr = this.sourceCode.substring(head, head + 1);
        switch (nextchr) {
            case "$":
                head++;
                return new TokenInfo(this.lineNum, "$", TokenType.TOKEN_VAR_PREFIX);
            case "(":
                head++;
                return new TokenInfo(this.lineNum, "(", TokenType.TOKEN_LEFT_PAREN);
            case ")":
                head++;
                return new TokenInfo(this.lineNum, ")", TokenType.TOKEN_RIGHT_PAREN);
            case "=":
                head++;
                return new TokenInfo(this.lineNum, "=", TokenType.TOKEN_EQUAL);
            case "\"":
                if (nextSourceCodeIs("\"\"")) {
                    head += 2;
                    return new TokenInfo(this.lineNum, "\"\"", TokenType.TOKEN_DUOQUOTE);
                } else {
                    head++;
                    return new TokenInfo(this.lineNum, "\"", TokenType.TOKEN_QUOTE);
                }
            default:
                //判断字符和print
                if (nextchr.equals("_") || isLetter(nextchr)) {
                    String token = scanName(nextchr);
                    if (KEYWORDS.get(token) != null) {
                        head += token.length();
                        return new TokenInfo(this.lineNum, token, KEYWORDS.get(token));
                    } else {
                        head += token.length();
                        return new TokenInfo(this.lineNum, token, TokenType.TOKEN_NAME);
                    }
                }
                if (isIgnored(nextchr)) {
                    String ignored = scanIgnored();
                    head += ignored.length();
                    processNewLine(ignored);
                    return new TokenInfo(this.lineNum, "Ignored", TokenType.TOKEN_IGNORED);
                }
        }
        throw new RuntimeException("MatchToken:unmatched token:" + nextchr);
    }
}
