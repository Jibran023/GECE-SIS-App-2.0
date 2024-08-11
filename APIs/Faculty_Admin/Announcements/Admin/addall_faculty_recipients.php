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
$announcementID = $_GET['announcementID'];

// Function to get all FacultyIDs
function getAllFacultyIDs($conn) {
    $facultyIDs = [];
    $sql = "SELECT FacultyID FROM faculty";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $facultyIDs[] = $row['FacultyID'];
        }
    }
    return $facultyIDs;
}

// Get all faculty IDs
$facultyIDs = getAllFacultyIDs($conn);

if (empty($facultyIDs)) {
    echo json_encode(["success" => false, "message" => "No faculty members found"]);
    exit();
}

// Insert each faculty member as a recipient
$recipientSql = "INSERT INTO announcementbyro_facultyrecipients (AnnouncementID, FacultyID) VALUES (?, ?)";
$recipientStmt = $conn->prepare($recipientSql);

foreach ($facultyIDs as $facultyID) {
    $recipientStmt->bind_param("ii", $announcementID, $facultyID);
    $recipientStmt->execute();
}

echo json_encode(["success" => true, "message" => "All faculty members added as recipients successfully"]);

// Close connections
$recipientStmt->close();
$conn->close();
?>
