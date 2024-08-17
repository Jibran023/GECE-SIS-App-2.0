<?php
$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner';       // MySQL password
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Retrieve data from URL parameters
$courseName = trim($_GET['CourseName']);
$sessionDescription = trim($_GET['SessionDescription']);

// Get RollNumbers for students in the specified course and session
$sqlStudents = "SELECT DISTINCT sm.RollNumber 
                FROM studentmap sm 
                INNER JOIN offeredcourses oc ON sm.CourseID = oc.CourseID 
                INNER JOIN academicsession a ON sm.SessionID = a.SessionID 
                INNER JOIN courses c ON sm.CourseID = c.CourseID
                WHERE c.Name = ? AND a.Description = ?";
$stmtStudents = $conn->prepare($sqlStudents);
$stmtStudents->bind_param("ss", $courseName, $sessionDescription);
$stmtStudents->execute();
$stmtStudents->bind_result($rollNumber);

// Collect roll numbers
$rollNumbers = [];
while ($stmtStudents->fetch()) {
    $rollNumbers[] = $rollNumber;
}
$stmtStudents->close();

if (count($rollNumbers) > 0) {
    echo json_encode(["success" => true, "rollNumbers" => $rollNumbers]);
} else {
    echo json_encode(["success" => false, "message" => "No students found for the given course and session"]);
}

// Close connection
$conn->close();
?>
