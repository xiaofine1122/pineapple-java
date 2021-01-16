package lexer;

/**
 * @author: xiaofine
 */
public class TokenInfo {
    Integer lineNum;
    String token;
    TokenType tokenType;

    public TokenInfo(Integer lineNum, String token, TokenType tokenType) {
        this.token = token;
        this.lineNum = lineNum;
        this.tokenType = tokenType;
    }
}
