<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = '';       // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}	

// Get semester description from the request
$FacultyName = $_GET['FacultyName'];
$USER = $_GET['USER'];


// Prepare the SQL statement
$sql = "Select * From sendnotifications 
WHERE ReceiverID = ? AND userID = ?;";

$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $FacultyName, $USER);
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
