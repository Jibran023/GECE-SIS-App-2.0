<?php
require_once('class/adminquerieshandler.php');

$con = new querieshandler();
$ID = $_GET['ID'];

$result = $con->fetchUsersExcludingId($ID);

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>
