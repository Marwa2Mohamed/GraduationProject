<?php
class account{
 
    // database connection and table name
    private $conn;
    private $table_name = "account";
 
    // object properties
      public $Acc_id;
      public $Google_acc;
      public $last_updated;

    
    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }
    // read jobs
    function read(){

        // select all query
        $query = "SELECT * from account ";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();
        
     
         //while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
       
        //  extract($row);
        //echo $id;
 
        //    }
          return $stmt;
    }
    
    // create job
    function create(){

        // query to insert record
        $query = "INSERT INTO
                    " . $this->table_name . "
                SET
                    Call_id=:Call_id, Contact_id=:Contact_id, Acc_id=:Acc_id, Time=:Time, day=:day, week=:week";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->Call_id=htmlspecialchars(strip_tags($this->Call_id));
        $this->Contact_id=htmlspecialchars(strip_tags($this->Contact_id));
        $this->Acc_id=htmlspecialchars(strip_tags($this->Acc_id));
        $this->Time=htmlspecialchars(strip_tags($this->Time));
        $this->day=htmlspecialchars(strip_tags($this->day));
        $this->week=htmlspecialchars(strip_tags($this->week));

        // bind values
        $stmt->bindParam(":Call_id", $this->Call_id);
        $stmt->bindParam(":Contact_id", $this->Contact_id);
        $stmt->bindParam(":Acc_id", $this->Acc_id);
        $stmt->bindParam(":Time", $this->Time);
        $stmt->bindParam(":day", $this->day);
        $stmt->bindParam(":week", $this->week);   
        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
    
    // used when filling up the update job form
    function readOne(){

        // query to read single record
        $query = "SELECT
                   *
                FROM
                    " . $this->table_name . "
                WHERE Call_id =? 
                LIMIT
                    0,1";

        // prepare query statement
        $stmt = $this->conn->prepare( $query );

        // bind id of job to be updated
        $stmt->bindParam(1, $this->id);

        // execute query
        $stmt->execute();

        // get retrieved row
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        // set values to object properties
        $this->Call_id = $row['Call_id'];
        $this->Contact_id = $row['Contact_id'];
        $this->Acc_id = $row['Acc_id'];
        $this->Time = $row['Time'];
        $this->day = $row['day'];
        $this->week = $row['week'];
    }
    
    // update the job
    function update(){

        // update query
        $query = "UPDATE
                    " . $this->table_name . "
                SET
                    Fname=:Fname, Minit=:Minit, Lname=:Lname, Ssn=:Ssn, Salary=:Salary,post_date=:post_date,expire_date=:expire_date, employment_type=:employment_type
                WHERE
                    id = :id";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->Fname=htmlspecialchars(strip_tags($this->Fname));
        $this->Minit=htmlspecialchars(strip_tags($this->Minit));
        $this->Lname=htmlspecialchars(strip_tags($this->Lname));    
        $this->Ssn=htmlspecialchars(strip_tags($this->Ssn));
        $this->Salary=htmlspecialchars(strip_tags($this->Salary));
        $this->employment_type=htmlspecialchars(strip_tags($this->employment_type));
        $this->id=htmlspecialchars(strip_tags($this->id));
        $this->post_date = $this->post_date;
        $this->expire_date = $this->expire_date;
        
        // bind new values
        $stmt->bindParam(':Fname', $this->Fname);
        $stmt->bindParam(':Minit', $this->Minit);
        $stmt->bindParam(':Lname', $this->Lname);    
        $stmt->bindParam(':Ssn', $this->Ssn);
        $stmt->bindParam(':Salary', $this->Salary);
        $stmt->bindParam(':employment_type', $this->employment_type);
        $stmt->bindParam(':id', $this->id);
        $stmt->bindParam(':post_date', $this->post_date);
        $stmt->bindParam(':expire_date', $this->expire_date);
        // execute the query
        if($stmt->execute()){
            return true;
        }

        return false;
    }
    
    // delete the job
    function delete(){

        // delete query
        $query = "DELETE FROM employee WHERE Ssn = ?";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->Ssn=htmlspecialchars(strip_tags($this->Ssn));

        // bind id of record to delete
        $stmt->bindParam(1, $this->Ssn);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }
    
    // search jobs
    function search($keywords){

        // select all query
        $query = "CALL Ass(12345)";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $keywords=htmlspecialchars(strip_tags($keywords));
        $keywords = "%{$keywords}%";

        // bind
        $stmt->bindParam(1, $keywords);
        $stmt->bindParam(2, $keywords);
        

        // execute query
        $stmt->execute();

        return $stmt;
    }
    
    // read jobs with pagination
    public function readPaging($from_record_num, $records_per_page){

        // select query
        $query = "SELECT
                    c.name as category_name, p.id, p.name, p.description, p.price, p.category_id, p.created
                FROM
                    " . $this->table_name . " p
                    LEFT JOIN
                        categories c
                            ON p.category_id = c.id
                ORDER BY p.created DESC
                LIMIT ?, ?";

        // prepare query statement
        $stmt = $this->conn->prepare( $query );

        // bind variable values
        $stmt->bindParam(1, $from_record_num, PDO::PARAM_INT);
        $stmt->bindParam(2, $records_per_page, PDO::PARAM_INT);

        // execute query
        $stmt->execute();

        // return values from database
        return $stmt;
    }
    // used for paging products
    public function countr(){
        $query = "SELECT COUNT(*) as total_rows FROM " . $this->table_name . "";

        $stmt = $this->conn->prepare( $query );
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);

        return $row['total_rows'];
    }
}
?>