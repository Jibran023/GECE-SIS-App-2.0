<?php
class db {

    protected $connection;
	protected $query;
    protected $show_errors = TRUE;
    protected $query_closed = TRUE;
	public $query_count = 0;

	public function __construct($dbhost = '127.0.0.1:3306', $dbuser = 'root', $dbpass = 'mazerunner', $dbname = 'gecesisapp', $charset = 'utf8') {
        $this->connection=new mysqli($dbhost,$dbuser,$dbpass,$dbname);
        //$this->connection = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
       // echo '<script>alert("'.$this->connection->connect_errno.'")</script>';
		if ($this->connection->connect_error) {
			$this->error('Failed to connect to MySQL - ' . $this->connection->connect_error);
		}
		$this->connection->set_charset($charset);
	}

    public function prepare($query) {
        return $this->connection->prepare($query);
    }

    public function close2() {
        $this->connection->close();
    }

    public function BooleanQuery($query){
        if($result = $this->connection->query($query)){
            //echo "Successfully Executed";
            //echo $result->num_rows;
            if($result->num_rows >= 1){
                return 1;
            }
            else{
                return 0;    
            }
        }
    }
    public function SelectQueryExecutorFetchArray($query){
        //$rows=array();
        return mysqli_fetch_array($this->connection->query($query));
    }
    public function SelectQueryExecutorFetchAssoc($query){
        // $row=mysqli_fetch_assoc($this->connection->query($query));
        $row=mysqli_query($this->connection,$query);
        //$row=mysqli_fetch_array($this->connection->query($query));
         $responses=array();
         while($r=mysqli_fetch_assoc($row))
         {
             $responses[]=$r;
         }
        //var_dump(json_encode($responses));
        $result=json_encode($responses);
        return $result;
     }

     public function SelectQueryExecutorFetchAssoc2($query) {
        $row = mysqli_query($this->connection, $query);
        $responses = array();
        while ($r = mysqli_fetch_assoc($row)) {
            $responses[] = $r;
        }
        return $responses; // Return the array directly
    }
     public function SelectQueryExecutorFetchAssocNE($query){
        // $row=mysqli_fetch_assoc($this->connection->query($query));
        $row=mysqli_query($this->connection,$query);
        //$row=mysqli_fetch_array($this->connection->query($query));
         $responses=array();
         while($r=mysqli_fetch_assoc($row))
         {
             $responses[]=$r;
         }
        //var_dump(json_encode($responses));
        return $responses;
     }
    public function InsertQueryExecutor($query){
        $insertMsg=$this->connection->query($query);
        if($insertMsg){
            return 1;
        }
        else{
            return 0;
        }
    }

    public function begin_transaction() {
        if (!$this->connection->begin_transaction()) {
            $this->debug_log("Failed to begin transaction: " . $this->conn->error);
            return false;
        }
        return true;
    }

    public function rollback() {
        if (!$this->connection->rollback()) {
            $this->debug_log("Failed to rollback transaction: " . $this->conn->error);
            return false;
        }
        return true;
    }

    public function commit() {
        if (!$this->connection->commit()) {
            $this->debug_log("Failed to commit transaction: " . $this->conn->error);
            return false;
        }
        return true;
    }

    public function TransactionQueryExecutor($array){
        mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
        $mysqliConnection=($this->connection);
        mysqli_begin_transaction($mysqliConnection);
        try{
            foreach($array as $query)
            {      
                mysqli_query($mysqliConnection,$query);
            }
            mysqli_commit($mysqliConnection);
            return true;
        } 
        catch(mysqli_sql_exception $exception)
            {
                mysqli_rollback($mysqliConnection);
                throw $exception;
                return false;
            }

    }
	public function close() {
		return $this->connection->close();
	}

    public function numRows() {
		$this->query->store_result();
		return $this->query->num_rows;
	}

	public function affectedRows() {
		return $this->query->affected_rows;
	}

    public function lastInsertID() {
    	return $this->connection->insert_id;
    }

    public function error($error) {
        if ($this->show_errors) {
            exit($error);
        }
    }

	private function _gettype($var) {
	    if (is_string($var)) return 's';
	    if (is_float($var)) return 'd';
	    if (is_int($var)) return 'i';
	    return 'b';
	}
    public function NumberOfRow($query){
        $result=mysqli_query($this->connection,$query);
        return mysqli_num_rows($result);
    }

}
?>