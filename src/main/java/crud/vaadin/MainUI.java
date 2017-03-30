package crud.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.event.SortEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import crud.backend.Person;
import crud.backend.PersonRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
            //.setSortableProperties("name", "email")
            .withFullWidth();

    private MTextField filterByName = new MTextField()
            .withPlaceholder("Filter by name");
    private Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Are you sure you want to delete the entry?", this::remove);

    public MainUI(PersonRepository r, PersonForm f, EventBus.UIEventBus b) {
        this.repo = r;
        this.personForm = f;
        this.eventBus = b;
    }

    @Override
    protected void init(VaadinRequest request) {
        DisclosurePanel aboutBox = new DisclosurePanel("Spring Boot JPA CRUD example with Vaadin UI", new RichText().withMarkDownResource("/welcome.md"));
        setContent(
                new MVerticalLayout(
                        aboutBox,
                        new MHorizontalLayout(filterByName, addNew, edit, delete),
                        list
                ).expand(list)
        );
        listEntities();

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(e -> {
            listEntities(e.getValue());
        });

        // Listen to change events emitted by PersonForm see onEvent method
        eventBus.subscribe(this);
    }

    protected void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void listEntities() {
        listEntities(filterByName.getValue());
    }

    final int PAGESIZE = 45;

    private void listEntities(String nameFilter) {
        // A dead simple in memory listing would be:
        // list.setRows(repo.findAll());

        // But we want to support filtering, first add the % marks for SQL name query
        String likeFilter = "%" + nameFilter + "%";
        list.setRows(repo.findByNameLikeIgnoreCase(likeFilter));

        // Lazy binding for better optimized connection from the Vaadin Table to
        // Spring Repository. This approach uses less memory and database
        // resources. Use this approach if you expect you'll have lots of data 
        // in your table. There are simpler APIs if you don't need sorting.
        //list.setDataProvider(
        //        // entity fetching strategy
        //        (sortOrder, offset, limit) -> {
        //            final List<Person> page = repo.findByNameLikeIgnoreCase(likeFilter,
        //                    new PageRequest(
        //                            offset / limit,
        //                            limit,
        //                            sortOrder.isEmpty() || sortOrder.get(0).getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
        //                            // fall back to id as "natural order"
        //                            sortOrder.isEmpty() ? "id" : sortOrder.get(0).getSorted()
        //                    )
        //            );
        //            return page.subList(offset % limit, page.size()).stream();
        //        },
        //        // count fetching strategy
        //        () -> (int) repo.countByNameLike(likeFilter)
        //);
        adjustActionButtonState();

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
        listEntities();
    }

    protected void edit(final Person phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(PersonModifiedEvent event) {
        listEntities();
        personForm.closePopup();
    }

}
