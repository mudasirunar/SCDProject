import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @Test
    void testStudentInitialization() {
        Student student = new Student("Ali", "123");
        assertEquals("Ali", student.getName());
        assertEquals("123", student.getRollNumber());
    }
}