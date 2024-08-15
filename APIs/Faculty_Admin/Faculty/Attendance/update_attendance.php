<?php
header('Content-Type: application/json');

// Database connection parameters
$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner'; // MySQL password
$database = 'gecesisapp';  // Your database name

// Create a connection to the database
$conn = new mysqli($host, $username, $password, $database);

// Check the connection
if ($conn->connect_error) {
    error_log("Connection failed: " . $conn->connect_error);
    echo json_encode(array("status" => "error", "message" => "Connection failed: " . $conn->connect_error));
    exit();
}

// Retrieve data from GET request (URL parameters)
$attendanceStatus = $_GET['AttendanceStatus'];
$name = $_GET['Name'];
$date = $_GET['Date'];
$sessionID = $_GET['SessionID'];
$courseID = $_GET['CourseID'];
$sectionID = $_GET['SectionID'];

// Debug statements to check received data
error_log("Debug: Received AttendanceStatus = " . $attendanceStatus);
error_log("Debug: Received Name = " . $name);
error_log("Debug: Received Date = " . $date);
error_log("Debug: Received SessionID = " . $sessionID);
error_log("Debug: Received CourseID = " . $courseID);
error_log("Debug: Received SectionID = " . $sectionID);

// Prepare and execute the SQL statement
$sql = "UPDATE attendance a
        INNER JOIN studentmap sm ON sm.id = a.StudentMapID
        INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
        SET a.AttendanceStatus = ?
        WHERE si.Name = ? AND a.Date = ? AND sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID = ?";

$stmt = $conn->prepare($sql);

// Check if the statement was prepared successfully
if (!$stmt) {
    error_log("Debug: Prepare failed - " . $conn->error);
    echo json_encode(array("status" => "error", "message" => "Prepare failed: " . $conn->error));
    exit();
} else {
    error_log("Debug: SQL statement prepared successfully.");
}

// Bind parameters
$stmt->bind_param("sssiii", $attendanceStatus, $name, $date, $sessionID, $courseID, $sectionID);
error_log("Debug: Parameters bound successfully.");

// Execute the statement
$executeResult = $stmt->execute();

// Debug statement to check execution result
if ($executeResult) {
    error_log("Debug: Execute succeeded for Name: " . $name);
} else {
    error_log("Debug: Execute failed - " . $stmt->error);
}

// Check if any rows were affected
if ($stmt->affected_rows > 0) {
    error_log("Debug: " . $stmt->affected_rows . " row(s) affected.");
    // Success response
    echo json_encode(array("status" => "success"));
} else {
    error_log("Debug: No rows affected.");
    // Failure response
    echo json_encode(array("status" => "failure", "message" => "No rows affected"));
}

// Close the statement and connection
$stmt->close();
$conn->close();
?>
