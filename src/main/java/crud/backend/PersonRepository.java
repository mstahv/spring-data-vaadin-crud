
package crud.backend;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    /* A version to fetch List instead of Page to avoid extra count query. */
    List<Person> findAllBy(Pageable pageable);
    
    List<Person> findByNameLikeIgnoreCase(String nameFilter);
    
    // For lazy loading and filtering
    List<Person> findByNameLikeIgnoreCase(String nameFilter, Pageable pageable);
    
    long countByNameLikeIgnoreCase(String nameFilter);

}
