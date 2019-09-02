package crud.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import crud.backend.Person;
import crud.backend.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.firitin.button.DeleteButton;
import org.vaadin.firitin.components.DisclosurePanel;
import org.vaadin.firitin.components.RichText;
import org.vaadin.firitin.components.button.VButton;
import org.vaadin.firitin.components.grid.VGrid;
import org.vaadin.firitin.components.orderedlayout.VHorizontalLayout;
import org.vaadin.firitin.components.orderedlayout.VVerticalLayout;
import org.vaadin.firitin.components.textfield.VTextField;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

@Route
public class MainView extends VVerticalLayout {

    private static final long serialVersionUID = 1L;

    PersonRepository repo;
    PersonForm personForm;

    private VGrid<Person> list = new VGrid<>(Person.class)
            .withProperties("id", "name", "email")
            //.setSortableProperties("name", "email")
            .withFullWidth();

    private VTextField filterByName = new VTextField()
            .withPlaceholder("Filter by name");
    private Button addNew = new VButton(VaadinIcon.PLUS.create()).onClick(this::add);
    private Button edit = new VButton(VaadinIcon.PENCIL.create()).onClick(this::edit);
    private DeleteButton delete = new DeleteButton().withIcon(VaadinIcon.TRASH.create())
            .withConfirmHandler(this::remove);

    public MainView(PersonRepository r, PersonForm f, StatsDisplay statsDisplay) {
        this.repo = r;
        this.personForm = f;
        
        DisclosurePanel aboutBox = new DisclosurePanel("Spring Boot JPA CRUD example with Vaadin UI", new RichText().withMarkDownResource("/welcome.md"));

        add(
            aboutBox,
            new VHorizontalLayout(filterByName, addNew, edit, delete)
        );
        addExpanded(list);
        add(statsDisplay);
        listEntities();

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(e -> {
            listEntities(e.getValue());
        });
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

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
        
        // If there is a moderate amount of rows, one can just fetch everything
        //list.setRows(repo.findByNameLikeIgnoreCase(likeFilter));

        // Lazy binding for better optimized connection from the Vaadin Grid to
        // Spring Repository. This approach uses less memory and database
        // resources. Use this approach if you expect you'll have lots of data 
        // in your table. There are simpler APIs if you don't need sorting.s
        list.setDataProvider(
                // lazy entity fetching strategy, due to a design flaw in DataProvider API,
                // this is bit tricky with Spring Data's Pageable abstration as requests
                // by Grid may be on two pages, see https://github.com/vaadin/framework/issues/8982
                // TODO see if this could be simplified in Viritin MGrid
                q -> {
                    final int pageSize = q.getLimit();
                    final int startPage = (int) Math.floor((double) q.getOffset() / pageSize);
                    final int endPage = (int) Math.floor((double) (q.getOffset() + pageSize - 1) / pageSize);
                    final Sort.Direction sortDirection = q.getSortOrders().isEmpty() || q.getSortOrders().get(0).getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
                    // fall back to id as "natural order"
                    
                    final String sortProperty = q.getSortOrders().isEmpty() ? "id" : q.getSortOrders().get(0).getSorted();
                    if (startPage != endPage) {
                        List<Person> page0 = repo.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(startPage, pageSize, sortDirection, sortProperty));
                        page0 = page0.subList(q.getOffset() % pageSize, page0.size());
                        List<Person> page1 = repo.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(endPage, pageSize, sortDirection, sortProperty));
                        page1 = page1.subList(0, pageSize - page0.size());
                        List<Person> result = new ArrayList<>(page0);
                        result.addAll(page1);
                        return result.stream();
                    } else {
                        return repo.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(startPage, pageSize, sortDirection, sortProperty)).stream();
                    }
                },
                
                // count fetching strategy
                q -> (int) repo.countByNameLikeIgnoreCase(likeFilter)
        );
        adjustActionButtonState();

    }

    public void add() {
        edit(new Person());
    }

    public void edit() {
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
