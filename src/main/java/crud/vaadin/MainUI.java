package crud.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import crud.backend.Person;
import crud.backend.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.maddon.button.ConfirmButton;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.label.RichText;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;

/**
 *
 */
@Title("PhoneBook CRUD example")
@Theme("valo")
@VaadinUI
public class MainUI extends UI {

    @Autowired
    PersonRepository repo;

    private MTable<Person> list = new MTable(Person.class)
            .withProperties("name", "email")
            .withColumnHeaders("Name", "Email")
            .withFullWidth();

    private Button addNew = new MButton(FontAwesome.PLUS, new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            add(event);
        }
    });
    private Button edit = new MButton(FontAwesome.PENCIL_SQUARE_O, new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            edit(event);
        }
    });
    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete the entry?", new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            remove(event);
        }
    });

    @Override
    protected void init(VaadinRequest request) {
        setContent(
                new MVerticalLayout(
                        new RichText().withMarkDownResource("/welcome.md"),
                        new MHorizontalLayout(addNew, edit, delete),
                        list
                ).expand(list)
        );
        listEntities();
        list.addMValueChangeListener(new MValueChangeListener<Person>() {

            public void valueChange(MValueChangeEvent<Person> e) {
                adjustActionButtonState();
            }
        });
    }

    protected void adjustActionButtonState() {
        boolean hasSelection = list.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void listEntities() {
        list.setBeans(repo.findAll());
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
        PhoneBookEntryForm phoneBookEntryForm = new PhoneBookEntryForm(
                phoneBookEntry);
        phoneBookEntryForm.openInModalPopup();
        phoneBookEntryForm.setSavedHandler(new AbstractForm.SavedHandler<Person>() {

            public void onSave(Person entry) {
                MainUI.this.saveEntry(entry);
            }
        });
        phoneBookEntryForm.setResetHandler(new AbstractForm.ResetHandler<Person>() {

            public void onReset(Person entry) {
                MainUI.this.resetEntry(entry);
            }
        });
    }

    public void saveEntry(Person entry) {
        repo.save(entry);
        listEntities();
        closeWindow();
    }

    public void resetEntry(Person entry) {
        listEntities();
        closeWindow();
    }

    protected void closeWindow() {
        for (Window w : getWindows().toArray(new Window[0])) {
            removeWindow(w);
        }
    }

}
