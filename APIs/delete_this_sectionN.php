<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$sectionID = $_GET['SectionID'];

$result = $con->deleteSection($sectionID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>