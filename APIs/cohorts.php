<?php
require_once ('Adminquerieshandler.php');
$con=new querieshandler();
$result= $con->GetCohorts();
print_r($result);
?>