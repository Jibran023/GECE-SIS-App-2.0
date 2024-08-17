<?php
$host = '127.0.0.1:3306';  // Host name
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
$facultyMembers = array_map('trim', explode(',', $_GET['FacultyMembers']));  // Converts comma-separated string to array

// Function to get FacultyID by FacultyName
function getFacultyID($facultyName, $conn) {
    $sql = "SELECT FacultyID FROM faculty WHERE FacultyName = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $facultyName);
    $stmt->execute();
    $stmt->bind_result($facultyID);
    $stmt->fetch();
    $stmt->close();
    return $facultyID;
}

// Insert the announcement into the database
$sql = "INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo) VALUES (?, ?, ?, ?, 'Faculty')";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);

if ($stmt->execute()) {
    // Get the ID of the inserted announcement
    $announcementID = $stmt->insert_id;

    // Insert each faculty member as a recipient
    $recipientSql = "INSERT INTO announcementbyro_facultyrecipients (AnnouncementID, FacultyID) VALUES (?, ?)";
    $recipientStmt = $conn->prepare($recipientSql);

    foreach ($facultyNames as $facultyName) {
        if (!empty($facultyName)) { // Ensure facultyName is not empty
            $facultyID = getFacultyID($facultyName, $conn);
            if ($facultyID !== null) { // Ensure facultyID was found
                $recipientStmt->bind_param("ii", $announcementID, $facultyID);
                $recipientStmt->execute();
            } else {
                // Optionally handle case where FacultyName is not found
                echo json_encode(["success" => false, "message" => "Faculty name not found: $facultyName"]);
                exit();
            }
        }
    }

    echo json_encode(["success" => true, "message" => "Announcement submitted successfully"]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to submit announcement"]);
}

// Close connection
$stmt->close();
$conn->close();
?>
