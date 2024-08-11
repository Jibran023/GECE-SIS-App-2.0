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

// Debug: Check if URL parameters are received
if (!isset($_GET['userID']) || !isset($_GET['Title']) || !isset($_GET['Content']) || !isset($_GET['PostedDateTime'])) {
    die(json_encode(["success" => false, "message" => "Missing required parameters"]));
}

// Retrieve data from URL parameters
$userID = $_GET['userID'];
$title = $_GET['Title'];
$content = $_GET['Content'];
$postedDateTime = $_GET['PostedDateTime'];

// Debug: Display retrieved parameters
error_log("userID: $userID, Title: $title, Content: $content, PostedDateTime: $postedDateTime");

// Insert the announcement into the database
$sql = "INSERT INTO announcements (facultyCoursesID, Title, Content, PostedDateTime) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);

// Debug: Check if SQL statement is prepared successfully
if (!$stmt) {
    die(json_encode(["success" => false, "message" => "Failed to prepare SQL statement: " . $conn->error]));
}

// Bind parameters and execute the statement
$stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);

// Debug: Check if statement execution is successful
if ($stmt->execute()) {
    // Get the ID of the inserted announcement
    $announcementID = $announcementID = $stmt->insert_id;

    // Debug: Display the ID of the inserted announcement
    error_log("Announcement inserted with ID: $announcementID");

    echo json_encode(["success" => true, "announcementID" => $announcementID]);
} else {
    // Debug: Log the error if the statement execution fails
    error_log("Failed to execute statement: " . $stmt->error);
    echo json_encode(["success" => false, "message" => "Failed to submit announcement"]);
}

// Close connection
$stmt->close();
$conn->close();
?>
