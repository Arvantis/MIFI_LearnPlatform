package fun.justdevelops.learnplatform.repository;

import fun.justdevelops.learnplatform.entity.person.Person;
import fun.justdevelops.learnplatform.entity.person.Student;
import fun.justdevelops.learnplatform.entity.person.Teacher;
import fun.justdevelops.learnplatform.enums.StudentStatus;
import fun.justdevelops.learnplatform.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    @Query("SELECT s FROM Student s")
    List<Student> findAllStudents();

    @Query("SELECT t FROM Teacher t")
    List<Teacher> findAllTeachers();

    @Query("SELECT s FROM Student s WHERE s.id=?1")
    Optional<Student> findStudentEntityById(UUID id);

    @Query("SELECT t FROM Teacher t WHERE t.id=?1")
    Teacher findTeacherEntityById(UUID id);

    @Query("SELECT s FROM Student s WHERE s.studentNo = :studentNo")
    Optional<Student> findByStudentNo(String studentNo);

    @Query("SELECT s FROM Student s WHERE s.status = :status")
    List<Student> findByStatus(StudentStatus status);

    @Transactional
    @Query("DELETE FROM Student s WHERE s.id = :id")
    void deleteStudent(UUID id);

    @Transactional
    @Query("DELETE FROM Teacher t WHERE t.id = :id")
    void deleteTeacher(UUID id);
}
