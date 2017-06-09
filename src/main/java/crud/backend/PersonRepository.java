
package crud.backend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    Page<Person> findByNameLikeIgnoreCase(String nameFilter, Pageable pageable);

    long countByNameLikeIgnoreCase(String nameFilter);

}
