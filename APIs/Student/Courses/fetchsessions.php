<?php

header('Content-Type: application/json');

// Database connection parameters
$host = '127.0.0.1';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = '';       // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}	


// Prepare the SQL statement
$sql = "SELECT Description FROM academicsession";
$stmt = $conn->prepare($sql);
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
