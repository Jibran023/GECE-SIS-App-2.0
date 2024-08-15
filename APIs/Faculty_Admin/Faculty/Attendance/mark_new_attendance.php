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
error_log("Received AttendanceStatus: " . $attendanceStatus);
error_log("Received Name: " . $name);
error_log("Received Date: " . $date);
error_log("Received SessionID: " . $sessionID);
error_log("Received CourseID: " . $courseID);
error_log("Received SectionID: " . $sectionID);

// Step 1: Fetch the StudentMapID based on the student's name, SessionID, CourseID, and SectionID
$sqlFetch = "SELECT sm.id AS StudentMapID 
             FROM studentmap sm 
             INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber 
             WHERE si.Name = ? AND sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID = ?";

$stmtFetch = $conn->prepare($sqlFetch);
if (!$stmtFetch) {
    error_log("Prepare failed for fetching StudentMapID: " . $conn->error);
    echo json_encode(array("status" => "error", "message" => "Prepare failed: " . $conn->error));
    exit();
}

// Bind the parameters (name, sessionID, courseID, sectionID)
error_log("Binding parameters for StudentMapID fetch: Name: $name, SessionID: $sessionID, CourseID: $courseID, SectionID: $sectionID");
$stmtFetch->bind_param("ssss", $name, $sessionID, $courseID, $sectionID);
$stmtFetch->execute();
$stmtFetch->bind_result($studentMapID);
$stmtFetch->fetch();
$stmtFetch->close();

// Log the fetched StudentMapID
if ($studentMapID) {
    error_log("Fetched StudentMapID: " . $studentMapID);
} else {
    error_log("No StudentMapID found for the given criteria");
    echo json_encode(array("status" => "error", "message" => "No StudentMapID found for the given criteria"));
    exit();
}

// Step 2: Insert the attendance status into the attendance table
$sqlInsert = "INSERT INTO attendance (StudentMapID, AttendanceStatus, Date, WarningsSent) VALUES (?, ?, ?, 0)";
$stmtInsert = $conn->prepare($sqlInsert);

if (!$stmtInsert) {
    error_log("Prepare failed for attendance insertion: " . $conn->error);
    echo json_encode(array("status" => "error", "message" => "Prepare failed: " . $conn->error));
    exit();
}

// Bind the parameters (studentMapID, attendanceStatus, date)
error_log("Binding parameters for attendance insertion: StudentMapID: $studentMapID, AttendanceStatus: $attendanceStatus, Date: $date");
$stmtInsert->bind_param("sss", $studentMapID, $attendanceStatus, $date);
$executeResult = $stmtInsert->execute();

if ($executeResult) {
    error_log("Execute succeeded for StudentMapID: " . $studentMapID);
    echo json_encode(array("status" => "success"));
} else {
    error_log("Execute failed: " . $stmtInsert->error);
    echo json_encode(array("status" => "failure", "message" => "Failed to insert attendance"));
}

// Close the statement and connection
$stmtInsert->close();
$conn->close();
?>
