package ui.scripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ui.pages.TGBackendPage;
import utils.DBUtil;
import utils.Waiter;

import java.util.List;


public class TGApplicationUIDB extends Base{
    String firstName;
    String lastName;
    String email;

    @BeforeMethod
    public void setPage(){
        driver.get("https://techglobal-training.com/backend");
        tgBackendPage = new TGBackendPage();

        DBUtil.createDBConnection();
    }

    @Test
    public void validateDBbyUI(){

        firstName = "Jane";
        lastName = "Doe";
        email = "janedoe@gmail.com";

        tgBackendPage.firstNameInput.sendKeys(firstName);
        tgBackendPage.lastNameInput.sendKeys(lastName);
        tgBackendPage.emailInput.sendKeys(email);
        tgBackendPage.dobInput.sendKeys("01012000");

        tgBackendPage.addButton.click();

        Waiter.pause(2);

        String query = "SELECT * FROM student WHERE email = '" + email + "'";

        List<List<Object>> queryResultList = DBUtil.getQueryResultList(query);

        List<Object> row = queryResultList.get(0);

        for (Object o : row) {
            System.out.println(o);
        }

        Assert.assertEquals(row.get(3), firstName);
        Assert.assertEquals(row.get(4), lastName);
        Assert.assertEquals(row.get(2), email);
    }
    @Test
    public void validateDatabaseByUIDataIsRemoved(){

//        tgBackendPage.namesAndLastNames.get(0).findElement()

        String name1 = firstName + " " + lastName;
        System.out.println(name1 + " NAME AND THE LAST NAME");

        List<WebElement> divs = tgBackendPage.namesAndLastNames;

        for (WebElement div : divs) {
            WebElement name = div.findElement(By.cssSelector(".common_nameAndLastname__zhZvk"));
            System.out.println(name.getText());

            if(name.getText().equals(name1)){
                System.out.println(name.getText() + "name we are looking for");
                WebElement deleteButton = div.findElement(By.xpath(".//button[contains(text(),'DELETE')]"));
                System.out.println(deleteButton.getText());
                deleteButton.click();
                break;
            }
        }

        Waiter.pause(3);

        String query = "SELECT * FROM student WHERE email = '" + email + "'";

        List<List<Object>> queryResultList = DBUtil.getQueryResultList(query);

        Assert.assertTrue(queryResultList.isEmpty(), " The query is still on the database");
    }
}
