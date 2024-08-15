<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner'; // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Retrieve parameters from the GET request
$facultyID = $_GET['FacultyID'] ?? '';
$courseID = $_GET['CourseID'] ?? '';
$sessionID = $_GET['SessionID'] ?? '';
$sectionID = $_GET['SectionID'] ?? '';

// Create a connection to the database
$conn = new mysqli($host, $username, $password, $database);

// Check for connection errors
if ($conn->connect_error) {
    // Log connection error
    error_log("Connection failed: " . $conn->connect_error);
    echo json_encode(["status" => "error", "message" => "Connection failed"]);
    exit();
}

// Prepare the SQL statement to update SectionID
$query = "Delete From facultycourses fc
    WHERE fc.FacultyID = ? 
    AND fc.CourseID = ? 
    AND fc.SessionID = ?
    AND fc.SectionID = ? ";
$stmt = $conn->prepare($query);

if (!$stmt) {
    // Log statement preparation error
    error_log("Statement preparation failed: " . $conn->error);
    echo json_encode(["status" => "error", "message" => "Statement preparation failed"]);
    $conn->close();
    exit();
}

// Bind parameters
$stmt->bind_param("ssss", $facultyID, $courseID, $sessionID, $sectionID);

// Execute the query
if ($stmt->execute()) {
    // Check if any rows were affected
    if ($stmt->affected_rows > 0) {
        // Log success message
        error_log("Update completed successfully");
        echo json_encode(["status" => "success", "message" => "Update completed successfully"]);
    } else {
        // No rows were affected, meaning no matching records were found
        echo json_encode(["status" => "warning", "message" => "No records found to update"]);
    }
} else {
    // Log execution error
    error_log("Execution failed: " . $stmt->error);
    echo json_encode(["status" => "error", "message" => "Execution failed"]);
}

// Close the statement and connection
$stmt->close();
$conn->close();

?>
