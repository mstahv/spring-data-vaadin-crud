package crud.vaadin;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import crud.backend.Person;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

public class PhoneBookEntryForm extends AbstractForm<Person> {

    TextField name = new MTextField("Name");
    TextField email = new MTextField("Email");
    TextField phoneNumber = new MTextField("Phone");
    DateField birthDay = new DateField("Birthday");
    CheckBox colleague = new CheckBox("Colleague");

    PhoneBookEntryForm(Person phoneBookEntry) {
        setSizeUndefined();
        setEagarValidation(true);
        setEntity(phoneBookEntry);
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        email,
                        phoneNumber,
                        birthDay,
                        colleague
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

}
