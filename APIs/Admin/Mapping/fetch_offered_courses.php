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

// Get the roll number and semester description from the request
$rollNumber = $_GET['rollNumber'];


// Prepare the SQL statement
$sql = "Select c.CourseID, c.Name, f.FacultyName, f.FacultyID, a.SessionID, a.Description  From offeredcourses oc
INNER JOIN courses c ON c.CourseID = oc.CourseID
INNER JOIN facultycourses fc ON fc.CourseID = oc.CourseID
INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
INNER JOIN academicsession a ON a.SessionID = oc.SessionID AND a.SessionID = fc.SessionID
WHERE oc.Year = ? AND a.Current = 1;";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $rollNumber);
$stmt->execute();
$result = $stmt->get_result();
// $result = $conn->query($sql);
// echo $result;

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
