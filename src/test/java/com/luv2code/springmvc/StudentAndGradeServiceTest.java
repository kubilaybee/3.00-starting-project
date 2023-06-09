package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @BeforeEach
    public void setupDatabase(){
        jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
                "values (1, 'name', 'surname', 'mail@test.com')");
    }

    /**
     * TODO -> FIX the findByEmailAddress
     */
    @Test
    void createStudentService(){
        studentService.createStudent("name","surname","mail@test.com");
        studentDao = Mockito.mock(StudentDao.class);

        when(studentDao.findByEmailAddress(anyString())).thenReturn(new CollegeStudent("name",
                "surname","mail@test.com"));
        CollegeStudent student = studentDao.findByEmailAddress("mail@test.com");

        assertEquals("mail@test.com",student.getEmailAddress(),"find by email");
    }
    @Test
    public void isStudentNullCheck(){
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    void deleteStudentService(){
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);

        assertTrue(deletedCollegeStudent.isPresent(),"Return True");

        studentService.deleteStudent(1);
        deletedCollegeStudent = studentDao.findById(1);
        assertFalse(deletedCollegeStudent.isPresent(),"Return False");
    }

    @Test
    public void getGradebookService(){
        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for (CollegeStudent collegeStudent:iterableCollegeStudents){
            collegeStudents.add(collegeStudent);
        }
        assertEquals(1,collegeStudents.size());
    }

    @AfterEach
    void setupAfterTransaction(){
        jdbc.execute("DELETE FROM student");
    }
}
