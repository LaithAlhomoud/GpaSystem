package gpaSystem;

import javax.swing.*; // Importing Swing components for GUI
import java.awt.*; // Importing AWT for layouts and colors
import java.io.File; // Importing File class for file operations
import java.io.FileWriter; // Importing FileWriter for writing to files
import java.io.IOException; // Importing IOException for handling IO exceptions
import java.io.PrintWriter; // Importing PrintWriter for writing text to files
import java.util.*; // Importing Java utility classes
import java.util.List; // Importing List interface
import java.util.stream.Collectors; // Importing Collectors for stream operations

public class UniversityGPASystemGUI {

    private JFrame frame; // Main window of the application
    private JTextField studentIdField, scoreField; // Text fields for student ID and score
    private JTextArea displayArea; // Text area for displaying information
    private UniversityGPASystem universityGPASystem; // Backend system for managing data
    private JComboBox<String> courseCodeComboBox; // ComboBox for selecting course codes

    public static void main(String[] args) {
        try {
            // Set the look and feel of the GUI to match the system's theme
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions encountered
        }

        // Ensure the GUI is created and shown in the Event Dispatch Thread
        EventQueue.invokeLater(() -> {
            try {
                UniversityGPASystemGUI gui = new UniversityGPASystemGUI();
                gui.frame.setVisible(true); // Make the frame visible
                
            } catch (Exception e) {
                e.printStackTrace(); // Print any exceptions encountered
            }
        });
    }

    public UniversityGPASystemGUI() {
        initializeGUI(); // Initialize the graphical user interface
        universityGPASystem = new UniversityGPASystem(); // Initialize the backend system
    }

    private void initializeGUI() {
        frame = new JFrame("University GPA System"); // Create the main window with a title
        frame.setBounds(100, 100, 800, 600); // Set the size and position of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation
        frame.setLayout(new BorderLayout()); // Set the layout manager for the frame
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Set the background color of the content pane
        // Creating a custom font for titles and labels
        Font titleFont = new Font("Arial", Font.BOLD, 16);

        // Customizing the input panel
        createInputPanel(titleFont);
        createDisplayArea();
        createButtonsPanel(titleFont);

        // Setting a custom icon for the frame (replace with icon path)
        ImageIcon frameIcon = new ImageIcon("/Users/laithalhomoud/Downloads/auraklogo1.png");
        frame.setIconImage(frameIcon.getImage());
    }

    private void createInputPanel(Font titleFont) {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Create a panel with grid layout
        inputPanel.setBackground(new Color(220, 220, 220)); // Set the background color of the input panel
        // Student ID Label
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(titleFont); // Set the font for the label
        studentIdLabel.setHorizontalAlignment(SwingConstants.LEFT); // Align the label text to the left
        studentIdField = new JTextField(10); // Create a text field for student ID input
        inputPanel.add(studentIdLabel); // Add the label to the input panel
        inputPanel.add(studentIdField); // Add the text field to the input panel

        // Score Label
        JLabel scoreLabel = new JLabel("Score:");
        scoreLabel.setFont(titleFont); // Set the font for the score label
        scoreField = new JTextField(10); // Create a text field for score input
        inputPanel.add(scoreLabel); // Add the score label to the input panel
        inputPanel.add(scoreField); // Add the score text field to the input panel

        // Course Code Label
        JLabel courseCodeLabel = new JLabel("Course Code:");
        courseCodeLabel.setFont(titleFont); // Set the font for the course code label
        courseCodeComboBox = new JComboBox<>(); // Create a combo box for selecting course codes
        inputPanel.add(courseCodeLabel); // Add the course code label to the input panel
        inputPanel.add(courseCodeComboBox); // Add the course code combo box to the input panel

        studentIdField.addActionListener(e -> updateCourseListForStudent()); // Add an action listener to the student ID field
        frame.add(inputPanel, BorderLayout.NORTH); // Add the input panel to the top (north) of the frame
    }

