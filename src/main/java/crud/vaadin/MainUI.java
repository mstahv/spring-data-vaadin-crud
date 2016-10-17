package crud.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import crud.backend.Person;
import crud.backend.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@Title("PhoneBook CRUD example")
@Theme("valo")
@SpringUI
public class MainUI extends UI implements EventBusListener<PersonModifiedEvent> {
    
    PersonRepository repo;
    PersonForm personForm;
    EventBus.UIEventBus eventBus;
    
    private MTable<Person> list = new MTable<>(Person.class)
            .withProperties("id", "name", "email")
            .withColumnHeaders("id", "Name", "Email")
            .setSortableProperties("name", "email")
            .withFullWidth();
    
    private TextField filterByName = new MTextField()
            .withInputPrompt("Filter by name");
    private Button addNew = new MButton(FontAwesome.PLUS, this::add);
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, this::edit);
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete the entry?", this::remove);
    
    @Autowired
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
        
        list.addMValueChangeListener(e -> adjustActionButtonState());
        filterByName.addTextChangeListener(e -> {
            listEntities(e.getText());
        });

        // Listen to change events emitted by PersonForm see onEvent method
        eventBus.subscribe(this);
    }
    
    protected void adjustActionButtonState() {
        boolean hasSelection = list.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }
    
    static final int PAGESIZE = 45;
    
    private void listEntities() {
        listEntities(filterByName.getValue());
    }
    
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
        // list.lazyLoadFrom(
        //         // entity fetching strategy
        //         (firstRow, asc, sortProperty) -> repo.findByNameLikeIgnoreCase(
        //                 likeFilter,
        //                 new PageRequest(
        //                         firstRow / PAGESIZE,
        //                         PAGESIZE,
        //                         asc ? Sort.Direction.ASC : Sort.Direction.DESC,
        //                         // fall back to id as "natural order"
        //                         sortProperty == null ? "id" : sortProperty
        //                 )
        //         ),
        //         // count fetching strategy
        //         () -> (int) repo.countByNameLike(likeFilter),
        //         PAGESIZE
        // );
        adjustActionButtonState();
        
    }
    
    public void add(ClickEvent clickEvent) {
        edit(new Person());
    }
    
    public void edit(ClickEvent e) {
        edit(list.getValue());
    }
    
    public void remove(ClickEvent e) {
        repo.delete(list.getValue());
        list.setValue(null);
        listEntities();
    }
    
    protected void edit(final Person phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }
    
    @Override
    public void onEvent(org.vaadin.spring.events.Event<PersonModifiedEvent> event) {
        listEntities();
        personForm.closePopup();
    }
    
}
