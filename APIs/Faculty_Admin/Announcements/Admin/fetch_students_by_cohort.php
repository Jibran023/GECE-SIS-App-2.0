<?php
$host = '127.0.0.1:3307';
$username = 'root';
$password = 'mazerunner';
$database = 'gecesisapp';

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$cohorts = isset($_GET['cohorts']) ? explode(',', $_GET['cohorts']) : [];

if (in_array("All", $cohorts)) {
    $sql = "SELECT Name FROM studentsinformation WHERE Status = 'Active'";
    $stmt = $conn->prepare($sql);
} else {
    $cohortsPlaceholder = implode(',', array_fill(0, count($cohorts), '?'));
    $sql = "SELECT Name FROM studentsinformation WHERE Cohort IN ($cohortsPlaceholder) AND Status = 'Active'";
    $stmt = $conn->prepare($sql);
    if ($stmt === false) {
        die("Failed to prepare statement: " . $conn->error);
    }
    $stmt->bind_param(str_repeat('s', count($cohorts)), ...$cohorts);
}

$stmt->execute();
$result = $stmt->get_result();

$students = [];
while ($row = $result->fetch_assoc()) {
    $students[] = $row['Name'];
}

echo json_encode($students);

$stmt->close();
$conn->close();
?>
