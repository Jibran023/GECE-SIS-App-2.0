<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner'; // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

$sessionID = $_GET['SessionID'];
$courseID = $_GET['CourseID'];
$sectionID = $_GET['SectionID'];
$studentNames = urldecode($_GET['StudentNames']); // Decode URL-encoded parameter
$studentNames = explode(",", $studentNames); // Split into array

// Debug: Log incoming parameters
error_log("SessionID: $sessionID, CourseID: $courseID, SectionID: $sectionID, StudentNames: " . implode(", ", $studentNames));

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    // Debug: Log connection error
    error_log("Connection failed: " . $conn->connect_error);
    echo json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]);
    exit();
}

$query = "
    UPDATE studentmap sm
    INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
    SET sm.SectionID = ?
    WHERE sm.SessionID = ? 
    AND sm.CourseID = ? 
    AND si.Name = ?";

$stmt = $conn->prepare($query);

if (!$stmt) {
    // Debug: Log statement preparation error
    error_log("Statement preparation failed: " . $conn->error);
    echo json_encode(["status" => "error", "message" => "Statement preparation failed: " . $conn->error]);
    exit();
}

foreach ($studentNames as $studentName) {
    // Trim any whitespace
    $studentName = trim($studentName);

    // Log the parameters and the query being executed
    error_log("Executing query with parameters: SessionID=$sessionID, CourseID=$courseID, SectionID=$sectionID, StudentName=$studentName");

    // Bind parameters
    $stmt->bind_param("iiss", $sectionID, $sessionID, $courseID, $studentName);

    if (!$stmt->execute()) {
        // Debug: Log statement execution error
        error_log("Execution failed for StudentName: $studentName, Error: " . $stmt->error);
        echo json_encode(["status" => "error", "message" => "Execution failed for StudentName: $studentName, Error: " . $stmt->error]);
        exit();
    }

    // Debug: Log successful execution for each student
    error_log("Successfully updated StudentName: $studentName");
}

echo json_encode(["status" => "success"]);

// Debug: Log success message
error_log("Update completed successfully");

$stmt->close();
$conn->close();

?>
