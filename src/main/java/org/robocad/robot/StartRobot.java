/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.robocad.robot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import static org.robocad.login.TechnocadLogin.baseURL;
import static org.robocad.login.TechnocadLogin.technocadPassword;
import static org.robocad.login.TechnocadLogin.technocadUser;
import org.robocad.settings.setting;
import org.robocad.sql.select;
import org.robocad.sql.update;
import org.robocad.valueobject.RobolistValue;

/**
 *
 * @author shrv
 */
public class StartRobot {

    private final String pathYandexMetric = "/xpi/addon-block-YandexMetric.xpi";
    private final String pathGoogleMetric = "/xpi/gaoptoutaddon_1.0.7.xpi";
    private String sRn;
    select sel = new select();
    update upd = new update();

    public FirefoxDriver start() {
        System.setProperty("webdriver.gecko.driver", setting.pathToJar + "/driver/geckodriver.exe");
        FirefoxDriver driver = null;
        try {
            FirefoxOptions options = new FirefoxOptions();
            FirefoxProfile profile = new FirefoxProfile();
            profile.addExtension(new File(setting.pathToJar + pathYandexMetric));
            profile.addExtension(new File(setting.pathToJar + pathGoogleMetric));
            options.setProfile(profile);
            options.addPreference("browser.download.folderList", 2);
            options.addPreference("browser.download.dir", setting.pathToFile);
            options.addPreference("browser.download.useDownloadDir", "true");
            options.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/xml");
            options.addPreference("browser.download.panel.shown", "false");
            options.addPreference("browser.download.lastDir.savePerSite", "false");
            options.addPreference("browser.helperApps.alwaysAsk.force", false);
            options.addPreference("browser.download.manager.showWhenStarting", false);
            driver = new FirefoxDriver(options);
            driver.manage().timeouts().implicitlyWait(60000, TimeUnit.MILLISECONDS);
            login(driver);
        } catch (NoSuchElementException e) {
            if (setting.isLogging) {
                Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return driver;
    }

    private void login(FirefoxDriver driver) {
        try {
            driver.get(baseURL);
            driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div/a[2]")).click();
            driver.findElement(By.id("UserName")).sendKeys(technocadUser);
            driver.findElement(By.id("Password")).sendKeys(technocadPassword);
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/div/form/div[2]/div/div[4]/button")).click();
        } catch (NoSuchElementException e) {
            if (setting.isLogging) {
                Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void request(FirefoxDriver driver, long nRn, String sRegionCode, String sCadNumb) {
        try {
            driver.get(baseURL + "/requestfiromsegrn");
            sRn = "P-" + String.valueOf(nRn);
            driver.findElement(By.id("TitleTab_Title")).sendKeys(sRn);
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[1]/div[4]/span[1]/input")).clear();
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[1]/div[4]/span[1]/input")).sendKeys("68 - Тамбовская область"); //sRegionCode
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[5]/a[2]")).click();
            driver.findElement(By.xpath("//*[@id=\"AreaInformationTab_CadastrNumberBlock_CadastrNumber\"]")).sendKeys(sCadNumb);
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[5]/a[2]")).click();
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[3]/div[18]/div/label/span")).click();
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[3]/div[19]/div/label/span")).click();
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[2]/div[2]/div[5]/a[2]")).click();
            driver.findElement(By.id("createPackage")).click();
        } catch (NoSuchElementException e) {
            if (setting.isLogging) {
                Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        try {
            driver.findElement(By.id("sign")).click();
            upd.updateSql(2, 1, null, nRn);
        } catch (NoSuchElementException e) {
            Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, e);
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[1]"));
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[1]")).findElements(By.tagName("a"));
                List<WebElement> errors = driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/form[1]/div[1]")).findElements(By.tagName("a"));
                String error = "Возникли ошибки на стадии заполнения документа: ";
                int i = 1;
                for (WebElement element : errors) {
                    error += i + ") " + element.getAttribute("innerHTML") + " ";
                    i++;
                }
                upd.updateSql(1, 3, error, nRn);
            } catch (NoSuchElementException ex) {
                if (setting.isLogging) {
                    Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        String S = new String();
    }

    public void responce(FirefoxDriver driver, long nRn, long nCompany) {
        sRn = "P-" + String.valueOf(nRn);
        driver.get(baseURL + "/api/requestsapi/get/?pageNumber=1&pageSize=10&childrenId=&startDate=&endDate=&searchContext=" + sRn + "&authorId=&mode=Active");
        String sJSON = driver.findElement(By.xpath(" html/body")).getText();
        JsonParser parser = new JsonParser();
        JsonObject mainObject = parser.parse(sJSON).getAsJsonObject();
        int TotalRequestsItems = mainObject.get("TotalRequestsItems").getAsInt();
        if (TotalRequestsItems == 0) {
            upd.updateSql(2, 4, "Не найден запрос в технокаде", nRn);
        } else if (TotalRequestsItems == 1) {
            JsonArray Requests = mainObject.getAsJsonArray("Requests");
            mainObject = Requests.get(0).getAsJsonObject();
            String StatusText = mainObject.get("StatusText").getAsString();
            switch (StatusText) {
                case "Получены документы":
                    driver.get(baseURL + "/RequestDetails?requestId=" + mainObject.get("RequestId").getAsString() + "&childrenId=&pageNumber=1");
                    String sURL = driver.findElement(By.id("download-answer-xml")).getAttribute("href");
                    driver.findElement(By.id("download-answer-xml")).click();
                    int index = sURL.indexOf("out_docs");
                    sURL = sURL.substring(index);
                    sURL = sURL.replace('/', '_');
                    try {
                        File file = new File("/home/shrv/Загрузки/" + sURL);
                        InputStream stream = new FileInputStream(file);
                        file.delete();
                        boolean sqlFlag = upd.SendBlob(stream, nRn, sURL, nCompany);
                        if (sqlFlag) {
                            //     driver.findElement(By.xpath("/html/body/div[2]/div[2]/section/div[1]/div[4]/a[4]")).click();
                        }
                    } catch (FileNotFoundException ex) {
                        if (setting.isLogging) {
                            Logger.getLogger(StartRobot.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case "Отправлена":
                case "Загружена":
                case "Ожидает проверки":
                case "Проверка пройдена. Ожидает получения расписки":
                case "Расписка получена. Ожидает получение документов":
                    upd.updateSql(2, 8, null, nRn);
                    break;
                case "Ошибка":
                    upd.updateSql(2, 4, null, nRn);
                    break;
                case "Требует корректировки":
                    upd.updateSql(1, 3, "Требуется корректировка запроса", nRn);
                    break;
                case "Выполнена. Получен отрицательный ответ":
                    upd.updateSql(2, 9, null, nRn);
                    break;
                case "Выполнена. Сведения отсутствуют":
                    upd.updateSql(2, 9, "Выполнена. Сведения отсутствуют", nRn);
                    break;
                case "Не отправлена":
                    upd.updateSql(1, 3, "Пакет не сформирован, подписание не выполнялось. Заявку можно редактировать", nRn);
                    break;
                case "Сформирована и проверена":
                    upd.updateSql(1, 3, "Пакет сформирован, подписание не выполнялось. Заявку можно редактировать, nRn)", nRn);
                    break;
                default:
                    upd.updateSql(1, 3, "Неизвестная ошибка", nRn);
                    break;
            }
        } else {
            upd.updateSql(2, 4, "Найдено больше одного запроса в технокаде", nRn);
        }

        String s = new String();
    }

    public void sendRequest(FirefoxDriver driver) {
        List<RobolistValue> RoboValues = new ArrayList<RobolistValue>();
        RoboValues = sel.sqlSelect(1, 10, 10);
        if (!RoboValues.isEmpty()) {
            for (RobolistValue RoboValue : RoboValues) {
                request(driver, RoboValue.getnRn(), RoboValue.getsRegion_code(), RoboValue.getsCadnumb());
            }
        }
    }

    public void findResponce(FirefoxDriver driver) {
        List<RobolistValue> RoboValues = new ArrayList<RobolistValue>();
        RoboValues = sel.sqlSelect(2, 1, 8);
        if (!RoboValues.isEmpty()) {
            for (RobolistValue RoboValue : RoboValues) {
                responce(driver, RoboValue.getnRn(), RoboValue.getnCompany());
            }
        }

    }
}
