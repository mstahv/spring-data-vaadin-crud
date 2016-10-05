package crud.vaadin;

import crud.backend.Person;
import java.io.Serializable;

public class PersonModifiedEvent implements Serializable {

    private final Person person;

    public PersonModifiedEvent(Person p) {
        this.person = p;
    }

    public Person getPerson() {
        return person;
    }
    
}
