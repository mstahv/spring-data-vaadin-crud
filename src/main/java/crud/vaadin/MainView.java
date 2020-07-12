package crud.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import crud.backend.Person;
import crud.backend.PersonRepository;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

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
