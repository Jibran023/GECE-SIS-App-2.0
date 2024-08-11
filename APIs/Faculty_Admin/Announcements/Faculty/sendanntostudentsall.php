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
$announcementID = intval($_GET['announcementID']);
$rollNumbers = $_GET['rollNumbers'];
$rollNumbersArray = explode(",", $rollNumbers);

// Debug: Check incoming roll numbers
error_log("Incoming Roll Numbers: " . json_encode($rollNumbersArray));

if (count($rollNumbersArray) > 0) {
    $insertSql = "INSERT INTO announcementrecipients (AnnouncementID, RollNumber) VALUES (?, ?)";
    $insertStmt = $conn->prepare($insertSql);

    foreach ($rollNumbersArray as $rollNumber) {
        $rollNumber = intval($rollNumber); // Ensure it's an integer
        $insertStmt->bind_param("ii", $announcementID, $rollNumber);
        if (!$insertStmt->execute()) {
            error_log("Failed to insert RollNumber: " . $rollNumber);
        }
    }

    echo json_encode(["success" => true, "message" => "Recipients added successfully"]);

    $insertStmt->close();
} else {
    echo json_encode(["success" => false, "message" => "No roll numbers provided"]);
}

// Close connection
$conn->close();
?>
