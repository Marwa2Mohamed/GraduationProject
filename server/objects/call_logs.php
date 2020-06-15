<?php
include_once '../config/Database.php';
class Call_log{
 
    // database connection and table name
    private $conn;
    private $table_name = "call_log";
 
    // object properties
    public $Call_id ;
    public $number ;
    public $Acc_id;
    public $Contact_id;
    public $time;          
    public $day;
    public $week;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    function create(){
        // query to insert record
    
       
        $query1="INSERT INTO contact(contact_id, phone_number,Acc_id)  VALUES ('".$this->Contact_id."','".$this->number."','".$this->Acc_id."')";
        $stmt = $this->conn->prepare($query1);
                $this->Contact_id=htmlspecialchars(strip_tags($this->Contact_id));
                $this->Acc_id=htmlspecialchars(strip_tags($this->Acc_id));


        // bind values
    
        $stmt->bindParam(":Contact_id", $this->Contact_id);
        $stmt->bindParam(":phone_number", $this->number);   
        $stmt->bindParam(":Acc_id", $this->Acc_id);
        $stmt->execute();
  

        $date =date("Y-m-d");
        $week = ceil(date('d',strtotime($date))/7);
        $query2 = "INSERT INTO call_log (Contact_id, Acc_id, time, day, week, date) 
                    VALUES ('".$this->Contact_id."', '".$this->Acc_id."', '".$this->time."', '".$this->day."','".$week."', NOW());";
    
        // prepare query
        $stmts = $this->conn->prepare($query2);
                 $this->Acc_id=htmlspecialchars(strip_tags($this->Acc_id));


        // bind values
        $stmt->bindParam(":Contact_id", $this->Contact_id);
        $stmt->bindParam(":Acc_id", $this->Acc_id);
        $stmt->bindParam(":time", $this->time);
        $stmt->bindParam(":day", $this->day);
           
        
       
 
        // execute query
        if($stmts->execute()){
            return true;
        }

        return false;

       
      // }
   
    }
    
}
?>