package vklonin;

import com.codeborne.selenide.SelenideElement;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.comparison.ImageMarkupPolicy;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SimpleAShotTests extends TestBase {

    static String resourcesPath = "/Users/vladimirklonin/IdeaProjects/simple_screenshots_tests/src/test/resources";
    static String tempPath = "/Users/vladimirklonin/IdeaProjects/simple_screenshots_tests/build/tmp/test";

    @BeforeAll //В реальном проекте скриншоты надо положить заранее и не делать их перед тестом
    static void makeScreenshotsForFurtherComparison() throws IOException {

        open("https://allure.autotests.cloud/");
        $("input[name=username]").setValue(ConfigTests.credentialsConfig.userLogin());
        $("input[name=password]").setValue(ConfigTests.credentialsConfig.userPassword());
        $("button[type=submit]").click();

        $(byText("AndreiK_UItests_ex")).click();

        sleep(500);

        Screenshot actualScreenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(500))
                .takeScreenshot(getWebDriver());
        ImageIO.write(actualScreenshot.getImage(), "bmp", new File(resourcesPath + "/screenValidation.png"));

        SelenideElement myWebElement = $$(".WidgetOld").get(0);
        File actualFile = myWebElement.getScreenshotAs(OutputType.FILE);
        BufferedImage actualImage = ImageIO.read(actualFile);
        FileUtils.copyFile(actualFile, new File(resourcesPath + "/elementValidation.png"));

        $(".FloatingMenuWithTrigger").click();
        $(byText("Sign out")).click();
    }

    @Test
    void testIfScreenIsSame() throws IOException {

        open("https://allure.autotests.cloud/");
        $("input[name=username]").setValue(ConfigTests.credentialsConfig.userLogin());
        $("input[name=password]").setValue(ConfigTests.credentialsConfig.userPassword());
        $("button[type=submit]").click();

        $(byText("AndreiK_UItests_ex")).click();

        sleep(500);

        Screenshot expectedScreenshot = new Screenshot(ImageIO.read(new File(resourcesPath + "/screenValidation.png")));

        Screenshot actualScreenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(500))
                .takeScreenshot(getWebDriver());

        ImageIO.write(actualScreenshot.getImage(), "bmp", new File(tempPath + "/screenToValidate.png"));

        ImageDiff diff = new ImageDiffer()
                .withDiffMarkupPolicy(new ImageMarkupPolicy().withDiffColor(Color.RED))
                .makeDiff(expectedScreenshot, actualScreenshot);

        ImageIO.write(diff.getMarkedImage(), "bmp", new File(resourcesPath + "/diff_screen.png"));

        $(".FloatingMenuWithTrigger").click();
        $(byText("Sign out")).click();

        assertFalse(diff.withDiffSizeTrigger(300).hasDiff());



    }

    @Test
    void testIfElementIsSame() throws IOException {

        open("https://allure.autotests.cloud/");
        $("input[name=username]").setValue(ConfigTests.credentialsConfig.userLogin());
        $("input[name=password]").setValue(ConfigTests.credentialsConfig.userPassword());
        $("button[type=submit]").click();

        $(byText("AndreiK_UItests_ex")).click();

        SelenideElement myWebElement = $$(".WidgetOld").get(0);

        sleep(300);

        File actualFile = myWebElement.getScreenshotAs(OutputType.FILE);
        BufferedImage actualImage = ImageIO.read(actualFile);
        FileUtils.copyFile(actualFile, new File(tempPath + "/elementToValidate.png"));

        Screenshot actualScreenshot = new Screenshot(ImageIO.read(new File(tempPath + "/elementToValidate.png")));
        Screenshot expectedScreenshot = new Screenshot(ImageIO.read(new File(resourcesPath + "/elementValidation.png")));

        ImageDiff diff = new ImageDiffer()
                .withDiffMarkupPolicy(new ImageMarkupPolicy().withDiffColor(Color.RED))
                .makeDiff(expectedScreenshot, actualScreenshot);

        ImageIO.write(diff.getMarkedImage(), "png", new File(resourcesPath + "/diff_element.png"));

        $(".FloatingMenuWithTrigger").click();
        $(byText("Sign out")).click();

        assertFalse(diff.withDiffSizeTrigger(10).hasDiff());

    }


}
