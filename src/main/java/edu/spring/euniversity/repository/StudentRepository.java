package edu.spring.euniversity.repository;

import edu.spring.euniversity.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
}
