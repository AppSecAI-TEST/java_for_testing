package ru.stqa.pft.addressbook.appmanager;



import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContactHelper extends HelperBase{


    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void returnToHomePage() {
        click(By.xpath("//div/div[4]/div/i/a[2]"));
    }

    public void submitContactCreation() {
        click(By.xpath("//div[@id='content']/form/input[21]"));
    }

    public void fillContactForm(ContactData contactData, boolean createion) {
        type(By.name("firstname"),contactData.getFirstName());
        type(By.name("middlename"),contactData.getMiddleName());
        type(By.name("lastname"),contactData.getLastName());
        type(By.name("address"),contactData.getAddress());
        type(By.name("email"),contactData.getEmail());
        type(By.name("email2"),contactData.getEmail2());
        type(By.name("email3"),contactData.getEmail3());
        type(By.name("home"),contactData.getHomePhone());
        type(By.name("mobile"),contactData.getMobilePhone());
        type(By.name("work"),contactData.getWorkPhone());
        attach(By.name("photo"), contactData.getPhoto());

        if (createion){
            if (contactData.getGroups().size() > 0){
                Assert.assertTrue(contactData.getGroups().size()==1);
                new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
            }
        } else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void selectContactById(int id) {
        WebElement sContact = wd.findElement(By.id(""+id+""));
        if (!sContact.isSelected()) {
            sContact.click();
        }
    }

    public void deleteSelectedContacts() {
        click(By.xpath("//div[@id='content']/form[2]/div[2]/input"));
    }

    public void acceptAlert() {
        wd.switchTo().alert().accept();
    }

    public void initContactModification(int id) {
        WebElement sContact = wd.findElement(By.xpath("//*[@id='"+ id +"']/../..//img[@title='Edit']"));
        if (!sContact.isSelected()) {
            sContact.click();
        }
        //wd.findElements(By.xpath("//table[@id='maintable']//img[@title='Edit']")).get(index).click();
    }

    public void submitContactModification() {
        click(By.name("update"));
    }

    public void create(ContactData contact, boolean b) {
        fillContactForm(contact, b);
        submitContactCreation();
        contactCache = null;
        returnToHomePage();
    }

    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteSelectedContacts();
        acceptAlert();
        contactCache = null;
    }

    public void modify(ContactData contact) {
        selectContactById(contact.getId());
        initContactModification(contact.getId());
        fillContactForm(contact, false);
        submitContactModification();
        contactCache = null;
    }

     public void selectGroupToAdd(GroupData group) {
         new Select(wd.findElement(By.name("to_group"))).selectByVisibleText(group.getName());
     }

    public void submitContactAddToGroup() {
        click(By.name("add"));
    }

    public List<ContactData> list() {
       List<ContactData> contacts = new ArrayList<ContactData>();
       List<WebElement> elements = wd.findElements(By.name("entry"));

        for (WebElement el : elements){
            List<WebElement> cells = el.findElements(By.tagName("td"));
            String lastName = cells.get(1).getText();
            String firstName = cells.get(2).getText();
            ContactData contact = new ContactData().withFirstName(firstName).withLastName(lastName);
            contacts.add(contact);
        }
       return contacts;
    }

    public int count() {
        return wd.findElements(By.name("selected[]")).size();
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null){
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.name("entry"));
        for (WebElement el : elements){
            List<WebElement> cells = el.findElements(By.tagName("td"));
            String lastName = cells.get(1).getText();
            String firstName = cells.get(2).getText();
            String address = cells.get(3).getText();
            String allphones = cells.get(5).getText();
            String allemails = cells.get(4).getText();
            int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("id"));
            contactCache.add(new ContactData().withId(id).withFirstName(firstName).withLastName(lastName).withAllPhones(allphones).withAddress(address).withAllEmails(allemails));
        }
        return new Contacts(contactCache);
    }

    public ContactData infoFromEditForm(ContactData contact) {
        selectContactById(contact.getId());
        initContactModification(contact.getId());
        String firstname = get(By.name("firstname"));
        String lastname = get(By.name("lastname"));
        String home = get(By.name("home"));
        String mobile = get(By.name("mobile"));
        String work = get(By.name("work"));
        String email = get(By.name("email"));
        String email2 = get(By.name("email2"));
        String email3 = get(By.name("email3"));
        String address = get(By.name("address"));
        return new ContactData().withId(contact.getId()).withFirstName(firstname).withLastName(lastname).withHomePhone(home).withMobilePhone(mobile).
                withWorkPhone(work).withEmail(email).withEmail2(email2).withEmail3(email3).withAddress(address);
    }

    public void filterContactsByGroup(GroupData group) {
        new Select(wd.findElement(By.name("group"))).selectByVisibleText(group.getName());
    }

    public void deleteContactFromGroup() {
        click(By.name("remove"));
    }
}
