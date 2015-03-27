
package crud.backend;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    List<Person> findByIdGreaterThan(long dummyId, Pageable pageable);
    
}
