<?php

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

$id = $_GET['id'];


// SQL query to select all columns from the users table
$sql = "Select ar.Title, ar.Content, f.FacultyName , ar.PostedDateTime, c.Name as NAME
From announcementrecipients af 
INNER JOIN announcements ar ON af.AnnouncementID = ar.AnnouncementID
INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
INNER JOIN facultycourses fc ON ar.facultyCoursesID = fc.CourseID
INNER JOIN faculty f ON fc.id = f.FacultyID
INNER JOIN courses c ON c.CourseID = fc.CourseID
WHERE si.RollNumber = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $id);
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
