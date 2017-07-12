package ru.stqa.pft.addressbook.tests;


import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase{

    @Test
    public void testContactModification(){
        app.getNavigationHelper().goToHomePage();
        if (!app.getContactHelper().isThereAContact()){
            app.getNavigationHelper().goToContactPage();
            app.getContactHelper().createContact(new ContactData("Andrey", "Nikolaevich", "Zakrenichnyy", "", "zik2004@mail.ru", "1234567" , "test1"), true);
        }
        app.getContactHelper().initContactModification();
        app.getContactHelper().fillContactForm(new ContactData("No_name", "No_name", "No_name", "No_adress", "No_email", "No_phone", null), false);
        app.getContactHelper().submitContactModification();

    }
}
