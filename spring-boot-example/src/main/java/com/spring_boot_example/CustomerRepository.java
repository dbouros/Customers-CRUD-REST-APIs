package com.spring_boot_example;

// Importing the JpaRepository class
import org.springframework.data.jpa.repository.JpaRepository;

/*
 syntax: JpaRepository<Entity, datatype(Entity_id_type)>
 With our interface extending the JpaRepository class we have all the methods that the JpaRepository class
 provides available to us, meaning all the CRUD operations, since the JpaRepository class also extends the
 CrudRepository class.
 SOS! -> This is where the "jdbc" mostly happens, where the "Customer" class gets connected with the
 "JpaRepository". -> ("JpaRepository<Customer, Integer>")
*/

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
