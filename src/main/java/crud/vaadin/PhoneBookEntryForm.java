package crud.vaadin;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import crud.backend.Person;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class PhoneBookEntryForm extends AbstractForm<Person> {

    TextField name = new MTextField("Name");
    TextField email = new MTextField("Email");
    TextField phoneNumber = new MTextField("Phone");
    DateField birthDay = new DateField("Birthday");
    Switch colleague = new Switch("Colleague");

    PhoneBookEntryForm(Person phoneBookEntry) {
        setSizeUndefined();
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
