<?php
// required headers
//require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
 
// include database and object file
include_once '../config/database.php';
include_once '../objects/job.php';
 
// get database connection
$database = new Database();
$db = $database->getConnection();
 
// prepare job object
$job = new Job($db);
 
// get job id
$data = json_decode(file_get_contents("php://input"));
 
// set job id to be deleted
$job->Call_id = $data->Call_id;
 
// delete the job
if($job->delete()){
    echo '{';
        echo '"message": "Job was deleted."';
    echo '}';
}
 
// if unable to delete the job
else{
    echo '{';
        echo '"message": "Unable to delete object."';
    echo '}';
}
?>