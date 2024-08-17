<?php
$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner';  // MySQL password
$database = 'gecesisapp';  // Your database name

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    debug_log("Connection failed: " . $conn->connect_error);
    die("Connection failed: " . $conn->connect_error);
} else {
    debug_log("Database connection successful.");
}

// Retrieve data from URL parameters
$announcementID = isset($_GET['announcementID']) ? $_GET['announcementID'] : '';
$rollNumbers = isset($_GET['rollNumbers']) ? $_GET['rollNumbers'] : '';
$sectionID = isset($_GET['sectionID']) ? $_GET['sectionID'] : '';

debug_log("Announcement ID: $announcementID, RollNumbers (raw): $rollNumbers");

// Convert rollNumbers to array if it's a comma-separated string
if (is_string($rollNumbers)) {
    debug_log("rollNumbers received as string: $rollNumbers");
    $rollNumbers = explode(',', $rollNumbers);  // Split the string into an array
    debug_log("rollNumbers converted to array: " . implode(", ", $rollNumbers));
}

// Check if rollNumbers is an array
if (!is_array($rollNumbers)) {
    debug_log("Error: RollNumbers is not an array.");
    echo json_encode(["success" => false, "message" => "Invalid RollNumbers format."]);
    exit();
}

// Insert each student as a recipient in the announcementrecipients table
$insertSql = "INSERT INTO announcementrecipients (AnnouncementID, RollNumber, sectionID) VALUES (?, ?, ?)";
$insertStmt = $conn->prepare($insertSql);

if ($insertStmt === false) {
    debug_log("Error preparing statement: " . $conn->error);
    echo json_encode(["success" => false, "message" => "Failed to prepare statement."]);
    exit();
}

foreach ($rollNumbers as $rollNumber) {
    debug_log("Attempting to add RollNumber: $rollNumber");

    $insertStmt->bind_param("iii", $announcementID, $rollNumber, $sectionID);
    if (!$insertStmt->execute()) {
        debug_log("Failed to add RollNumber: $rollNumber - Error: " . $insertStmt->error);
        echo json_encode(["success" => false, "message" => "Failed to add some recipients"]);
        exit();
    } else {
        debug_log("Successfully added RollNumber: $rollNumber");
    }
}

debug_log("All RollNumbers added successfully.");

echo json_encode(["success" => true, "message" => "Recipients added successfully"]);

// Close statements
$insertStmt->close();
$conn->close();
debug_log("Database connection closed.");

// Debug logging function
function debug_log($message) {
    file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
}
?>
