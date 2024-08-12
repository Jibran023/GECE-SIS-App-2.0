<?php
$host = '127.0.0.1:3307';
$username = 'root';
$password = 'mazerunner';
$database = 'gecesisapp';

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$cohorts = array_map('trim', explode(',', $_GET['cohorts']));
$students = array_map('trim', explode(',', $_GET['students']));

$rollNumbers = [];

error_log("Cohorts: " . implode(", ", $cohorts));
error_log("Students: " . implode(", ", $students));

// Check if "All" is in cohorts list
if (in_array('All', $cohorts)) {
    error_log("All cohorts selected. Fetching all active students.");
    $sql = "SELECT RollNumber FROM studentmap WHERE status = 'Active'";
    $result = $conn->query($sql);
    if ($result) {
        while ($row = $result->fetch_assoc()) {
            $rollNumbers[] = $row['RollNumber'];
        }
    } else {
        error_log("SQL error when fetching all active students: " . $conn->error);
    }
    error_log("Fetched roll numbers for all cohorts: " . implode(", ", $rollNumbers));
} else {
    // If "All" is not in cohorts list but "All" is in students list
    if (in_array('All', $students)) {
        error_log("All students in selected cohorts. Fetching all active students in the cohorts: " . implode(", ", $cohorts));
        $placeholders = implode(',', array_fill(0, count($cohorts), '?'));
        $sql = "SELECT RollNumber FROM studentmap WHERE status = 'Active' AND cohort IN ($placeholders)";
        $stmt = $conn->prepare($sql);
        if ($stmt) {
            $stmt->bind_param(str_repeat('s', count($cohorts)), ...$cohorts);
            $stmt->execute();
            $result = $stmt->get_result();
            if ($result) {
                while ($row = $result->fetch_assoc()) {
                    $rollNumbers[] = $row['RollNumber'];
                }
            } else {
                error_log("SQL error when fetching active students by cohort: " . $stmt->error);
            }
            $stmt->close();
        } else {
            error_log("SQL prepare statement error: " . $conn->error);
        }
        error_log("Fetched roll numbers for all students in cohorts: " . implode(", ", $rollNumbers));
    } else {
        // No "All" in either list, fetch specific students
        error_log("Specific students selected. Fetching roll numbers for students: " . implode(", ", $students));
        $placeholders = implode(',', array_fill(0, count($students), '?'));
        $sql = "SELECT RollNumber FROM studentsinformation WHERE Name IN ($placeholders)";
        $stmt = $conn->prepare($sql);
        if ($stmt) {
            $stmt->bind_param(str_repeat('s', count($students)), ...$students);
            $stmt->execute();
            $result = $stmt->get_result();
            if ($result) {
                while ($row = $result->fetch_assoc()) {
                    $rollNumbers[] = $row['RollNumber'];
                }
            } else {
                error_log("SQL error when fetching roll numbers for specific students: " . $stmt->error);
            }
            $stmt->close();
        } else {
            error_log("SQL prepare statement error: " . $conn->error);
        }
        error_log("Fetched roll numbers for specific students: " . implode(", ", $rollNumbers));
    }
}

echo json_encode(["success" => true, "rollNumbers" => $rollNumbers]);
error_log("Final roll numbers: " . implode(", ", $rollNumbers));

$conn->close();
?>
