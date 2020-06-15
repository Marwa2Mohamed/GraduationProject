<?php
// required headers
//require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once '../config/database.php';
 
// instantiate job object
include_once '../objects/job.php';
 
$database = new Database();
$db = $database->getConnection();
 
$call_log = new Call_log($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"));
 
// set job property values
$call_log->Call_id = $data->Call_id;
$call_log->Contact_id = $data->Contact_id;
$call_log->Acc_id = $data->Acc_id;
$call_log->Time = $data->Time;
$call_log->day = $data->day;
$call_log->week = $data->week; 
// create the job
if($job->create()){
    echo '{';
        echo '"message": "Job was created."';
    echo '}';
}
 
// if unable to create the job, tell the user
else{
    echo '{';
        echo '"message": "Unable to create Job."';
    echo '}';
}
?>