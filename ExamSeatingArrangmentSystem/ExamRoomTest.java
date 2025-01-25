import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamRoomTest {
    private ExamRoom examRoom;

    @BeforeEach
    void setUp() {
        examRoom = new ExamRoom("CG1", 20); // Room A with a capacity of 20 students.
    }

    @Test
    void testAddStudent_Success() {
        Student student = new Student("Ali", "101");
        assertTrue(examRoom.addStudent(student), "Student should be added successfully.");
    }
}
