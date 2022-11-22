package com.github.silviacristinaa.padroesprojetospring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.silviacristinaa.padroesprojetospring.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
