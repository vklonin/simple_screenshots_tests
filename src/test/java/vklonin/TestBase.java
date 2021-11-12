package vklonin;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
        Configuration.browser = "safari";
    }

}
