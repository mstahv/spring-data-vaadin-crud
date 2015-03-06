package crud.vaadin;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import crud.backend.Address;
import crud.backend.AddressType;
import crud.backend.AddressTypeRepository;
import crud.backend.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.VaadinComponent;
import org.vaadin.viritin.fields.AbstractElementCollection;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@VaadinComponent
public class PhoneBookEntryForm extends AbstractForm<Person> {

    @Autowired
    AddressTypeRepository addressTypeRepository;

    TextField name = new MTextField("Name");
    TextField email = new MTextField("Email");
    TextField phoneNumber = new MTextField("Phone");
    DateField birthDay = new DateField("Birthday");
    CheckBox colleague = new CheckBox("Colleague");

    public static class AddressRow {

        TypedSelect<AddressType> type = new TypedSelect().withWidth("100px");
        TextField street = new MTextField().withInputPrompt("street");
        TextField city = new MTextField().withInputPrompt("city").withWidth(
                "6em");
        TextField zip = new MTextField().withInputPrompt("zip").withWidth("4em");
    }

    ElementCollectionField<Address> addresses = new ElementCollectionField<>(
            Address.class, AddressRow.class).withCaption("Addressess")
            .withEditorInstantiator(() -> {
                AddressRow addressRow = new AddressRow();
                // The ManyToOne field needs its options to be populated
                // Note, if there is lots of rows, it would be better to share
                // the list of addresstypes
                addressRow.type.setOptions(addressTypeRepository.findAll());
                return addressRow;
            });

    PhoneBookEntryForm() {
        setSizeUndefined();
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        email,
                        phoneNumber,
                        birthDay,
                        colleague,
                        addresses
                ).withWidth(""),
                getToolbar()
        ).withWidth("");
    }

}
