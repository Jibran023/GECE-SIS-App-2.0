<?php 
class querieshandler{
    public function legal_input($value) {
        $value = trim($value);
        $value = stripslashes($value);
        $value = htmlspecialchars($value);
        return $value;
    }

    public function LoginQuery($username,$password)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT * FROM `users` where `Email`='$username' and Password=MD5('$password')";
        //echo $query;
        $result=$con->BooleanQuery($query);
        //echo "Executed";
        return $result;
    }
    
    public function LoginQuery2()
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT id, Email, Password, department FROM users";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        //echo "Executed";
        return $result;
    }

    public function FetchFacultyData()
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT FacultyID, Email, Password, Status FROM faculty";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        //echo "Executed";
        return $result;
    }

    public function FetchStudentsData()
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT id, Email, Status, Password FROM studentsinformation";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        //echo "Executed";
        return $result;
    }

    public function getAttendanceData($rollNumber, $semesterDescription, $courseID) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT a.Date, a.AttendanceStatus
        FROM studentmap sm
        INNER JOIN attendance a ON a.StudentMapID = sm.id
        INNER JOIN academicsession ac ON ac.SessionID = sm.SessionID
        WHERE sm.RollNumber = ? AND ac.Description = ? AND sm.CourseID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        $stmt->bind_param("sss", $rollNumber, $semesterDescription, $courseID);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getFacultyCourses($facultyID, $semesterDescription) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT c.CourseID, c.Name, fc.CourseID as FAC
        FROM courses c
        JOIN facultycourses fc ON c.CourseID = fc.CourseID 
        JOIN faculty f ON fc.FacultyID = f.FacultyID
        WHERE f.facultyID = ? AND fc.SessionID = (
            SELECT SessionID 
            FROM academicsession 
            WHERE Description = ?
        )";

        // Prepare the statement
        $stmt = $con->prepare($query);
        $stmt->bind_param("ss", $facultyID, $semesterDescription);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getCoursesStudent($rollNumber, $semesterDescription) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT c.CourseID, c.Name
        FROM courses c
        INNER JOIN studentmap sm ON c.CourseID = sm.CourseID
        WHERE sm.RollNumber = ? AND sm.SessionID = (
            SELECT SessionID 
            FROM academicsession 
            WHERE Description = ?
        )";

        // Prepare the statement
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ss", $rollNumber, $semesterDescription);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function FetchSessions(){
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT Description FROM academicsession
        Where Current = 1";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        //echo "Executed";
        return $result;
    }

    public function studentsrole($id) {
        require_once('Admindp.php');
        $con = new db();

        $query = "SELECT * FROM studentsinformation WHERE id = ?";

        // Prepare the statement
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function ProfileView($id) {
        require_once('Admindp.php');
        $con = new db();

        $query = "SELECT * FROM studentsinformation WHERE id = ?";

        // Prepare the statement
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function addFacultyCourseSection($facultyID, $courseID, $sessionID, $sectionID) {
        require_once('Admindp.php');
        $con = new db();

        // Validate inputs
        if (empty($facultyID) || empty($courseID) || empty($sessionID) || empty($sectionID)) {
            return [
                "status" => "error",
                "message" => "Missing FacultyID, CourseID, SessionID, or SectionID"
            ];
        }

        $query = "INSERT INTO facultycourses (FacultyID, CourseID, SessionID, SectionID) VALUES (?, ?, ?, ?)";
        $stmt = $con->prepare($query);

        if (!$stmt) {
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        $stmt->bind_param("iiii", $facultyID, $courseID, $sessionID, $sectionID);

        if ($stmt->execute()) {
            $response = [
                "status" => "success",
                "message" => "Section added to faculty courses successfully"
            ];
        } else {
            $response = [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }

        $stmt->close();
        $con->closeConnection();

        return $response;
    }

    public function updateStudentSection($sessionID, $courseID, $sectionID, $studentNames) {
        require_once('Admindp.php');
        $con = new db();

        // Decode and sanitize student names
        $studentNames = urldecode($studentNames);
        $studentNamesArray = explode(",", $studentNames);
        $studentNamesArray = array_map([$this, 'legal_input'], $studentNamesArray);

        $query = "
            UPDATE studentmap sm
            INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
            SET sm.SectionID = ?
            WHERE sm.SessionID = ? 
            AND sm.CourseID = ? 
            AND si.Name = ?";

        $stmt = $con->prepare($query);

        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        foreach ($studentNamesArray as $studentName) {
            // Log the parameters and the query being executed
            error_log("Executing query with parameters: SessionID=$sessionID, CourseID=$courseID, SectionID=$sectionID, StudentName=$studentName");

            // Bind parameters
            $stmt->bind_param("iiss", $sectionID, $sessionID, $courseID, $studentName);

            if (!$stmt->execute()) {
                error_log("Execution failed for StudentName: $studentName, Error: " . $stmt->error);
                return [
                    "status" => "error",
                    "message" => "Execution failed for StudentName: $studentName, Error: " . $stmt->error
                ];
            }

            error_log("Successfully updated StudentName: $studentName");
        }

        $stmt->close();
        $con->closeConnection(); // Assuming you have a closeConnection method in your db class

        error_log("Update completed successfully");

        return ["status" => "success"];
    }

    public function insertSection($courseID, $sectionName) {
        require_once('Admindp.php');
        $con = new db();

        // Validate inputs
        if (empty($courseID) || empty($sectionName)) {
            return [
                "status" => "error",
                "message" => "Missing CourseID or SectionName"
            ];
        }

        $query = "INSERT INTO sections (CourseID, SectionName) VALUES (?, ?)";
        $stmt = $con->prepare($query);

        if (!$stmt) {
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        $stmt->bind_param("is", $courseID, $sectionName);

        if ($stmt->execute()) {
            $sectionID = $stmt->insert_id;
            $response = [
                "status" => "success",
                "message" => "Section created successfully",
                "SectionID" => $sectionID
            ];
        } else {
            $response = [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }

        $stmt->close();
        $con->closeConnection();

        return $response;
    }

    public function deleteSection($sectionID) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize input
        $sectionID = $this->legal_input($sectionID);

        // Prepare the SQL statement to delete the section
        $query = "DELETE FROM sections WHERE id = ?";
        $stmt = $con->prepare($query);

        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        // Bind parameters
        $stmt->bind_param("i", $sectionID);

        // Execute the query
        if ($stmt->execute()) {
            // Check if any rows were affected
            if ($stmt->affected_rows > 0) {
                error_log("Delete completed successfully");
                $response = [
                    "status" => "success",
                    "message" => "Section deleted successfully"
                ];
            } else {
                $response = [
                    "status" => "warning",
                    "message" => "No records found to delete"
                ];
            }
        } else {
            error_log("Execution failed: " . $stmt->error);
            $response = [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();

        return $response;
    }

    public function getOfferedCoursesByYearAndRollNumber($year, $rollNumber) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize input
        $year = $this->legal_input($year);
        $rollNumber = $this->legal_input($rollNumber);

        $query = "
            SELECT *, s.id as SectionID 
            FROM offeredcourses sm 
            INNER JOIN facultycourses fc ON sm.CourseID = fc.CourseID AND sm.SessionID = fc.SessionID
            INNER JOIN academicsession a ON a.SessionID = sm.SessionID
            INNER JOIN sections s ON s.id = fc.SectionID
            WHERE sm.Year = ? AND sm.CourseID = ? AND a.Current = 1;
        ";

        // Prepare the statement
        $stmt = $con->prepare($query);

        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        // Bind parameters
        $stmt->bind_param("ss", $year, $rollNumber);

        // Execute the query
        if ($stmt->execute()) {
            $result = $stmt->get_result();

            // Fetch data as an associative array
            $data = array();
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }

            // Close the statement and connection
            $stmt->close();
            $con->closeConnection();

            return [
                "status" => "success",
                "data" => $data
            ];
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }
    }

    public function getCoursesStudent($rollNumber) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize input
        $rollNumber = $this->legal_input($rollNumber);

        $query = "
            SELECT c.CourseID, c.Name, f.FacultyName, f.FacultyID, a.SessionID, a.Description  
            FROM offeredcourses oc
            INNER JOIN courses c ON c.CourseID = oc.CourseID
            INNER JOIN facultycourses fc ON fc.CourseID = oc.CourseID
            INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
            INNER JOIN academicsession a ON a.SessionID = oc.SessionID AND a.SessionID = fc.SessionID
            WHERE oc.Year = ? AND a.Current = 1;
        ";

        // Prepare the statement
        $stmt = $con->prepare($query);

        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        // Bind parameters
        $stmt->bind_param("i", $rollNumber);

        // Execute the query
        if ($stmt->execute()) {
            $result = $stmt->get_result();

            // Fetch data as an associative array
            $data = array();
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }

            // Close the statement and connection
            $stmt->close();
            $con->closeConnection();

            return [
                "status" => "success",
                "data" => $data
            ];
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }
    }

    public function getStudentMapData($year, $rollNumber) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize input
        $year = $this->legal_input($year);
        $rollNumber = $this->legal_input($rollNumber);

        $query = "
            SELECT * 
            FROM studentmap sm
            INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
            WHERE sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID IS NULL AND si.Status = 'Active';
        ";

        // Prepare the statement
        $stmt = $con->prepare($query);

        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        // Bind parameters
        $stmt->bind_param("ss", $year, $rollNumber);

        // Execute the query
        if ($stmt->execute()) {
            $result = $stmt->get_result();

            // Fetch data as an associative array
            $data = array();
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }

            // Close the statement and connection
            $stmt->close();
            $con->closeConnection();

            return [
                "status" => "success",
                "data" => $data
            ];
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->closeConnection();
            return [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }
    }

    public function getStudentData($year, $rollNumber, $id) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize inputs
        $year = $this->legal_input($year);
        $rollNumber = $this->legal_input($rollNumber);
        $id = $this->legal_input($id);

        // Prepare the SQL statement
        $query = "
        SELECT * 
        FROM studentmap sm
        INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
        WHERE sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID = ? AND si.Status = 'Active'";

        $stmt = $con->prepare($query);
        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        $stmt->bind_param("sss", $year, $rollNumber, $id);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = [];
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->closeConnection();

        return $data;
    }

    public function deleteFacultyCourse($facultyID, $courseID, $sessionID, $sectionID) {
        require_once('Admindp.php');
        $con = new db();

        // Validate inputs
        if (empty($facultyID) || empty($courseID) || empty($sessionID) || empty($sectionID)) {
            return [
                "status" => "error",
                "message" => "Missing FacultyID, CourseID, SessionID, or SectionID"
            ];
        }

        // Prepare the SQL statement to delete a record
        $query = "DELETE FROM facultycourses 
                  WHERE FacultyID = ? AND CourseID = ? AND SessionID = ? AND SectionID = ?";
        $stmt = $con->prepare($query);

        if (!$stmt) {
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }

        // Bind parameters
        $stmt->bind_param("ssss", $facultyID, $courseID, $sessionID, $sectionID);

        // Execute the query
        if ($stmt->execute()) {
            if ($stmt->affected_rows > 0) {
                $response = [
                    "status" => "success",
                    "message" => "Record deleted successfully"
                ];
            } else {
                $response = [
                    "status" => "warning",
                    "message" => "No records found to delete"
                ];
            }
        } else {
            $response = [
                "status" => "error",
                "message" => "Execution failed: " . $stmt->error
            ];
        }

        $stmt->close();
        $con->closeConnection();

        return $response;
    }

    
    
    public function ReturnArray($username,$password)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT * FROM `users` where `Email`='$username' and Password=MD5('$password')";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchArray($query);
       // echo "Executed";
        return $result;

    }
    public function FacultyLogin($username,$password)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT * FROM `FacultyLogin` where `Email`='$username' and Password=MD5('$password')";
        //echo $query;
        $result=$con->BooleanQuery($query);
        //echo "Executed";
        return $result;
    }
    public function FacultyReturnArray($username,$password)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT * FROM `users` where `Email`='$username' and Password=MD5('$password')";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchArray($query);
       // echo "Executed";
        return $result;

    }
    public function GetCGPA($RollNumber,$cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT ROUND(SUM(GPA)/COUNT(GPA),2) AS GPA FROM (SELECT *,(newtable.Internal+newtable.External)as Total,CASE WHEN newtable.SessionID<=6 THEN (select LG.Grade From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession<='6') ELSE (select LG.Grade From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession>'6') END as `Grade`,
        CASE WHEN newtable.SessionID<=6 THEN (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession<='6') ELSE (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession>'6') END as `GPA`,
        CASE WHEN newtable.SessionID<=6 
        THEN 
        CASE WHEN (newtable.Internal<18 OR newtable.External<27) THEN 'FAILS' ELSE 'PASSES' END
        ELSE CASE WHEN newtable.SessionID>6 THEN CASE WHEN (newtable.Internal<20 OR newtable.External<30) THEN 'FAILS' ELSE 'PASSES' END ELSE 'PASSES' END
        END AS Status FROM(SELECT *,(SELECT R.Internal From Results R WHERE R.CourseID=dt.CourseID AND R.RollNumber=$RollNumber ORDER BY SessionID DESC LIMIT 1) AS Internal,(SELECT R.External From Results R WHERE R.CourseID=dt.CourseID AND R.RollNumber=$RollNumber ORDER BY SessionID DESC LIMIT 1) AS External FROM (Select OC.CourseID,OC.Semester,OC.Year,OC.SessionID,ACD.Description,C.CCOld,C.CCNew,C.Name,C.ShortCode From OfferedCourses OC INNER JOIN Courses C ON C.CourseID=OC.CourseID INNER JOIN AcademicSession ACD ON ACD.SessionID=OC.SessionID)dt WHERE dt.Year=$cohort)newtable  WHERE Internal IS NOT NULL)Fdt;";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchArray($query);
       // echo "Executed";
        return $result;

    }
    public function GetCohorts(){
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT DISTINCT cohort FROM `StudentsInformation` ORDER BY cohort ASC;";
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        return $result;
    } 
    public function GetStudentsByCohort($cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT DISTINCT RollNumber,Name FROM `StudentsInformation` WHERE cohort='$cohort' ORDER BY RollNumber ASC;";
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        return $result;
    }//AppliedHistory
    public function AppliedHistory($Email)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT @a:=@a+1 `SNO`,DATE_FORMAT(`EntryDate`,'%d-%M-%Y %l:%i %p') as `DoA`,`id`,`Name`,`Department`, `Complaint`,`Status`, `ResolveDate` FROM `complaints`,(SELECT @a:=0) as `SNO` WHere `Email`='$Email'";
        $result=$con->SelectQueryExecutorFetchAssoc($query);
        return $result;
    }
    public function StudentBasicInformation($RollNumber)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="select * From StudentsInformation WHere RollNumber='$RollNumber'";
        $result=$con->SelectQueryExecutorFetchArray($query);
        return $result;
    }
    public function GetStudentsAcademicSessions($RollNumber)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="Select DISTINCT RES.SessionID,ACD.Description From Results RES INNER JOIN AcademicSession ACD On ACD.SessionID=RES.SessionID WHERE RES.RollNumber='$RollNumber';";
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function FetchResults($RollNumber,$SessionID,$Condition)
    {
        require_once('Admindp.php');
        $con=new db();
        //$query="SELECT RES.RollNumber,CO.CCOld,CO.CCNew,CO.Name,RES.Internal,RES.External,(RES.Internal+RES.External) as TotalMarks,SM.SessionID,(select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition) as `Grade`,(select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition)as `GPA` From Results as RES RIGHT JOIN StudentMap as SM ON SM.CourseID=RES.CourseID AND SM.RollNumber=RES.RollNumber INNER JOIN Courses as CO ON CO.CourseID=SM.CourseID Where RES.RollNumber='$RollNumber' AND SM.SessionID='$SessionID';";
        $query="SELECT RES.RollNumber,Cou.id,Cou.CCOld, Cou.CCNew,Cou.Name,RES.Internal,RES.External, (RES.Internal+RES.External) TotalMarks, (select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition) as `Grade`,(select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition)as `GPA` FROM Results RES INNER JOIN Courses Cou ON Cou.CourseID=RES.CourseID WHERE RES.RollNumber='$RollNumber' AND RES.SessionID='$SessionID';";
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function FetchResultsSemesterWise($RollNumber,$Semester,$cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        //$query="SELECT RES.RollNumber,CO.CCOld,CO.CCNew,CO.Name,RES.Internal,RES.External,(RES.Internal+RES.External) as TotalMarks,SM.SessionID,(select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition) as `Grade`,(select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition)as `GPA` From Results as RES RIGHT JOIN StudentMap as SM ON SM.CourseID=RES.CourseID AND SM.RollNumber=RES.RollNumber INNER JOIN Courses as CO ON CO.CourseID=SM.CourseID Where RES.RollNumber='$RollNumber' AND SM.SessionID='$SessionID';";
        $query="SELECT *,(newtable.Internal+newtable.External)as Total,CASE WHEN newtable.SessionID<=6 THEN (select LG.Grade From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession<='6') ELSE (select LG.Grade From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession>'6') END as `Grade`,
        CASE WHEN newtable.SessionID<=6 THEN (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession<='6') ELSE (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession>'6') END as `GPA`,
        CASE WHEN newtable.SessionID<=6 
        THEN 
        CASE WHEN (newtable.Internal<18 OR newtable.External<27) THEN 'FAILS' ELSE 'PASSES' END
        ELSE CASE WHEN newtable.SessionID>6 THEN CASE WHEN (newtable.Internal<20 OR newtable.External<30) THEN 'FAILS' ELSE 'PASSES' END ELSE 'PASSES' END
        END AS Status FROM(SELECT *,(SELECT R.Internal From Results R WHERE R.CourseID=dt.CourseID AND R.RollNumber=$RollNumber ORDER BY SessionID DESC LIMIT 1) AS Internal,(SELECT R.External From Results R WHERE R.CourseID=dt.CourseID AND R.RollNumber=$RollNumber ORDER BY SessionID DESC LIMIT 1) AS External FROM (Select OC.CourseID,OC.Semester,OC.Year,OC.SessionID,ACD.Description,C.CCOld,C.CCNew,C.Name,C.ShortCode From OfferedCourses OC INNER JOIN Courses C ON C.CourseID=OC.CourseID INNER JOIN AcademicSession ACD ON ACD.SessionID=OC.SessionID)dt WHERE dt.Semester=$Semester AND dt.Year=$cohort)newtable  WHERE Internal IS NOT NULL;";
       // print_r($query);
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function FetchCGPASemesterANDStudentWise($RollNumber,$Semester,$cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        //$query="SELECT RES.RollNumber,CO.CCOld,CO.CCNew,CO.Name,RES.Internal,RES.External,(RES.Internal+RES.External) as TotalMarks,SM.SessionID,(select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition) as `Grade`,(select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession$Condition)as `GPA` From Results as RES RIGHT JOIN StudentMap as SM ON SM.CourseID=RES.CourseID AND SM.RollNumber=RES.RollNumber INNER JOIN Courses as CO ON CO.CourseID=SM.CourseID Where RES.RollNumber='$RollNumber' AND SM.SessionID='$SessionID';";
        $query="SELECT ndt.Description,Count(*)*3 as TotalCreditHours,sum(CASE WHEN `Status`='Passes' then 3 else 0 end) as CreditHours, ROUND(SUM(GPA)/Count(*),2) as `CGPA`  FROM(SELECT *,(newtable.Internal+newtable.External)as Total,CASE WHEN newtable.SessionID<=6 THEN (select LG.Grade From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession<='6') ELSE (select LG.Grade From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession>'6') END as `Grade`,
        CASE WHEN newtable.SessionID<=6 THEN (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession<='6') ELSE (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(newtable.Internal+newtable.External) AND LG.Applicablesession>'6') END as `GPA`,
        CASE WHEN newtable.CourseID=50
        THEN CASE WHEN (newtable.Internal + newtable.External<50) THEN 'FAILS' ELSE 'PASSES' END
        ELSE
        CASE WHEN newtable.SessionID<=6 
        THEN 
        CASE WHEN (newtable.Internal<18 OR newtable.External<27) THEN 'FAILS' ELSE 'PASSES' END
        ELSE CASE WHEN newtable.SessionID>6 THEN CASE WHEN (newtable.Internal<20 OR newtable.External<30) THEN 'FAILS' ELSE 'PASSES' END ELSE 'PASSES' END
        END END AS `Status` FROM(SELECT *,(SELECT R.Internal From Results R WHERE R.CourseID=dt.CourseID AND R.RollNumber=$RollNumber ORDER BY SessionID DESC LIMIT 1) AS Internal,(SELECT R.External From Results R WHERE R.CourseID=dt.CourseID AND R.RollNumber=$RollNumber ORDER BY SessionID DESC LIMIT 1) AS External FROM (Select OC.CourseID,OC.Semester,OC.Year,OC.SessionID,ACD.Description,C.CCOld,C.CCNew,C.Name,C.ShortCode From OfferedCourses OC INNER JOIN Courses C ON C.CourseID=OC.CourseID INNER JOIN AcademicSession ACD ON ACD.SessionID=OC.SessionID)dt WHERE dt.Semester=$Semester AND dt.Year=$cohort)newtable  WHERE Internal IS NOT NULL)ndt Group By ndt.Description;";
       // print_r($query);
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function AcademicSessionWiseData($RollNumber)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT RES.RollNumber,CO.CourseID,CO.CCOld,CO.CCNew,CO.Name,RES.Internal,RES.External, (RES.Internal+RES.External) TotalMarks,CASE WHEN RES.SessionID<=6 THEN (select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession<='6') ELSE (select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession>'6') END as `Grade`,
        CASE WHEN RES.SessionID<=6 THEN (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession<='6') ELSE (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession>'6') END as `GPA`, ACD.Description FROM Results RES INNER JOIn AcademicSession ACD On ACD.SessionID=RES.SessionID INNER JOIn Courses CO On RES.CourseID=CO.CourseID WHERE RES.RollNumber='$RollNumber' ORDER BY ACD.SessionID ASC;";
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function GetSemesterCohortwise($cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="select DISTINCT Semester From OfferedCourses WHere Year=$cohort Order By Semester ASC;";
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function GetStudentsAcademicSessionsCohortwise($cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="Select DISTINCT RES.SessionID,ACD.Description From Results RES INNER JOIN AcademicSession ACD On ACD.SessionID=RES.SessionID INNER JOIN StudentsInformation SI ON SI.RollNumber=RES.RollNumber WHERE SI.cohort='$cohort' ORDER BY RES.SessionID ASC;";
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function GetStudentsSemestersCohortwise($cohort)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT DISTINCT Semester FROM OfferedCourses WHERE Year=$cohort;";
        //print_r($query);
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function GetCourseCountByCohort($cohort,$SessionID)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT * FROM `OfferedCourses` OC WHERE OC.Year='$cohort' AND OC.SessionID='$SessionID';";
        $result=$con->NumberOfRow($query);
        return $result;
    }
    public function GetCoursesByCohort($cohort,$SessionID)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT OC.CourseID,CO.CCNew,CO.ShortCode FROM `OfferedCourses` OC INNER JOIN Courses CO ON CO.CourseID=OC.CourseID  WHERE OC.Year='$cohort' AND OC.SessionID='$SessionID';";
        //print_r($query);
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
    public function GetResultsByCourse($RollNumber,$CourseID)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="Select RES.RollNumber, SI.Name, RES.CourseID ,RES.Internal,RES.External,(RES.Internal+RES.External)as Total,
        CASE WHEN RES.SessionID<=6 THEN (select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession<='6') ELSE (select LG.Grade From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession>'6') END as `Grade`,
        CASE WHEN RES.SessionID<=6 THEN (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession<='6') ELSE (select LG.GradePoint From LUGPA as LG WHERE LG.Marks=(RES.Internal+RES.External) AND LG.Applicablesession>'6') END as `GPA`,
        CASE WHEN RES.SessionID<=6 
        THEN 
        CASE WHEN (RES.Internal<18 OR RES.External<27) THEN 'FAILS' ELSE 'PASSES' END
        ELSE CASE WHEN RES.SessionID>6 THEN CASE WHEN (RES.Internal<20 OR RES.External<30) THEN 'FAILS' ELSE 'PASSES' END ELSE 'PASSES' END
        END AS Status From Results RES INNER JOIN StudentsInformation SI ON RES.RollNumber=SI.RollNumber WHERE RES.RollNumber='$RollNumber' AND RES.CourseID='$CourseID' ORDER BY RES.SessionID DESC LIMIT 1;";
        //print_r($query);
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        if($result!=null){
            return $result;
        }else{
            return 0;
            }
        //print_r($result);
        //return $result;
    }
    public function GetStudentsBycohorts($cohort,$Status)
    {
        require_once('Admindp.php');
        $con=new db();
        $query="Select SI.RollNumber,SI.Name,SI.cohort,SI.Status FROM StudentsInformation SI WHERE SI.cohort='$cohort' AND Status='$Status' ORDER BY SI.Name;";
        //print_r($query);
        $result=$con->SelectQueryExecutorFetchAssocNE($query);
        return $result;
    }
}


?>