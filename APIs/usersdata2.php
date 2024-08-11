<?php

// Function to encode data to Base64
function encodeToBase64($data) {
    // Convert data to JSON string
    $jsonData = json_encode($data);
    
    // Encode JSON string to Base64
    return base64_encode($jsonData);
}

// Database connection parameters
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

// SQL query to select all columns from the users table
$sql = "SELECT * FROM lugpa";
$result = $conn->query($sql);

// Create an array to hold the results
$data = array();

// Check if there are results
if ($result->num_rows > 0) {
    // Fetch all rows as associative arrays
    while ($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
} else {
    $data = array("message" => "No results");
}

echo json_encode($data);

// Close connection
$conn->close();
?>
