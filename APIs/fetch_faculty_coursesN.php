<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$facultyID = isset($_GET['FacultyID']) ? $_GET['FacultyID'] : null;

$result = $con->getCurrentAcademicYearsByFaculty($facultyID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
