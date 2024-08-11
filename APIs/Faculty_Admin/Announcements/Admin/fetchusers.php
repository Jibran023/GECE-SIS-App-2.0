<?php
$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner';       // MySQL password
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Retrieve the ID parameter from the URL
$ID = $_GET['ID'];

// SQL query to select all columns from the users table, excluding the user with the given ID
$sql = "SELECT * FROM users WHERE id != ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $ID);
$stmt->execute();
$result = $stmt->get_result();

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

// Set the content type to JSON
header('Content-Type: application/json');

// Output the data as JSON
echo json_encode($data);

// Close connections
$stmt->close();
$conn->close();
?>
