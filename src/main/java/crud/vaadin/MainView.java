package crud.vaadin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import crud.backend.PersonRepository;

@Route
public class MainView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    PersonRepository repo;

    public MainView(PersonRepository r) {
        this.repo = r;
        
        // TODO
        // 1. List all persons from the repository in a Grid
        // 2. Make the data binding lazy loaded
        // 3. Configure the data binding so that the initial estimate of the person count is 1000
        // 4. Make the name column sortable, others non-sortable
    }

}