    // Method to update the course list based on the selected student
    private void updateCourseListForStudent() {
        String studentId = studentIdField.getText().trim(); // Get the text from the student ID field and trim it
        if (DataValidator.isValidStudentId(studentId)) { // Check if the student ID is valid
            List<String> courses = universityGPASystem.getRegisteredCoursesForStudent(studentId); // Get the courses registered for the student
            courseCodeComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0]))); // Update the combo box model
        }
    }

    // Method to create the display area
    private void createDisplayArea() {
        displayArea = new JTextArea(); // Create a new text area for displaying information
        displayArea.setEditable(false); // Make the text area non-editable
        displayArea.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set the font for the text area
        displayArea.setBackground(new Color(255, 255, 255)); // Set the background color of the text area to white
        JScrollPane scrollPane = new JScrollPane(displayArea); // Create a scroll pane for the text area
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200))); // Set a border for the scroll pane
        frame.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the frame
    }
    // Method to create the buttons panel with custom styling
    private void createButtonsPanel(Font titleFont) {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Create a panel with flow layout for buttons
        buttonsPanel.setBackground(new Color(245, 245, 245)); // Set the background color of the buttons panel

        // Add/Edit Grade Button
        JButton addGradeButton = new JButton("Add/Edit Grade");
        customizeButton(addGradeButton, titleFont, new Color(30, 150, 30), Color.BLACK); // Customize button appearance
        addGradeButton.addActionListener(e -> addOrEditGrade()); // Add action listener to handle button click
        buttonsPanel.add(addGradeButton); // Add the button to the panel

        // Calculate GPA Button
        JButton calculateGPAButton = new JButton("Calculate GPA");
        customizeButton(calculateGPAButton, titleFont, new Color(70, 130, 180), Color.BLACK); // Customize button appearance
        calculateGPAButton.addActionListener(e -> calculateGPA()); // Add action listener to handle button click
        buttonsPanel.add(calculateGPAButton); // Add the button to the panel

        // Import Data Button
        JButton importDataButton = new JButton("Import Data");
        customizeButton(importDataButton, titleFont, new Color(128, 128, 0), Color.BLACK); // Customize button appearance
        importDataButton.addActionListener(e -> importData()); // Add action listener to handle button click
        buttonsPanel.add(importDataButton); // Add the button to the panel

        // Export Data Button
        JButton exportDataButton = new JButton("Export Data");
        customizeButton(exportDataButton, titleFont, new Color(255, 215, 0), Color.BLACK); // Customize button appearance
        exportDataButton.addActionListener(e -> exportData()); // Add action listener to handle button click
        buttonsPanel.add(exportDataButton); // Add the button to the panel

        frame.add(buttonsPanel, BorderLayout.SOUTH); // Add the buttons panel to the bottom (south) of the frame
    }

    // Helper method to customize the appearance of buttons
    private void customizeButton(JButton button, Font font, Color bgColor, Color fgColor) {
        button.setFont(font); // Set the font of the button
        button.setBackground(bgColor); // Set the background color of the button
        button.setForeground(fgColor); // Set the foreground (text) color of the button
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Set the border of the button
        button.setToolTipText(button.getText()); // Set the tooltip text to the button text
    }

    // Method to process the imported file
    private void processImportFile(File file) throws IOException {
        try (Scanner scanner = new Scanner(file)) { // Create a scanner to read the file
            while (scanner.hasNextLine()) { // Loop through all lines in the file
                String line = scanner.nextLine(); // Read the next line
                String[] parts = line.split(", "); // Split the line into parts
                switch (parts[0]) { // Check the first part to determine the type of data
                    case "Student":
                        processStudentLine(parts); // Process a student line
                        break;
                    case "Course":
                        processCourseLine(parts); // Process a course line
                        break;
                    case "Enrollment":
                        processEnrollmentLine(parts); // Process an enrollment line
                        break;
                    case "Score":
                        processScoreLine(parts); // Process a score line
                        break;
                    
                }
            }
        }
        
        displayStudents(); // Display the students after processing the file
        
    }
    // Method to process a line representing a student from the imported file
    private void processStudentLine(String[] parts) {
        if (parts.length == 3 && "Student".equals(parts[0])) { // Ensure correct format and data type
            String studentId = parts[1]; // Extract student ID
            String studentName = parts[2]; // Extract student name
            Student student = new GeneralStudent(studentId, studentName); // Create a new student object
            universityGPASystem.addStudent(student); // Add the student to the system
        }
    }

    // Method to process a line representing a course from the imported file
    private void processCourseLine(String[] parts) {
        if (parts.length == 3 && "Course".equals(parts[0])) { // Ensure correct format and data type
            String courseCode = parts[1]; // Extract course code
            String courseName = parts[2]; // Extract course name
            Course course = new Course(courseCode, courseName); // Create a new course object
            universityGPASystem.addCourse(course); // Add the course to the system
        }
    }

    // Method to process a line representing a student enrollment from the imported file
    private void processEnrollmentLine(String[] parts) {
        if (parts.length == 3 && "Enrollment".equals(parts[0])) { 
            String studentId = parts[1]; // Extract student ID
            String courseCode = parts[2]; // Extract course code
            universityGPASystem.registerStudentToCourse(studentId, courseCode); // Register the student to the course
        }
    }

    // Method to process a line representing a score from the imported file
    private void processScoreLine(String[] parts) {
        if (parts.length == 4) { // 
            String studentId = parts[1]; // Extract student ID
            String courseCode = parts[2]; // Extract course code
            try {
                double score = Double.parseDouble(parts[3]); // Parse the score value
                universityGPASystem.addOrEditGrade(studentId, courseCode, score); // Add or edit the grade in the system
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid score format in file for Student ID " + studentId + ", Course Code " + courseCode, "Format Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid score line format in file.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to initiate the data import process
    private void importData() {
        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser dialog
        int result = fileChooser.showOpenDialog(frame); // Show the dialog and get the result
        if (result == JFileChooser.APPROVE_OPTION) { // If the user selects a file
            File selectedFile = fileChooser.getSelectedFile(); // Get the selected file
            try {
                processImportFile(selectedFile); // Process the selected file
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to initiate the data export process
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser(); // Create a file chooser for saving a file
        fileChooser.setDialogTitle("Specify a file to save"); // Set the title of the dialog
        int userSelection = fileChooser.showSaveDialog(frame); // Show the save dialog and get the result
        if (userSelection == JFileChooser.APPROVE_OPTION) { // If the user selects a file
            File fileToSave = fileChooser.getSelectedFile(); // Get the file to save
            try (PrintWriter out = new PrintWriter(new FileWriter(fileToSave))) { // Create a PrintWriter for the file
                // Iterate through all students and their grades
                for (Map.Entry<String, Student> studentEntry : universityGPASystem.getStudents().entrySet()) {
                    String studentId = studentEntry.getKey(); // Get the student ID
                    Student student = studentEntry.getValue(); // Get the student object
                    // Iterate through all grades for the student
                    for (Map.Entry<Course, Double> gradeEntry : student.getGrades().entrySet()) {
                        Course course = gradeEntry.getKey(); // Get the course
                        Double score = gradeEntry.getValue(); // Get the score
                        // Write the score information to the file
                        out.println("Score, " + studentId + ", " + course.getCourseCode() + ", " + score);
                    }
                }
            } catch (IOException ex) {
                // Display an error message if there is an issue writing to the file
                JOptionPane.showMessageDialog(frame, "Error writing to file: " + ex.getMessage(), "File Write Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Method to display only the students and their IDs
    private void displayStudents() {
        StringBuilder sb = new StringBuilder("Registered Students:\n"); // Create a StringBuilder for appending student information
        for (Map.Entry<String, Student> entry : universityGPASystem.getStudents().entrySet()) { // Iterate over each student in the system
            Student student = entry.getValue(); // Get the student object
            sb.append("Student ID: ").append(student.getStudentID()) // Append student ID to the string builder
              .append(", Name: ").append(student.getStudentName()).append("\n"); // Append student name to the string builder
        }
        displayArea.setText(sb.toString()); // Set the text of the display area to the string builder's content
    }

    // Method to handle adding or editing a grade
    private void addOrEditGrade() {
        String studentId = studentIdField.getText().trim(); // Get and trim the text from the student ID field
        String courseCode = (String) courseCodeComboBox.getSelectedItem(); // Get the selected course code from the combo box
        String scoreText = scoreField.getText().trim(); // Get and trim the text from the score field
        try {
            double score = Double.parseDouble(scoreText); // Parse the score text to a double
            if (DataValidator.isValidStudentId(studentId) && DataValidator.isValidCourseCode(courseCode) && DataValidator.isValidScore(score)) { // Validate the input data
                boolean success = universityGPASystem.addOrEditGrade(studentId, courseCode, score); // Add or edit the grade in the system
                if (success) { // If the operation is successful
                    displayArea.append("Grade updated for Student ID " + studentId + ", Course Code " + courseCode + ": " + score + "\n"); // Display success message
                } else { // If the operation is unsuccessful
                    JOptionPane.showMessageDialog(frame, "Failed to add/edit grade. The student might not be registered in the specified course.", "Grade Update Error", JOptionPane.ERROR_MESSAGE); // Display error message
                }
            } else { // If input data is invalid
                JOptionPane.showMessageDialog(frame, "Invalid input.", "Input Error", JOptionPane.ERROR_MESSAGE); // Display error message
            }
        } catch (NumberFormatException ex) { // Catch number format exception for score parsing
            JOptionPane.showMessageDialog(frame, "Invalid score format. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE); // Display error message for invalid score format
        }
    }

    // Method to calculate and display the GPA of a student
    private void calculateGPA() {
        String studentId = studentIdField.getText().trim(); // Get and trim the text from the student ID field
        if (DataValidator.isValidStudentId(studentId)) { // Validate the student ID
            try {
                double gpa = universityGPASystem.calculateGPA(studentId); // Calculate the GPA for the given student ID
                displayArea.append("GPA for Student ID " + studentId + " is: " + gpa + "\n"); // Display the calculated GPA
            } catch (IllegalArgumentException ex) { // Catch illegal argument exception for student not found
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE); // Display error message for calculation error
            }
        } else { // If student ID is invalid
            JOptionPane.showMessageDialog(frame, "Invalid student ID.", "Input Error", JOptionPane.ERROR_MESSAGE); // Display error message for invalid student ID
        }
    }
    class UniversityGPASystem {
        private Map<String, Student> students; // Map to store students with their IDs as keys
        private Map<String, Course> courses; // Map to store courses with their codes as keys

        public UniversityGPASystem() {
            students = new HashMap<>(); // Initialize the map for students
            courses = new HashMap<>(); // Initialize the map for courses
            initializeCourses(); // Call method to initialize predefined courses
            initializeStudents(); // Call method to initialize predefined students
        }

        // Method to get the map of all students
        public Map<String, Student> getStudents() {
            return students; // Return the map containing students
        }

        // Method to initialize predefined courses (for demonstration purposes)
        private void initializeCourses() {
            courses.put("MATH101", new MathCourse("MATH101", "Mathematics")); // Add a Math course
            courses.put("PROG101", new ProgrammingCourse("PROG101", "Programming")); // Add a Programming course
            
        }

        // Method to initialize predefined students (for demonstration purposes)
        private void initializeStudents() {
            students.put("S001", new GeneralStudent("S001", "Noor")); // Add a student with ID S001
            students.put("S002", new GeneralStudent("S002", "Laith")); // Add a student with ID S002
            
        }

        // Method to add a student to the system
        public void addStudent(Student student) {
            if (student != null && !students.containsKey(student.getStudentID())) { // Check if student is not already in the system
                students.put(student.getStudentID(), student); // Add the student to the map
            }
        }

        // Method to add a course to the system
        public void addCourse(Course course) {
            if (course != null && !courses.containsKey(course.getCourseCode())) { // Check if course is not already in the system
                courses.put(course.getCourseCode(), course); // Add the course to the map
            }
        }

        // Method to register a student to a course
        public void registerStudentToCourse(String studentId, String courseCode) {
            if (students.containsKey(studentId) && courses.containsKey(courseCode)) { // Check if both student and course exist
                Student student = students.get(studentId); // Get the student object
                Course course = courses.get(courseCode); // Get the course object
                student.addCourse(course); // Register the student to the course
            }
        }

        // Method to get all courses in the system
        public Map<String, Course> getCourses() {
            return courses; // Return the map containing courses
        }

        // Method to get a list of students registered for a specific course
        public List<Student> getStudentsForCourse(String courseCode) {
            List<Student> registeredStudents = new ArrayList<>(); // Create a list to store registered students
            for (Student student : students.values()) { // Iterate over all students
                if (student.isRegisteredInCourse(courseCode)) { // Check if the student is registered in the course
                    registeredStudents.add(student); // Add the student to the list
                }
            }
            return registeredStudents; // Return the list of registered students
        }
        // Method to add or edit a grade for a student in a course
        public boolean addOrEditGrade(String studentId, String courseCode, double score) {
            if (students.containsKey(studentId) && courses.containsKey(courseCode)) { // Check if student and course exist
                Student student = students.get(studentId); // Retrieve the student object
                Course course = courses.get(courseCode); // Retrieve the course object
                if (student.isRegisteredInCourse(courseCode)) { // Check if student is registered in the course
                    student.addGrade(course, score); // Add or update the grade for the course
                    return true; // Indicate successful addition/editing of grade
                }
            }
            return false; // Return false if grade could not be added or edited
        }

        // Method to calculate the GPA of a student
        public double calculateGPA(String studentId) {
            if (students.containsKey(studentId)) { // Check if the student exists
                return students.get(studentId).calculateGPA(); // Calculate and return the GPA of the student
            } else {
                throw new IllegalArgumentException("Student not found."); // Throw an exception if student does not exist
            }
        }

        // Method to get the registered courses for a specific student
        public List<String> getRegisteredCoursesForStudent(String studentId) {
            if (students.containsKey(studentId)) { // Check if the student exists
                Student student = students.get(studentId); // Get the student object
                // Return a list of course codes that the student is registered in
                return student.getRegisteredCourses().stream()
                              .map(Course::getCourseCode)
                              .collect(Collectors.toList());
            }
            return new ArrayList<>(); // Return an empty list if student does not exist
        }
    }

    // class representing a student
     class Student {
        private String studentID; // Unique identifier for the student
        private String studentName; // Name of the student
        private Map<Course, Double> grades; // Map to store grades for courses
        private List<Course> registeredCourses; // List of courses the student is registered in

        // Constructor for the Student class
        public Student(String studentID, String studentName) {
            this.studentID = studentID; // Set the student ID
            this.studentName = studentName; // Set the student name
            this.grades = new HashMap<>(); // Initialize the map for grades
            this.registeredCourses = new ArrayList<>(); // Initialize the list for registered courses
        }

        // Method to add a course to the student's list of registered courses
        public void addCourse(Course course) {
            if (course != null && !registeredCourses.contains(course)) { // Check if the course is not already added
                registeredCourses.add(course); // Add the course to the list
            }
        }

        // Method to check if the student is registered in a specific course
        public boolean isRegisteredInCourse(String courseCode) {
            return registeredCourses.stream() // Stream the list of registered courses
                                    .anyMatch(course -> course.getCourseCode().equals(courseCode)); // Check if any course matches the course code
        }

        // Method to add or update a grade for a course
        public void addGrade(Course course, double grade) {
            grades.put(course, grade); // Add or update the grade for the course in the map
        }

        // Method to calculate the student's GPA
        public double calculateGPA() {
            if (grades.isEmpty()) return 0.0; // Return 0.0 if there are no grades
            double totalPoints = 0.0; // Initialize total points
            int count = 0; // Initialize count of courses with grades
            for (Course course : registeredCourses) { // Iterate over registered courses
                if (grades.containsKey(course)) { // Check if a grade exists for the course
                    totalPoints += convertScoreToGradePoint(grades.get(course)); // Add the grade points to total
                    count++; // Increment the count
                }
            }
            return count > 0 ? totalPoints / count : 0.0; // Calculate and return average GPA, or 0.0 if no courses
        }
        // Method to convert a numerical score to a grade point (on a 4.0 scale)
        private double convertScoreToGradePoint(double score) {
            if (score >= 90) return 4.0; // A grade
            if (score >= 80) return 3.0; // B grade
            if (score >= 70) return 2.0; // C grade
            if (score >= 60) return 1.0; // D grade
            return 0.0; // F grade
        }

        // Getter for student ID
        public String getStudentID() { return studentID; }

        // Getter for student name
        public String getStudentName() { return studentName; }

        // Setter for student name
        public void setStudentName(String studentName) { this.studentName = studentName; }

        // Getter for the map of grades
        public Map<Course, Double> getGrades() { return grades; }

        // Getter for the list of registered courses
        public List<Course> getRegisteredCourses() { return registeredCourses; }
    }

    // Concrete class representing a general student
    class GeneralStudent extends Student {
        // Constructor for GeneralStudent
        public GeneralStudent(String studentID, String studentName) {
            super(studentID, studentName); // Call the superclass constructor
        }
    }
    
    class MathStudent extends Student {
        // Constructor for GeneralStudent
        public MathStudent(String studentID, String studentName) {
            super(studentID, studentName); // Call the superclass constructor
        }
    }
    
    class ScienceStudent extends Student {
        // Constructor for GeneralStudent
        public ScienceStudent(String studentID, String studentName) {
            super(studentID, studentName); // Call the superclass constructor
        }
    }

    

    // class representing a course
    class Course {
        private String courseCode; // Unique code for the course
        private String courseName; // Name of the course

        // Constructor for Course
        public Course(String courseCode, String courseName) {
            this.courseCode = courseCode; // Set the course code
            this.courseName = courseName; // Set the course name
        }

        // Getter for course code
        public String getCourseCode() { return courseCode; }

        // Setter for course code
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

        // Getter for course name
        public String getCourseName() { return courseName; }

        // Setter for course name
        public void setCourseName(String courseName) { this.courseName = courseName; }
    }

    // Concrete class representing a Math course
    class MathCourse extends Course {
        // Constructor for MathCourse
        public MathCourse(String courseCode, String courseName) {
            super(courseCode, courseName); // Call the superclass constructor
        }
    }

    // Concrete class representing a Programming course
    class ProgrammingCourse extends Course {
        // Constructor for ProgrammingCourse
        public ProgrammingCourse(String courseCode, String courseName) {
            super(courseCode, courseName); // Call the superclass constructor
        }
    }

    // Concrete class representing an Arabic course
    class ArabicCourse extends Course {
        // Constructor for ArabicCourse
        public ArabicCourse(String courseCode, String courseName) {
            super(courseCode, courseName); // Call the superclass constructor
        }
    }
    // Concrete class representing a Physics course
    class PhysicsCourse extends Course {
        // Constructor for PhysicsCourse
        public PhysicsCourse(String courseCode, String courseName) {
            super(courseCode, courseName); // Call the superclass constructor
        }
    }

    // Concrete class representing a History course
    class HistoryCourse extends Course {
        // Constructor for HistoryCourse
        public HistoryCourse(String courseCode, String courseName) {
            super(courseCode, courseName); // Call the superclass constructor
        }
    }

    
    
}

class DataValidator {
    // Method to validate student ID format
    public static boolean isValidStudentId(String studentId) {
        return studentId != null && studentId.matches("[A-Za-z0-9]+"); // Check if ID is alphanumeric
    }

    // Method to validate course code format
    public static boolean isValidCourseCode(String courseCode) {
        return courseCode != null && courseCode.matches("[A-Za-z0-9]+"); // Check if code is alphanumeric
    }

    // Method to validate name format (it used for both name and course)
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty(); // Check if name is not empty
    }

    // Method to validate score
    public static boolean isValidScore(double score) {
        return score >= 0.0 && score <= 100.0; // Check if score is within 0-100 range
    }

    
}
