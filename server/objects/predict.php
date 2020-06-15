<?php
class Predict{
 
    // database connection and table name
    private $conn;
    private $table_name = "call-log";
 
    // object properties
    public $Acc_id;
    public $day;
    public $period;
    public $contact_1;
    public $contact_2;
    public $contact_3;
    public $contact_4;
    public $contact_5;
    public $contact_6;
    public $contact_7;
    public $contact_8;
    public $contact_9;
    public $contact_10;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }
    
    ////////read/////
    function read(){

        // select all query
        $query = "SELECT * from prediction WHERE Acc_id = '".$this->Acc_id."' ";


        // prepare query statement
        $stmt = $this->conn->prepare($query);
        $this->Acc_id=htmlspecialchars(strip_tags($this->Acc_id));

        $stmt->bindParam(":Acc_id", $this->Acc_id);
        // execute query
        $stmt->execute();
        
     
         //while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
       
        //  extract($row);
        //echo $id;
 
        //    }
          return $stmt;
    }
    
    
    
function create(){
        // query to insert record


    $query = "INSERT INTO `prediction` (`day`, `period`, `Acc_id`, `contact_1`, `contact_2`, `contact_3`, `contact_4`,`contact_5`,`contact_6`,`contact_7`,`contact_8`,`contact_9`,`contact_10`)
 VALUES (4, 1, '".$this->Acc_id."', 3, 4,6,1,3,4,1,2,3,4)";
    


        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
       $this->day=htmlspecialchars(strip_tags($this->day));
        $this->period=htmlspecialchars(strip_tags($this->period));
        $this->Acc_id=htmlspecialchars(strip_tags($this->Acc_id));
        $this->contact_1=htmlspecialchars(strip_tags($this->contact_1));
        $this->contact_2=htmlspecialchars(strip_tags($this->contact_2));
        $this->contact_3=htmlspecialchars(strip_tags($this->contact_3));
        $this->contact_4=htmlspecialchars(strip_tags($this->contact_4));
        $this->contact_5=htmlspecialchars(strip_tags($this->contact_5));
        $this->contact_6=htmlspecialchars(strip_tags($this->contact_6));
        $this->contact_7=htmlspecialchars(strip_tags($this->contact_7));
        $this->contact_8=htmlspecialchars(strip_tags($this->contact_8));
        $this->contact_9=htmlspecialchars(strip_tags($this->contact_9));
        $this->contact_10=htmlspecialchars(strip_tags($this->contact_10));
    
        // bind values
        $stm->bindParam(":day", $this->day);
        $stmt->bindParam(":period", $this->period);
        $stmt->bindParam(":Acc_id", $this->Acc_id);
        $stmt->bindParam(":contact_1", $this->contact_1);
        $stmt->bindParam(":contact_2", $this->contact_2);
        $stmt->bindParam(":contact_3", $this->contact_3);
        $stmt->bindParam(":contact_4", $this->contact_4);
        $stmt->bindParam(":contact_5", $this->contact_5);
        $stmt->bindParam(":contact_6", $this->contact_6);
        $stmt->bindParam(":contact_7", $this->contact_7);
        $stmt->bindParam(":contact_8", $this->contact_8);
        $stmt->bindParam(":contact_9", $this->contact_9);
        $stmt->bindParam(":contact_10", $this->contact_10);
 
        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

       
       
       
       
       
       

   
    }
}
?>