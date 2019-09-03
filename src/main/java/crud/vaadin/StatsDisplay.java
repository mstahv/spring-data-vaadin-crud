package crud.vaadin;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import crud.backend.PersonRepository;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

/**
 * An view that shows how EventBus can help to decouple UI components.
 * StatsDislay listens PersonModifiedEvevent events fired by PersonForm, without
 * a direct dependency to the PersonForm. PersonForm or any other UI class don't
 * need to know that StatsDisplay is interested about those changes. Also, if
 * another view is introduced to edit Person objects, it can just emit the
 * events and again, no changes are needed for StatsDisplay component.
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
