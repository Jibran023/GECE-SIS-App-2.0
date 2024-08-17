<?php
header('Content-Type: application/json');

$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner'; // MySQL password
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// SQL query to select distinct years from offeredcourses where the academic session is current
$sql = "SELECT DISTINCT oc.Year 
        FROM offeredcourses oc
        INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
        WHERE a.Current = 1";

$stmt = $conn->prepare($sql);
if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Statement preparation failed: " . $conn->error]);
    exit();
}

// Execute the query
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

echo json_encode($data);

$stmt->close();
$conn->close();
?>
