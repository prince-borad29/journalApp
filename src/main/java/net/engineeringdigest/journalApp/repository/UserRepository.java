package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<Users, ObjectId> {
    Users findByUsername(String username);

    void deleteByUsername(String username);
}