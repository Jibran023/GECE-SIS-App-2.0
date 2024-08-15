<?php
header('Content-Type: application/json');

$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username
$password = 'mazerunner'; // MySQL password
$database = 'gecesisapp';  // Your database name

// Get FacultyID from the GET request
$facultyID = isset($_GET['FacultyID']) ? $_GET['FacultyID'] : null;
$sectionID = isset($_GET['SectionID']) ? $_GET['SectionID'] : null;
$date = isset($_GET['Date']) ? $_GET['Date'] : null;

if ($facultyID === null) {
    echo json_encode(["status" => "error", "message" => "FacultyID is required"]);
    exit();
}
if ($sectionID === null) {
    echo json_encode(["status" => "error", "message" => "SectionID is required"]);
    exit();
}
if ($date === null) {
    echo json_encode(["status" => "error", "message" => "Date is required"]);
    exit();
}

// Create connection
$conn = new mysqli($host, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// SQL query to select all columns from the users table
$sql = "Select * From studentmap sm 
INNER JOIN facultycourses fc ON sm.SessionID = fc.SessionID AND fc.CourseID = sm.CourseID AND fc.SectionID = sm.SectionID
Inner Join studentsinformation si ON si.RollNumber = sm.RollNumber
WHERE fc.FacultyID = ? AND fc.SectionID = ? AND fc.SessionID = ?";

$stmt = $conn->prepare($sql);
if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Statement preparation failed: " . $conn->error]);
    exit();
}

// Bind the FacultyID parameter to the SQL query
$stmt->bind_param("iii", $facultyID, $sectionID, $date);

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

echo json_encode($data);

$stmt->close();
$conn->close();
?>
