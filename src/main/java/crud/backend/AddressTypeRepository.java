
package crud.backend;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Empty JpaRepository is enough for a simple crud.
 */
public interface AddressTypeRepository extends JpaRepository<AddressType, Long> {
    
}
