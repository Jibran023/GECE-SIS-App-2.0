<?php
header('Content-Type: application/json');

$host = '127.0.0.1:3306';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner'; // MySQL password
$database = 'gecesisapp';  // Your database name

// Get FacultyID, SessionID, and SectionID from the GET request
$facultyID = isset($_GET['FacultyID']) ? $_GET['FacultyID'] : null;
$sessionID = isset($_GET['SessionID']) ? $_GET['SessionID'] : null;
$sectionID = isset($_GET['SectionID']) ? $_GET['SectionID'] : null;

// Check if any of the required parameters are missing
if ($facultyID === null || $sessionID === null || $sectionID === null) {
    echo json_encode(["status" => "error", "message" => "FacultyID, SessionID, and SectionID are required"]);
    exit();
}

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// SQL query to select distinct dates from the attendance table
$sql = "SELECT DISTINCT a.Date 
        FROM attendance a
        INNER JOIN studentMap sm ON a.StudentMapID = sm.id
        INNER JOIN facultycourses fc ON fc.CourseID = sm.CourseID AND fc.SessionID = sm.SessionID AND sm.SectionID = fc.SectionID
        WHERE fc.FacultyID = ? AND fc.SessionID = ? AND fc.SectionID = ?";

$stmt = $conn->prepare($sql);
if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Statement preparation failed: " . $conn->error]);
    exit();
}

// Bind the FacultyID, SessionID, and SectionID parameters to the SQL query
$stmt->bind_param("iss", $facultyID, $sessionID, $sectionID);

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

// Return the results as a JSON object
echo json_encode($data);

$stmt->close();
$conn->close();
?>
