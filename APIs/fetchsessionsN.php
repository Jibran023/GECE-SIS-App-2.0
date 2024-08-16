<?php
require_once('class/querieshandler.php');

$con = new querieshandler();

$result = $con->FetchSessions();

// Output the result as JSON
header('Content-Type: application/json');
echo json_encode($result);
?>