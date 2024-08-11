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

// Retrieve data from URL parameters
$userID = $_GET['userID'];
$title = $_GET['Title'];
$content = $_GET['Content'];
$postedDateTime = $_GET['PostedDateTime'];

// Insert the announcement into the database
$sql = "INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo) VALUES (?, ?, ?, ?, 'faculty')";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);

if ($stmt->execute()) {
    // Get the ID of the inserted announcement
    $announcementID = $stmt->insert_id;
    echo json_encode(["success" => true, "announcementID" => $announcementID]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to submit announcement"]);
}

// Close connection
$stmt->close();
$conn->close();
?>
