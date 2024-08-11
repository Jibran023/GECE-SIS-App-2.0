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

// SQL query to select all columns from the users table
$sql = "Select ar.Title, ar.Content, u.Name , ar.PostedDateTime
From announcementbyro_studentrecipients af 
INNER JOIN announcementbyro ar ON af.AnnouncementID = ar.AnnouncementID
INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
INNER JOIN users u ON ar.userID = u.id
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