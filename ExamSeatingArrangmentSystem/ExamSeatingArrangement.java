import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;


public class ExamSeatingArrangement extends JFrame {
    private JComboBox<String> roomDropdown;
    private JTextField roomNameField, roomCapacityField, studentNameField, studentRollNoField;
    private JTextArea outputArea;
    private JTable roomDetailsTable;
    private DefaultTableModel tableModel;
    private java.util.List<ExamRoom> rooms;

    public ExamSeatingArrangement() {
        rooms = new ArrayList<>();
        setupWelcomeScreen();
    }

    private void setupWelcomeScreen() {
        JFrame welcomeFrame = new JFrame("Welcome");
        welcomeFrame.setSize(500, 300);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        welcomeFrame.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Exam Seating Arrangement System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 18));
        welcomeFrame.add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Serif", Font.BOLD, 16));
        startButton.addActionListener((ActionEvent e) -> {
            welcomeFrame.dispose();
            setupMainGUI();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        welcomeFrame.add(buttonPanel, BorderLayout.SOUTH);

        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setVisible(true);
    }

    private void setupMainGUI() {
        setTitle("Exam Seating Arrangement System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel for Room and Student Details
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        // Row 1: Room Details
        JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roomNameField = new JTextField(10);
        roomCapacityField = new JTextField(10);
        JButton addRoomButton = new JButton("Add Room");
        roomPanel.add(new JLabel("Room Name:"));
        roomPanel.add(roomNameField);
        roomPanel.add(new JLabel("Capacity:"));
        roomPanel.add(roomCapacityField);
        roomPanel.add(addRoomButton);

        // Row 2: Student Details
        JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentNameField = new JTextField(10);
        studentRollNoField = new JTextField(10);
        JButton addStudentButton = new JButton("Add Student");
        studentPanel.add(new JLabel("Student Name:"));
        studentPanel.add(studentNameField);
        studentPanel.add(new JLabel("Roll No:"));
        studentPanel.add(studentRollNoField);
        studentPanel.add(addStudentButton);

        topPanel.add(roomPanel);
        topPanel.add(studentPanel);

        // Middle Panel: Output Area and Table
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Output Area
        outputArea = new JTextArea(20, 20);
        outputArea.setFont(new Font("Serif", Font.PLAIN, 14));
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        middlePanel.add(outputScrollPane);

        // Table for Room Details
        tableModel = new DefaultTableModel(new String[]{"Room Name", "Capacity", " "}, 0);
        roomDetailsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(roomDetailsTable);
        middlePanel.add(tableScrollPane);

        // Bottom Panel: Room Dropdown and Show Details Button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        roomDropdown = new JComboBox<>();
        roomDropdown.addItem("-select-");
        JButton showRoomDetailsButton = new JButton("Show Room Details");
        JButton removeStudentButton = new JButton("Remove Student");
        bottomPanel.add(new JLabel("Select Room:"));
        bottomPanel.add(roomDropdown);
        bottomPanel.add(showRoomDetailsButton);
        bottomPanel.add(removeStudentButton);

        // Add Components to Main Frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button Listeners
        addRoomButton.addActionListener(e -> addRoom());
        addStudentButton.addActionListener(e -> addStudentToRoom());
        showRoomDetailsButton.addActionListener(e -> showRoomDetails());
        removeStudentButton.addActionListener(e -> removeStudentFromRoom());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addRoom() {
        String roomName = roomNameField.getText().trim();
        String capacityText = roomCapacityField.getText().trim();

        // Check if any field is empty
        if (roomName.isEmpty() || capacityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both Room Name and Capacity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if the room already exists
        for (ExamRoom room : rooms) {
            if (room.getRoomName().equalsIgnoreCase(roomName)) {
                JOptionPane.showMessageDialog(this, "Room name already exists! Please choose a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid capacity! Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ExamRoom newRoom = new ExamRoom(roomName, capacity);
        rooms.add(newRoom);
        roomDropdown.addItem(roomName);
        roomNameField.setText(""); // Clear field
        roomCapacityField.setText(""); // Clear field
        outputArea.append("Room " + roomName + " added with capacity " + capacity + ".\n");
    }

    private void addStudentToRoom() {
        String studentName = capitalizeName(studentNameField.getText().trim());
        String studentRollNo = studentRollNoField.getText().trim();

        // Check if any field is empty
        if (studentName.isEmpty() || studentRollNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both Student Name and Roll No!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (roomDropdown.getSelectedItem() == null || roomDropdown.getSelectedItem().equals("-select-")) {
            JOptionPane.showMessageDialog(this, "Please select a room first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedRoomName = roomDropdown.getSelectedItem().toString();
        ExamRoom selectedRoom = rooms.stream()
                .filter(room -> room.getRoomName().equals(selectedRoomName))
                .findFirst()
                .orElse(null);

        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Selected room not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedRoom.getStudents().size() >= selectedRoom.getCapacity()) {
            JOptionPane.showMessageDialog(this, "Room is full! Cannot add more students.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedRoom.getStudents().stream().anyMatch(student -> student.getRollNumber().equals(studentRollNo))) {
            JOptionPane.showMessageDialog(this, "Student with this roll number already exists in the room!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidRollNumber(studentRollNo)) {
            JOptionPane.showMessageDialog(this, "Invalid roll number! Roll number must have last three digits as numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = new Student(studentName, studentRollNo);
        if (selectedRoom.addStudent(student)) {
            studentNameField.setText(""); // Clear field
            studentRollNoField.setText(""); // Clear field
            outputArea.append("Student " + studentName + " (Roll No: " + studentRollNo + ") added to room " + selectedRoomName + ".\n");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Room is full or roll number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRoomDetails() {
        if (rooms.size() == 1) {
            showRoomDetailsInTable(rooms.get(0));
        } else {
            // Show room selection dialog
            String[] roomNames = rooms.stream().map(ExamRoom::getRoomName).toArray(String[]::new);
            String selectedRoomName = (String) JOptionPane.showInputDialog(this, "Select Room", "Room Selection", JOptionPane.QUESTION_MESSAGE, null, roomNames, roomNames[0]);
            if (selectedRoomName != null) {
                ExamRoom selectedRoom = rooms.stream()
                        .filter(room -> room.getRoomName().equals(selectedRoomName))
                        .findFirst()
                        .orElse(null);
                if (selectedRoom != null) {
                    showRoomDetailsInTable(selectedRoom);
                }
            }
        }
    }

    private void showRoomDetailsInTable(ExamRoom room) {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Sort students by roll number
        List<Student> sortedStudents = room.getStudents().stream()
                .sorted(Comparator.comparingInt(student -> getRollNumberLastDigits(student.getRollNumber())))
                .collect(Collectors.toList());

        // Add room details
        tableModel.addRow(new Object[]{room.getRoomName(), room.getCapacity(), ""});
        tableModel.addRow(new Object[]{"S.No", "Student Name", "Roll No"});
        for (int i = 0; i < sortedStudents.size(); i++) {
            Student student = sortedStudents.get(i);
            tableModel.addRow(new Object[]{i + 1, student.getName(), student.getRollNumber()});
        }
    }
    
    private void removeStudentFromRoom() {
        // Create a dialog box for room selection
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JComboBox<String> roomSelectionDropdown = new JComboBox<>();
        roomSelectionDropdown.addItem("-select-");

        // Populate dropdown with room names
        for (ExamRoom room : rooms) {
            roomSelectionDropdown.addItem(room.getRoomName());
        }

        panel.add(new JLabel("Select Room:"));
        panel.add(roomSelectionDropdown);
        panel.add(new JLabel("Enter Roll No:"));
        JTextField rollNoField = new JTextField();
        panel.add(rollNoField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Remove Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String selectedRoomName = (String) roomSelectionDropdown.getSelectedItem();
            String rollNo = rollNoField.getText().trim();

            if (selectedRoomName == null || selectedRoomName.equals("-select-")) {
                JOptionPane.showMessageDialog(this, "Please select a room.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (rollNo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a roll number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate roll number
            if (!isValidRollNumber(rollNo)) {
                JOptionPane.showMessageDialog(this, "Invalid roll number! Please enter a valid roll number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ExamRoom selectedRoom = rooms.stream()
                    .filter(room -> room.getRoomName().equals(selectedRoomName))
                    .findFirst()
                    .orElse(null);

            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(this, "Selected room not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the student exists in the selected room
            Student studentToRemove = selectedRoom.getStudents().stream()
                    .filter(student -> student.getRollNumber().equals(rollNo))
                    .findFirst()
                    .orElse(null);

            if (studentToRemove != null) {
                selectedRoom.removeStudent(studentToRemove);
                outputArea.append("Student with Roll No " + rollNo + " removed from room " + selectedRoomName + ".\n");
                showRoomDetailsInTable(selectedRoom); // Update the table with room details
            } else {
                JOptionPane.showMessageDialog(this, "Student not found in the selected room.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private boolean isValidRollNumber(String rollNo) {
        String lastThreeDigits = rollNo.length() >= 3 ? rollNo.substring(rollNo.length() - 3) : "";
        try {
            Integer.parseInt(lastThreeDigits);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getRollNumberLastDigits(String rollNo) {
        String lastThreeDigits = rollNo.length() >= 3 ? rollNo.substring(rollNo.length() - 3) : "000";
        try {
            return Integer.parseInt(lastThreeDigits);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String capitalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        String[] words = name.trim().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        return capitalized.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamSeatingArrangement());
    }
}