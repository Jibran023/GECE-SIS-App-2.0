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
$id = $_GET['id'];

// Prepare the SQL statement
$sql = "Select * From studentmap sm
INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
Where sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID != ?;";

$stmt = $conn->prepare($sql);
$stmt->bind_param("sss", $year, $rollNumber, $id); // 'ss' for string (year and rollNumber)
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
