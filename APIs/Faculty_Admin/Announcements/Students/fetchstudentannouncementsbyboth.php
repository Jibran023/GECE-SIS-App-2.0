<?php

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

$id = $_GET['id'];

// SQL query to get announcements from student recipients
$sql1 = "SELECT ar.Title, ar.Content, u.Name, ar.PostedDateTime, u.department AS Course
         FROM announcementbyro_studentrecipients af 
         INNER JOIN announcementbyro ar ON af.AnnouncementID = ar.AnnouncementID
         INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
         INNER JOIN users u ON ar.userID = u.id
         WHERE si.RollNumber = ?";

// Prepare and execute the first query
$stmt1 = $conn->prepare($sql1);
$stmt1->bind_param("s", $id);
$stmt1->execute();
$result1 = $stmt1->get_result();

// Fetch data from the first query
$data1 = array();
while ($row = $result1->fetch_assoc()) {
    $data1[] = $row;
}

// SQL query to get announcements from faculty recipients
$sql2 = "SELECT ar.Title, ar.Content, f.FacultyName AS Name, ar.PostedDateTime, c.Name AS Course
         FROM announcementrecipients af 
         INNER JOIN announcements ar ON af.AnnouncementID = ar.AnnouncementID
         INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
         INNER JOIN facultycourses fc ON ar.facultyCoursesID = fc.CourseID
         INNER JOIN faculty f ON fc.id = f.FacultyID
         INNER JOIN courses c ON c.CourseID = fc.CourseID
         WHERE si.RollNumber = ?";

// Prepare and execute the second query
$stmt2 = $conn->prepare($sql2);
$stmt2->bind_param("s", $id);
$stmt2->execute();
$result2 = $stmt2->get_result();

// Fetch data from the second query
$data2 = array();
while ($row = $result2->fetch_assoc()) {
    $data2[] = $row;
}

// Combine results from both queries
$combinedData = array_merge($data1, $data2);

// Output the combined data as JSON
echo json_encode($combinedData);

// Close statements and connection
$stmt1->close();
$stmt2->close();
$conn->close();

?>
