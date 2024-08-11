<?php

$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner';       // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Create a connection
$conn = new mysqli($host, $username, $password, $database);

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Retrieve the parameters
$facultyID = isset($_GET['facultyID']) ? $_GET['facultyID'] : '';
$courseName = isset($_GET['courseName']) ? $_GET['courseName'] : '';
$sessionDescription = isset($_GET['sessionDescription']) ? $_GET['sessionDescription'] : '';

// Prepare the SQL query
$sql = "
SELECT s.SectionName
FROM faculty f
INNER JOIN facultycourses fc ON f.FacultyID = fc.FacultyID
INNER JOIN courses c ON c.CourseID = fc.CourseID
INNER JOIN sections s ON s.CourseID = fc.CourseID
INNER JOIN academicsession a ON a.SessionID = fc.SessionID
WHERE f.FacultyID = ? AND c.Name = ? AND a.Description = ?
";

// Prepare the statement
$stmt = $conn->prepare($sql);
if ($stmt === false) {
    die("Error preparing statement: " . $conn->error);
}

// Bind the parameters
$stmt->bind_param("iss", $facultyID, $courseName, $sessionDescription);

// Execute the query
$stmt->execute();

// Get the result
$result = $stmt->get_result();

// Fetch the section names and output them as JSON
$sections = array();
while ($row = $result->fetch_assoc()) {
    $sections[] = $row['SectionName'];
}

// Return the section names as a JSON response
header('Content-Type: application/json');
echo json_encode($sections);

// Close the connection
$stmt->close();
$conn->close();
?>
