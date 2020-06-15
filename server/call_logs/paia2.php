<?php
//require("admin_auth.php");
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once '../config/Database.php';
 
// instantiate job object
include_once '../objects/call_logs.php';

$database = new Database();
$db = $database->getConnection();
 
$call_logs = new Call_log($db);


  // $data = '{"CallLogs": [ {"Acc_id":1},{"ID":"2742","number":"01099203204","time":"01","Day":"Fri"},{"ID":"2252","number":"01099203204","time":"01","Day":"Fri"},{"ID":"2991","number":"01116688933","time":"00","Day":"Sat"},{"ID":"2762","number":"01153919837","time":"10","Day":"Sat"} ]}';

    $data =isset($_POST["json"]) ? $_POST["json"] : die(); // put the contents of the file into a variable
    echo $data .'<br/>';
    $characters = json_decode($data); // decode the JSON feed
    //echo $characters->CallLogs;
    $num = count($characters->CallLogs);
    echo $num, "\n";
    if($num !=0){
    $x =0;
    for ($x=1;$x<$num;$x++) {
        //$characters as $character;
        echo $x;
        echo $characters->CallLogs[1]->number;
        $call_logs->Acc_id = $characters->CallLogs[0]->Acc_id;
        $call_logs->number = $characters->CallLogs[$x]->number;
        $call_logs->time = $characters->CallLogs[$x]->time;
        $call_logs->day = $characters->CallLogs[$x]->Day;
        $call_logs->Contact_id = $characters->CallLogs[$x]->ID;// create the job
        if($call_logs->create()){
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


    }
    

        
    }elseif($num == 0){ 
        echo "Nothing will be entered","\n";
        } 
    
 
//echo 'post fail';

   // if( isset($_POST["json"])) {}else{//important }

    ?>

