<?php
// Check if the required GET parameters are set
if (isset($_GET['rollNumber']) && isset($_GET['courseID'])) {
    $rollNumber = $_GET['rollNumber'];
    $courseID = $_GET['courseID'];

    // Database connection (replace with your actual connection details)
    $conn = new mysqli("127.0.0.1:3307", "root", "mazerunner", "gecesisapp");

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // Prepare the SQL query
    $sql = "SELECT 
                r.CourseID,
    r.Internal,
    r.External,
    r.Internal + r.External AS TotalMarks,
    l.Grade,
    l.GradePoint AS GPA
FROM
    results r
INNER JOIN courses c ON r.CourseID = c.CourseID
INNER JOIN lugpa l ON r.id = l.id  -- Assuming a relationship between results and lugpa
WHERE
    r.RollNumber = ?
    AND c.Name = ?";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ii", $rollNumber, $courseID); // Adjust parameter types if necessary (i for integer, s for string)
    $stmt->execute();
    $result = $stmt->get_result();

    $courses = array();

    while ($row = $result->fetch_assoc()) {
        $courses[] = $row;
    }

    echo json_encode($courses);

    $stmt->close();
    $conn->close();
} else {
    echo json_encode(array("error" => "Missing rollNumber or courseID"));
}
?>
