<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner';       // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}	

// Get semester description from the request
$facultyID = $_GET['facultyID'];
$semesterDescription = $_GET['semesterDescription'];


// Prepare the SQL statement
$sql = "
SELECT c.CourseID, c.Name
FROM courses c
JOIN facultycourses fc ON c.CourseID = fc.CourseID JOIN faculty f ON fc.FacultyID = f.FacultyID
WHERE f.facultyID = ? AND fc.SessionID = (
    SELECT SessionID 
    FROM academicsession 
    WHERE Description = ?
)";

$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $facultyID, $semesterDescription);
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
