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
$ID = $_GET['ID']; // This ID is used to exclude from results

// Function to get all FacultyIDs
function getAllFacultyIDs($conn, $excludeID) {
    $facultyIDs = [];
    $sql = "SELECT id FROM users WHERE id != ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $excludeID);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $facultyIDs[] = $row['id']; // Assuming 'id' is the correct column name
        }
    }
    $stmt->close();
    return $facultyIDs;
}

// Get all faculty IDs excluding the one specified by $ID
$facultyIDs = getAllFacultyIDs($conn, $ID);

if (empty($facultyIDs)) {
    echo json_encode(["success" => false, "message" => "No faculty members found"]);
    exit();
}

// Insert each faculty member as a recipient
$recipientSql = "INSERT INTO announcementbyro_userrecipients (AnnouncementID, UserID) VALUES (?, ?)";
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
