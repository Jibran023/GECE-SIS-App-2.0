
<?php
require_once('Adminquerieshandler.php');
$con=new querieshandler();
$result= $con->GetStudentsByCohort($_GET['cohort']);
print_r($result);
?>