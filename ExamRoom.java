import java.util.List;
import java.util.ArrayList;

class ExamRoom {
    private String roomName;
    private int capacity;    
    private List<Student> students;

    // Constructor
    public ExamRoom(String roomName, int capacity) {
        this.roomName = roomName;
        this.capacity = capacity;
        this.students = new ArrayList<>();
    }

    // Synchronized getter for roomName
    public synchronized String getRoomName() {
        return roomName;
    }

    // Synchronized getter for capacity
    public synchronized int getCapacity() {
        return capacity;
    }

    // Synchronized method to get students
    public synchronized List<Student> getStudents() {
        return new ArrayList<>(students); // Return a copy for thread safety
    }

    // Synchronized method to add a student
    public synchronized boolean addStudent(Student student) {
        while (students.size() >= capacity) {
            try {
                wait(); // Wait if the room is at full capacity
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                return false;
            }
        }
        students.add(student);
        notifyAll(); // Notify other threads that a student was added
        return true;
    }

    // Synchronized method to remove a student
    public synchronized void removeStudent(Student student) {
        if (students.remove(student)) {
            notifyAll(); // Notify threads when a student is removed
        }
    }

    // Synchronized method to check if a student is in the room
    public synchronized boolean hasStudent(Student student) {
        return students.contains(student);
    }

    // Synchronized method to clear all students
    public synchronized void clearRoom() {
        students.clear();
        notifyAll(); // Notify threads after clearing the room
    }
}
