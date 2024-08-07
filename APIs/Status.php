<?php
//print_r("In PHP File");
$Status=$_GET['Status'];
require_once('Adminquerieshandler.php'); 
$dbcon=new querieshandler();
$result= $dbcon->UpdateStatusOfApplicants($_POST['id'],$Status);
echo '<script> alert("Action Successfully Posted")</script>';
?>