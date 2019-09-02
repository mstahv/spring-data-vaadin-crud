package crud.vaadin;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import crud.backend.PersonRepository;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 * An other example class that listens PersonModifiedEvevent events fired by
 * PersonForm, without a direct dependency to the PersonForm.
 */
@UIScope
@SpringComponent
public class StatsDisplay extends VerticalLayout {

    PersonRepository repository;

    public StatsDisplay(PersonRepository repository) {
        this.repository = repository;
        updateStats();
    }

    private void updateStats() {
        removeAll();
        add(new Paragraph("You have " + repository.countByColleagueTrue() + " colleagues in your addressbook."));
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(PersonModifiedEvent event) {
        updateStats();
    }

}
