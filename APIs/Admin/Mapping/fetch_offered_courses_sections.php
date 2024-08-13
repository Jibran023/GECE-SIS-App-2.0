<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner'; // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}	

// Get the year and rollNumber from the request
$year = $_GET['year'];
$rollNumber = $_GET['rollNumber'];

// Prepare the SQL statement
$sql = "SELECT *, s.id as SectionID FROM offeredcourses sm 
INNER JOIN facultycourses fc ON sm.CourseID = fc.CourseID AND sm.SessionID = fc.SessionID
INNER JOIN academicsession a ON a.SessionID = sm.SessionID
INNER JOIN sections s ON s.id = fc.SectionID -- This statement will join us with sections
WHERE sm.Year = ? AND sm.CourseID = ? AND a.Current = 1;";

$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $year, $rollNumber); // 'ss' for string (year and rollNumber)
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
