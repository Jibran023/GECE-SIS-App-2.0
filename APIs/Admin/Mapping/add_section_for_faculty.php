<?php

header('Content-Type: application/json');

// Database connection parameters
$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner'; // MySQL password
$database = 'gecesisapp';  // Your database name

// Retrieve parameters from the GET request
$facultyID = $_GET['FacultyID'] ?? '';
$courseID = $_GET['CourseID'] ?? '';
$sessionID = $_GET['SessionID'] ?? '';
$sectionID = $_GET['SectionID'] ?? '';

// Debug: Log received parameters
error_log("Received parameters - FacultyID: $facultyID, CourseID: $courseID, SessionID: $sessionID, SectionID: $sectionID");

// Check if required parameters are provided
if (empty($facultyID) || empty($courseID) || empty($sessionID) || empty($sectionID)) {
    $errorMsg = "Missing FacultyID, CourseID, SessionID, or SectionID";
    error_log("Error: $errorMsg");
    echo json_encode(["status" => "error", "message" => $errorMsg]);
    exit();
}

// Create a connection to the database
$conn = new mysqli($host, $username, $password, $database);

// Check for connection errors
if ($conn->connect_error) {
    $errorMsg = "Connection failed: " . $conn->connect_error;
    error_log("Error: $errorMsg");
    echo json_encode(["status" => "error", "message" => $errorMsg]);
    exit();
}

// Debug: Log successful database connection
error_log("Database connection successful.");

// Prepare the SQL statement to insert the new section into faculty courses
$query = "INSERT INTO facultycourses (FacultyID, CourseID, SessionID, SectionID) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($query);

// Check if statement preparation was successful
if (!$stmt) {
    $errorMsg = "Statement preparation failed: " . $conn->error;
    error_log("Error: $errorMsg");
    echo json_encode(["status" => "error", "message" => $errorMsg]);
    $conn->close();
    exit();
}

// Debug: Log statement preparation success
error_log("Statement prepared successfully.");

// Bind parameters
$stmt->bind_param("iiii", $facultyID, $courseID, $sessionID, $sectionID);

// Execute the query
if ($stmt->execute()) {
    error_log("Section added to faculty courses successfully.");
    echo json_encode(["status" => "success", "message" => "Section added to faculty courses successfully"]);
} else {
    $errorMsg = "Execution failed: " . $stmt->error;
    error_log("Error: $errorMsg");
    echo json_encode(["status" => "error", "message" => $errorMsg]);
}

// Close the statement and connection
$stmt->close();
$conn->close();

?>
