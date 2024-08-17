<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner';       // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get the parameters from the request
$rollNumber = $_GET['rollNumber'];
$semesterDescription = $_GET['semesterDescription'];
$courseID = $_GET['courseID'];

// Prepare the SQL statement
$sql = "
SELECT a.Date, a.AttendanceStatus
FROM studentmap sm
INNER JOIN attendance a ON a.StudentMapID = sm.id
INNER JOIN academicsession ac ON ac.SessionID = sm.SessionID
WHERE sm.RollNumber = ? AND ac.Description = ? AND sm.CourseID = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("sss", $rollNumber, $semesterDescription, $courseID);
$stmt->execute();
$result = $stmt->get_result();

// Fetch data
$data = array();
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

// Output the data as JSON
echo json_encode($data);

// Close connection
$stmt->close();
$conn->close();

?>
