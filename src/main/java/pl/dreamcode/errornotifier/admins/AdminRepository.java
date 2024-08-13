package pl.dreamcode.errornotifier.admins;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {
    
    public Admin findAdminByEmail(String email);

}
