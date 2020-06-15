<?php
// required headers
//require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Content-Type: application/json; charset=UTF-8");
 
// include database and object files
include_once '../config/database.php';
include_once '../objects/job.php';
 
// instantiate database and job object
$database = new Database();
$db = $database->getConnection();
 
// initialize object
$job = new Call_log($db);
 
// get keywords
$keywords=isset($_GET["s"]) ? $_GET["s"] : "";
 
// query jobs
$stmt = $job->search($keywords);
$num = $stmt->rowCount();
 
// check if more than 0 record found
if($num>0){
 
    // jobs array
    $jobs_arr=array();
    $jobs_arr["records"]=array();
 
    // retrieve our table contents
    // fetch() is faster than fetchAll()
    // http://stackoverflow.com/questions/2770630/pdofetchall-vs-pdofetch-in-a-loop
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        // extract row
        // this will make $row['name'] to
        // just $name only
        extract($row);
 
        $job_item=array(
            "Call_id" => $Call_id,
            "Contact_id" => $Contact_id,
            "Acc_id" => $Acc_id,
            "Time" => $Time,          
            "day" => $day,
            "week" =>$week
        );
 
        array_push($jobs_arr["records"], $job_item);
    }
 
    echo json_encode($jobs_arr);
}
 
else{
    echo json_encode(
        array("message" => "No jobs found.")
    );
}
?>