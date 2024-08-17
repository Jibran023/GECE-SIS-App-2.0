<?php
$host = '127.0.0.1:3306';
$username = 'root';
$password = 'mazerunner';
$database = 'gecesisapp';

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$announcementID = $_GET['announcementID'];
$rollNumbers = array_map('intval', explode(',', $_GET['rollNumbers']));

$recipientSql = "INSERT INTO announcementbyro_studentrecipients (AnnouncementID, RollNumber) VALUES (?, ?)";
$recipientStmt = $conn->prepare($recipientSql);

foreach ($rollNumbers as $rollNumber) {
    if (!empty($rollNumber)) {
        $recipientStmt->bind_param("ii", $announcementID, $rollNumber);
        if (!$recipientStmt->execute()) {
            echo json_encode(["success" => false, "message" => "Failed to insert recipient with RollNumber: $rollNumber"]);
            $recipientStmt->close();
            $conn->close();
            exit();
        }
    }
}

echo json_encode(["success" => true, "message" => "Recipients added successfully"]);

$recipientStmt->close();
$conn->close();
?>
