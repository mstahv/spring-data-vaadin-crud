package crud.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import crud.backend.Person;
import crud.backend.PersonRepository;
import org.vaadin.firitin.components.formlayout.VFormLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.firitin.form.AbstractForm;
import org.vaadin.spring.events.EventBus;

@UIScope
@SpringComponent
public class PersonForm extends AbstractForm<Person> {

    private static final long serialVersionUID = 1L;

    EventBus.UIEventBus eventBus;
    PersonRepository repo;

    TextField name = new VTextField("Name");
    TextField email = new VTextField("Email");
    TextField phoneNumber = new VTextField("Phone");
    DatePicker birthDay = new DatePicker("Birthday");
    Checkbox colleague = new Checkbox("Colleague");

    PersonForm(PersonRepository r, EventBus.UIEventBus b) {
        super(Person.class);
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
        
    }

    @Override
    protected Component createContent() {
        return new VVerticalLayout(
                new VFormLayout(
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
