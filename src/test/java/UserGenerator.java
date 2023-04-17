import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static User getRandom() {
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        String email = generatedString + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String userName = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, userName);
    }
}
