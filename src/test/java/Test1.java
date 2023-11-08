import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
    public static void main(String[] args) {
        String input = "This is a sample text!Hello12345";
        String regex = "!(\\w+)"; // 正则表达式，匹配'!'后的字母和数字

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group(1); // 获取匹配的内容（不包括'!'）
            System.out.println("Match: " + match);
        }
    }
}
