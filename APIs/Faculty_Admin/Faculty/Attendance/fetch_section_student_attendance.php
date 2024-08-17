<?php
header('Content-Type: application/json');

$host = '127.0.0.1:3306';  // Host name
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
$sql = "Select si.Name, ac.AttendanceStatus From facultycourses fc
Inner Join offeredcourses oc ON fc.CourseID = oc.CourseID AND fc.SessionID = oc.SessionID
INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
INNER JOIN courses c ON fc.CourseID = c.CourseID
INNER JOIN sections s ON fc.SectionID = s.id
INNER JOIN studentmap sm ON sm.SessionID = oc.SessionID AND sm.CourseID = oc.CourseID AND sm.SectionID = fc.SectionID
INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
INNER JOIN attendance ac ON ac.StudentMapID = sm.id
Where a.Current = 1 AND f.FacultyID = ? AND s.id = ? AND ac.Date = ?";

$stmt = $conn->prepare($sql);
if (!$stmt) {
    echo json_encode(["status" => "error", "message" => "Statement preparation failed: " . $conn->error]);
    exit();
}

// Bind the FacultyID parameter to the SQL query
$stmt->bind_param("iis", $facultyID, $sectionID, $date);

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
