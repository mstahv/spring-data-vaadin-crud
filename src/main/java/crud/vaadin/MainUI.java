package crud.vaadin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.data.provider.Sort;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import crud.backend.Person;
import crud.backend.PersonRepository;

@Title("PhoneBook CRUD example")
@Theme("valo")
@SpringUI
public class MainUI extends UI {

    private static final long serialVersionUID = 1L;

    PersonRepository repo;
    PersonForm personForm;
    EventBus.UIEventBus eventBus;

    private MGrid<Person> list = new MGrid<>(Person.class)
            .withProperties("id", "name", "email")
            .withColumnHeaders("id", "Name", "Email")
            // not yet supported by V8
            // .setSortableProperties("name", "email")
            .withFullWidth();

    private MTextField filterByName = new MTextField()
            .withPlaceholder("Filter by name");
    private Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Are you sure you want to delete the entry?", this::remove);

    FilterablePageableDataProvider<Person, Object> dataProvider = new FilterablePageableDataProvider<Person, Object>() {
        @Override
        protected Page<Person> fetchFromBackEnd(Query<Person, Object> query,
                Pageable pageable) {
            return repo.findByNameLikeIgnoreCase(getRepoFilter(), pageable);
        }

        @Override
        protected int sizeInBackEnd(Query<Person, Object> query) {
            return (int) repo.countByNameLikeIgnoreCase(getRepoFilter());
        }

        private String getRepoFilter() {
            String filter = getOptionalFilter().orElse("");
            return "%" + filter + "%";
        }

        @Override
        protected List<QuerySortOrder> getDefaultSortOrders() {
            return Sort.asc("name").build();
        }

    };

    public MainUI(PersonRepository r, PersonForm f, EventBus.UIEventBus b) {
        repo = r;
        personForm = f;
        eventBus = b;
    }

    @Override
    protected void init(VaadinRequest request) {
        DisclosurePanel aboutBox = new DisclosurePanel(
                "Spring Boot JPA CRUD example with Vaadin UI",
                new RichText().withMarkDownResource("/welcome.md"));
        setContent(new MVerticalLayout(aboutBox,
                new MHorizontalLayout(filterByName, addNew, edit, delete), list)
                        .expand(list));

        list.setDataProvider(dataProvider);
        list.asSingleSelect()
                .addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(e -> {
            setFilter(e.getValue());
        });

        // Listen to change events emitted by PersonForm see onEvent method
        eventBus.subscribe(this);
    }

    protected void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    public void add(ClickEvent clickEvent) {
        edit(new Person());
    }

    public void edit(ClickEvent e) {
        edit(list.asSingleSelect().getValue());
    }

    public void remove() {
        repo.delete(list.asSingleSelect().getValue());
        list.deselectAll();
        setFilter("");
    }

    private void setFilter(String filter) {
        dataProvider.setFilter(filter);
        adjustActionButtonState();
    }

    protected void edit(final Person phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(PersonModifiedEvent event) {
        setFilter("");
        personForm.closePopup();
    }

}
