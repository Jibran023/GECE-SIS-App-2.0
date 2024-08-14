<?php

header('Content-Type: application/json');

// Database connection parameters
$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner'; // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Retrieve parameters from the GET request
$courseID = $_GET['CourseID'] ?? '';
$sectionName = $_GET['SectionName'] ?? '';

// Check if required parameters are provided
if (empty($courseID) || empty($sectionName)) {
    echo json_encode(["status" => "error", "message" => "Missing CourseID or SectionName"]);
    exit();
}

// Create a connection to the database
$conn = new mysqli($host, $username, $password, $database);

// Check for connection errors
if ($conn->connect_error) {
    error_log("Connection failed: " . $conn->connect_error);
    echo json_encode(["status" => "error", "message" => "Connection failed"]);
    exit();
}

// Prepare the SQL statement to insert the new section
$query = "INSERT INTO sections (CourseID, SectionName) VALUES (?, ?)";
$stmt = $conn->prepare($query);

if (!$stmt) {
    error_log("Statement preparation failed: " . $conn->error);
    echo json_encode(["status" => "error", "message" => "Statement preparation failed"]);
    $conn->close();
    exit();
}

// Bind parameters
$stmt->bind_param("is", $courseID, $sectionName);

// Execute the query
if ($stmt->execute()) {
    // Retrieve the auto-incremented SectionID
    $sectionID = $stmt->insert_id;
    
    // Log success message
    error_log("Section created successfully with SectionID: $sectionID");
    echo json_encode(["status" => "success", "message" => "Section created successfully", "SectionID" => $sectionID]);
} else {
    error_log("Execution failed: " . $stmt->error);
    echo json_encode(["status" => "error", "message" => "Execution failed"]);
}

// Close the statement and connection
$stmt->close();
$conn->close();

?>
