<?php

header('Content-Type: application/json');

$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = '';       // MySQL password
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get data from request
$MsgIDs = $_GET['MsgIDs'];
$ReadTime = $_GET['ReadTime'];

// Convert MsgIDs to an array
$msgIdsArray = explode(',', $MsgIDs);

// Prepare the SQL statement dynamically based on the number of MsgIDs
$placeholders = implode(',', array_fill(0, count($msgIdsArray), '?'));
$sql = "UPDATE sendnotifications 
        SET ReadTime = ? 
        WHERE MsgID IN ($placeholders)";

// AND (ReadTime IS NULL OR ReadTime = '')


// Prepare the statement
$stmt = $conn->prepare($sql);

// Check if preparation was successful
if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => $conn->error]);
    exit;
}

// Bind parameters dynamically
$params = array_merge([$ReadTime], $msgIdsArray);
$params_types = str_repeat('s', count($params));
$stmt->bind_param($params_types, ...$params);

// Execute the statement
$result = $stmt->execute();

// Check if the update was successful
if ($result) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

// Close the connection
$stmt->close();
$conn->close();

?>
