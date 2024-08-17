<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();

$result = $con->getCurrentAcademicYears();

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
