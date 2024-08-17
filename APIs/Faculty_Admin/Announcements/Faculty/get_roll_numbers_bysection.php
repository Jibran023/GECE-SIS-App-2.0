<?php
$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner';  // MySQL password
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    debug_log("Connection failed: " . $conn->connect_error);
    die("Connection failed: " . $conn->connect_error);
} else {
    debug_log("Database connection successful.");
}

// Retrieve data from URL parameters
$courseName = isset($_GET['CourseName']) ? $_GET['CourseName'] : '';
$sessionDescription = isset($_GET['SessionDescription']) ? $_GET['SessionDescription'] : '';
$sectionName = isset($_GET['SectionName']) ? $_GET['SectionName'] : '';

debug_log("Course Name: $courseName, Session Description: $sessionDescription, Section Name: $sectionName");

// Retrieve RollNumbers and Section ID based on CourseName, SessionDescription, and SectionName
$sql = "SELECT sm.RollNumber, s.Id as SectionID
        FROM studentmap sm
        INNER JOIN offeredcourses oc ON sm.CourseID = oc.CourseID
        INNER JOIN sections s ON sm.SectionID = s.Id
        INNER JOIN academicsession a ON oc.SessionID = a.SessionID
        INNER JOIN courses c ON c.CourseID = oc.CourseID
        WHERE c.Name = ? AND a.Description = ? AND s.SectionName = ?";

if ($stmt = $conn->prepare($sql)) {
    $stmt->bind_param("sss", $courseName, $sessionDescription, $sectionName);
    $stmt->execute();
    $stmt->bind_result($rollNumber, $sectionID);

    $rollNumbers = [];
    $sectionIDs = [];
    while ($stmt->fetch()) {
        $rollNumbers[] = $rollNumber;
        $sectionIDs[] = $sectionID;
        debug_log("RollNumber found: $rollNumber, SectionID found: $sectionID");
    }

    $stmt->close();
} else {
    debug_log("Failed to prepare SQL statement: " . $conn->error);
}

// Return result
echo json_encode(["success" => true, "rollNumbers" => $rollNumbers, "sectionID" => $sectionIDs]);

// Close connection
$conn->close();
debug_log("Connection closed.");

// Debug logging function
function debug_log($message) {
    file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
}
?>
