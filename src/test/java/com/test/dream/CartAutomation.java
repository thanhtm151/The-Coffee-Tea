package com.test.dream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class CartAutomation {
    public static void main(String[] args) {
        System.setProperty("webdriver.edge.driver", "D:\\Dream\\msedgedriver.exe");
        WebDriver driver = new EdgeDriver();

        try {
            driver.get("http://localhost:8090/home");
            driver.manage().window().maximize();

            WebElement modal = driver.findElement(By.id("discountModal"));


            WebElement closeButton = modal.findElement(By.className("custom-close-btn"));
            closeButton.click();
            // Sleep after close the modal
            Thread.sleep(1000);
            WebElement loginBtn = driver.findElement(By.id("link-login"));
            Actions act = new Actions(driver);
            act.click(loginBtn).perform();
            // Sleep after click the login button
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("cuong");
            driver.findElement(By.id("password")).sendKeys("123");
            WebElement BtnLogin = driver.findElement(By.id("btnLogin"));
            act.click(BtnLogin).perform();
            Thread.sleep(3000);
            WebElement modalAfterLogin = driver.findElement(By.id("discountModal"));
            if (modalAfterLogin.isDisplayed()) {
                WebElement closButton = modalAfterLogin.findElement(By.className("custom-close-btn"));
                closButton.click();
                Thread.sleep(3000);
            }

            WebElement storeBtn = driver.findElement(By.id("link-store"));
            act.click(storeBtn).perform();
            Thread.sleep(2000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, 700)");
            Thread.sleep(1000);
            WebElement addToCartButton = driver.findElement(By.id("addToCart_9"));
            addToCartButton.click();

            Thread.sleep(2000);
            WebElement moreDetailButton = driver.findElement(By.id("moreDetail_9"));
            moreDetailButton.click();
            Thread.sleep(3000);
            js.executeScript("window.scrollBy(0, 400)");
            Thread.sleep(2000);
            js.executeScript("document.getElementById('size-2').click();");

            WebElement addToCartBtn = driver.findElement(By.id("addToCartButton_9"));
            act.click(addToCartBtn).perform();
            js.executeScript("document.getElementById('size-3').click();");
            for(int i = 0; i < 2; i++) {
                act.click(addToCartBtn).perform();
            }
            WebElement showCart = driver.findElement(By.id("link-cart"));
            act.click(showCart).perform();
            Thread.sleep(2000);

            js.executeScript("window.scrollBy(0, 300)");

            WebElement quantityInput = driver.findElement(By.xpath("(//input[@type='number'])[1]")); // Xác định input số lượng của sản phẩm đầu tiên
            quantityInput.clear(); // Xóa giá trị hiện tại trong ô input
            quantityInput.sendKeys("3"); // Gửi số lượng mới vào ô input
            Thread.sleep(2000);
            quantityInput.clear();
            quantityInput.sendKeys("2");
            Thread.sleep(2000);
            WebElement removeButton = driver.findElement(By.xpath("(//button[@ng-click='cart.remove(product.id)'])[1]")); // Xác định nút xóa sản phẩm đầu tiên
            act.click(removeButton).perform();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
