package crud.vaadin;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import crud.backend.Person;
import crud.backend.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBus.UIEventBus;
import org.vaadin.teemu.switchui.Switch;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class PersonForm extends AbstractForm<Person> {

    EventBus.UIEventBus eventBus;
    PersonRepository repo;

    TextField name = new MTextField("Name");
    TextField email = new MTextField("Email");
    TextField phoneNumber = new MTextField("Phone");
    DateField birthDay = new DateField("Birthday");
    // Typically you'd use std CheckBox, using Swithch to demonstrate
    // the awesome extendions by the community: http://vaadin.com/directory
    Switch colleague = new Switch("Colleague");

    @Autowired
    PersonForm(PersonRepository r, EventBus.UIEventBus b) {
        this.repo = r;
        this.eventBus = b;

        // On save & cancel, publish events that other parts of the UI can listen
        setSavedHandler(person -> {
            // persist changes
            repo.save(person);
            // send the event for other parts of the application
            eventBus.publish(this, new PersonModifiedEvent(person));
        });
        setResetHandler(p -> eventBus.publish(this, new PersonModifiedEvent(p)));

        setSizeUndefined();
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
