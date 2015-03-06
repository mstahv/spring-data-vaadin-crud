
package crud.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Matti Tahvonen
 */
@Service
@Transactional
public class PersonService {

    @Autowired
    PersonRepository repo;
    
    /**
     * Returns a Person instance with all relations attached.
     * 
     * @param person
     * @return person instance with all relations attached
     */
    public Person fetchForEditing(Person person) {
        Person one = repo.getOne(person.getId());
        one.getAddresses().size(); // join addresses
        return one;
    }

    public Page<Person> findAll(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }

    public long count() {
        return repo.count();
    }

    public void delete(Person value) {
        repo.delete(value);
    }

    public void save(Person entry) {
        repo.save(entry);
    }
}
