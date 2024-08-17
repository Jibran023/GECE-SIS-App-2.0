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

$announcementID = $_GET['announcementID'];
$facultyNames = array_map('trim', explode(',', $_GET['FacultyMembers']));  // Converts comma-separated string to array and trims spaces
$ID = $_GET['ID'];

// Function to get FacultyID by FacultyName
function getFacultyID($facultyName, $conn, $ID) {
    $sql = "SELECT id FROM users WHERE Name = ? AND id != ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("si", $facultyName, $ID);
    $stmt->execute();
    $stmt->bind_result($facultyID);
    $stmt->fetch();
    $stmt->close();
    
    // Prepare debugging information
    $debugInfo = [
        "Query" => $sql,
        "FacultyName" => $facultyName,
        "FacultyID" => $facultyID,
        "ID" => $ID
    ];
    
    // Return debug information as JSON
    echo json_encode($debugInfo);
    
    return $facultyID;
}

// Insert each faculty member as a recipient
$recipientSql = "INSERT INTO announcementbyro_userrecipients (AnnouncementID, UserID) VALUES (?, ?)";
$recipientStmt = $conn->prepare($recipientSql);

$debugOutput = [];

foreach ($facultyNames as $facultyName) {
    if (!empty($facultyName)) { // Ensure facultyName is not empty
        $facultyID = getFacultyID($facultyName, $conn, $ID);
        if ($facultyID !== null) { // Ensure facultyID was found
            $recipientStmt->bind_param("ii", $announcementID, $facultyID);
            $recipientStmt->execute();
        } else {
            // Optionally handle case where FacultyName is not found
            $debugOutput[] = ["success" => false, "message" => "Faculty name not found: $facultyName"];
            echo json_encode($debugOutput);
            exit();
        }
    }
}

$debugOutput[] = ["success" => true, "message" => "Recipients added successfully"];
echo json_encode($debugOutput);
?>
