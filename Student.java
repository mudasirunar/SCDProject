public class Student {
    private String name;
    private String rollNumber;
    private boolean isProcessed = false;

    // Constructor
    public Student(String name, String rollNumber) {
        this.name = formatName(name);
        this.rollNumber = rollNumber;
    }

    // Synchronized getter for Name
    public synchronized String getName() {
        return name;
    }

    // Synchronized getter for Roll Number
    public synchronized String getRollNumber() {
        return rollNumber;
    }

    // Synchronized method to process student data
    public synchronized void processStudentData() {
        try {
            // Simulate processing time
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
        isProcessed = true;
        notifyAll(); // Notify all waiting threads after processing
    }

    // Synchronized method to wait for data to be processed
    public synchronized void waitForProcessing() {
        while (!isProcessed) {
            try {
                wait(); // Wait until notified
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
            }
        }
    }

    // Private method to format the name
    private String formatName(String name) {
        String[] parts = name.trim().split("\\s+");
        StringBuilder formattedName = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                formattedName.append(part.substring(0, 1).toUpperCase())
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return formattedName.toString().trim();
    }
}
