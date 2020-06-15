<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/job.php';
 
// get database connection
$database = new Database();
$db = $database->getConnection();
 
// prepare Job object
$job = new Job($db);
 
// set ID property of job to be edited
$job->id = isset($_GET['Call_id']) ? $_GET['Call-id'] : die();
 
// read the details of product to be edited
$job->readOne();
 
// create array
$job_arr = array();
$job_arr["records"] = array();

$job_item = array(
    "Call_id" => $Call_id,
    "Contact_id" => $Contact_id,
    "Acc_id" => $Acc_id,
    "Time" => $Time,          
    "day" => $day,
    "week" =>$week
 
);
     array_push($job_arr["records"], $job_item);
 
// make it json format
print_r(json_encode($job_arr));
?>