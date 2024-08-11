<?php
// Database connection
$host = '127.0.0.1:3307';  // Host name
$username = 'root';   // MySQL username (default is 'root' for XAMPP)
$password = 'mazerunner';       // MySQL password (default is empty for XAMPP)
$database = 'gecesisapp';  // Your database name

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Get the JSON input
    $data = json_decode(file_get_contents("php://input"), true);

    $userID = $data['userID'];
    $title = $data['Title'];
    $content = $data['Content'];
    $postedDateTime = $data['PostedDateTime'];
    $facultyMembers = $data['FacultyMembers'];

    // Insert the announcement into the announcementbyro table
    $stmt = $pdo->prepare("INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo)
                           VALUES (:userID, :Title, :Content, :PostedDateTime, 'Faculty')");
    $stmt->execute([
        ':userID' => $userID,
        ':Title' => $title,
        ':Content' => $content,
        ':PostedDateTime' => $postedDateTime
    ]);

    // Get the last inserted AnnouncementID
    $announcementID = $pdo->lastInsertId();

    // Insert into announcementbyro_facultyrecipients for each selected faculty member
    foreach ($facultyMembers as $facultyID) {
        $stmt = $pdo->prepare("INSERT INTO announcementbyro_facultyrecipients (AnnouncementID, FacultyID)
                               VALUES (:AnnouncementID, :FacultyID)");
        $stmt->execute([
            ':AnnouncementID' => $announcementID,
            ':FacultyID' => $facultyID
        ]);
    }

    // Return success response
    echo json_encode(['success' => true]);

} catch (PDOException $e) {
    // Return error response
    echo json_encode(['success' => false, 'error' => $e->getMessage()]);
}
?>
