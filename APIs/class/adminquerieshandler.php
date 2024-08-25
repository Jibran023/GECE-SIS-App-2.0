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
        $con->close();  // Assuming you have a closeConnection method in your db class

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
        $con->close();  // Assuming you have a closeConnection method in your db class

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
        $stmt = $con->prepare($query);
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
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function FetchSessions(){
        require_once('Admindp.php');
        $con=new db();
        $query="SELECT Description FROM academicsession
        Where Current = 1";
        //echo $query;
        $result=$con->SelectQueryExecutorFetchAssoc2($query);
        //echo "Executed";
        return $result;
    }

    public function studentsrole($id) {
        require_once('Admindp.php');
        $con = new db();

        $query = "SELECT * FROM studentsinformation WHERE id = ?";
        
        // Prepare the statement
        $stmt = $con->prepare($query);
        $stmt->bind_param("i", $id);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function ProfileView($id) {
        require_once('Admindp.php');
        $con = new db();

        $query = "SELECT * FROM studentsinformation WHERE id = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
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
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getCurrentAcademicYears() {
        require_once('Admindp.php');
        $con = new db();

        $query = "SELECT DISTINCT oc.Year 
                  FROM offeredcourses oc
                  INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
                  WHERE a.Current = 1";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Create an array to hold the results
        $data = array();

        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getRollNumbersAndSectionID($courseName, $sessionDescription, $sectionName) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
        
        $query = "SELECT sm.RollNumber, s.Id as SectionID
                  FROM studentmap sm
                  INNER JOIN offeredcourses oc ON sm.CourseID = oc.CourseID
                  INNER JOIN sections s ON sm.SectionID = s.Id
                  INNER JOIN academicsession a ON oc.SessionID = a.SessionID
                  INNER JOIN courses c ON c.CourseID = oc.CourseID
                  WHERE c.Name = ? AND a.Description = ? AND s.SectionName = ?";
        
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            debug_log("Failed to prepare SQL statement: " . $con->error);
            return ["status" => "error", "message" => "Failed to prepare SQL statement"];
        }

        $stmt->bind_param("sss", $courseName, $sessionDescription, $sectionName);
        $stmt->execute();
        $stmt->bind_result($rollNumber, $sectionID);

        $rollNumbers = [];
        $sectionIDs = [];
        while ($stmt->fetch()) {
            $rollNumbers[] = $rollNumber;
            $sectionIDs[] = $sectionID;
            debug_log("RollNumber found: $rollNumber, SectionID found: $sectionID");
        }

        $stmt->close();
        $con->close();

        return ["success" => true, "rollNumbers" => $rollNumbers, "sectionID" => $sectionIDs];
    }
    // Debug logging function
    private function debug_log($message) {
        file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
    }

    public function updateReadTime($MsgIDs, $ReadTime) {
        require_once('Admindp.php');
        $con = new db();

        // Convert MsgIDs to an array
        $msgIdsArray = explode(',', $MsgIDs);

        // Prepare the SQL statement dynamically based on the number of MsgIDs
        $placeholders = implode(',', array_fill(0, count($msgIdsArray), '?'));
        $query = "UPDATE sendnotifications 
                  SET ReadTime = ? 
                  WHERE MsgID IN ($placeholders)";

        // Prepare the statement
        $stmt = $con->prepare($query);

        // Check if preparation was successful
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind parameters dynamically
        $params = array_merge([$ReadTime], $msgIdsArray);
        $params_types = str_repeat('s', count($params));
        $stmt->bind_param($params_types, ...$params);

        // Execute the statement
        $result = $stmt->execute();

        // Check if the update was successful
        if ($result) {
            $response = ["status" => "success"];
        } else {
            $response = ["status" => "error", "message" => $stmt->error];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();

        return $response;
    }

    public function getFacultyNotifications($facultyName) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT si.Name, si.department 
        FROM users si
        INNER JOIN sendnotifications sn ON si.id = sn.userID
        WHERE sn.userID = ?";

        $stmt = $con->prepare($query);
        if ($stmt === false) {
            $this->debug_log("Failed to prepare SQL statement: " . $con->error);
            return ["status" => "error", "message" => "Failed to prepare SQL statement"];
        }

        $stmt->bind_param("s", $facultyName);
        $stmt->execute();
        $result = $stmt->get_result();

        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        $stmt->close();
        $con->close();

        return $data;
    }

    public function getStudentNotifications($facultyName) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT Name, cohort, RollNumber 
        FROM studentsinformation si
        INNER JOIN sendnotifications sn ON si.id = sn.ReceiverID
        WHERE sn.ReceiverID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        $stmt->bind_param("s", $facultyName);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function fetchNotifications($facultyName, $userID) {
        require_once('Admindp.php');
        $con = new db();

        $query = "SELECT * FROM sendnotifications WHERE ReceiverID = ? AND userID = ? Order by PostedDateTime DESC";
        
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind the parameters
        $stmt->bind_param("ss", $facultyName, $userID);

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function fetchNotificationsByFaculty($facultyName) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT * 
        FROM sendnotifications
        WHERE userID = ?
        GROUP BY ReceiverID
        ORDER BY PostedDateTime DESC";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => "Failed to prepare SQL statement: " . $con->error];
        }

        // Bind the parameters
        $stmt->bind_param("s", $facultyName);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getNotificationsByFacultyName($FacultyName) {
        require_once('Admindp.php');
        $con = new db();
    
        $query = "SELECT * FROM sendnotifications WHERE ReceiverID = ? Order by PostedDateTime DESC";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
    
        // Bind the parameter
        $stmt->bind_param("s", $FacultyName);
    
        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        return $data;
    }

    public function submitAnnouncement($userID, $title, $content, $postedDateTime, $facultyNames) {
        require_once('Admindp.php');
        $con = new db();

        // Function to get FacultyID by FacultyName
        function getFacultyID($facultyName, $con) {
            $sql = "SELECT FacultyID FROM faculty WHERE FacultyName = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("s", $facultyName);
            $stmt->execute();
            $stmt->bind_result($facultyID);
            $stmt->fetch();
            $stmt->close();
            return $facultyID;
        }

        // Insert the announcement into the database
        $sql = "INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo) VALUES (?, ?, ?, ?, 'Faculty')";
        $stmt = $con->prepare($sql);
        $stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);

        if ($stmt->execute()) {
            // Get the ID of the inserted announcement
            $announcementID = $stmt->insert_id;

            // Insert each faculty member as a recipient
            $recipientSql = "INSERT INTO announcementbyro_facultyrecipients (AnnouncementID, FacultyID) VALUES (?, ?)";
            $recipientStmt = $con->prepare($recipientSql);

            foreach ($facultyNames as $facultyName) {
                if (!empty($facultyName)) { // Ensure facultyName is not empty
                    $facultyID = getFacultyID($facultyName, $con);
                    if ($facultyID !== null) { // Ensure facultyID was found
                        $recipientStmt->bind_param("ii", $announcementID, $facultyID);
                        $recipientStmt->execute();
                    } else {
                        // Optionally handle case where FacultyName is not found
                        return ["success" => false, "message" => "Faculty name not found: $facultyName"];
                    }
                }
            }

            $stmt->close();
            $recipientStmt->close();
            $con->close();

            return ["success" => true, "message" => "Announcement submitted successfully"];
        } else {
            $stmt->close();
            $con->close();
            return ["success" => false, "message" => "Failed to submit announcement"];
        }
    }

    public function addAnnouncementRecipients2($announcementID, $facultyNames, $ID) {
        require_once('Admindp.php');
        $con = new db();

        
        
        // Sanitize the input
        $announcementID = $this->legal_input($announcementID);
        $facultyNames = array_map([$this, 'legal_input'], $facultyNames); // Sanitize each faculty name
        $ID = $this->legal_input($ID);
        
        // Prepare the SQL statement for inserting recipients
        $recipientSql = "INSERT INTO announcementbyro_userrecipients (AnnouncementID, UserID) VALUES (?, ?)";
        $recipientStmt = $con->prepare($recipientSql);

        // Check for errors in statement preparation
        if ($recipientStmt === false) {
            return ["success" => false, "message" => "Failed to prepare SQL statement for inserting recipients"];
        }

        $debugOutput = [];

        // Loop through each faculty name to add recipients
        foreach ($facultyNames as $facultyName) {
            if (!empty($facultyName)) { // Ensure facultyName is not empty
                $facultyID = $this->getFacultyIDx($facultyName, $con, $ID);
                if ($facultyID !== null) { // Ensure facultyID was found
                    $recipientStmt->bind_param("ii", $announcementID, $facultyID);
                    $recipientStmt->execute();
                } else {
                    // Handle case where FacultyName is not found
                    $debugOutput[] = ["success" => false, "message" => "Faculty name not found: $facultyName"];
                    $recipientStmt->close();
                    $con->close(); // Close the connection
                    return $debugOutput;
                }
            }
        }

        // Close the statement and connection
        $recipientStmt->close();
        $con->close();

        $debugOutput[] = ["success" => true, "message" => "Recipients added successfully"];
        return $debugOutput;
    }

    public function getFacultyIDx($facultyName, $con, $ID) {
        $sql = "SELECT id FROM users WHERE Name = ? AND id != ?";
        $stmt = $con->prepare($sql);
        if ($stmt === false) {
            return null;
        }

        $stmt->bind_param("si", $facultyName, $ID);
        $stmt->execute();
        $stmt->bind_result($facultyID);
        $stmt->fetch();
        $stmt->close();

        return $facultyID;
    }

    public function insertAnnouncementRecipients2($announcementID, $rollNumbers) {
        require_once('Admindp.php');
        $con = new db();

        $query = "INSERT INTO announcementbyro_studentrecipients (AnnouncementID, RollNumber) VALUES (?, ?)";
        $stmt = $con->prepare($query);

        if ($stmt === false) {
            return ["success" => false, "message" => "Failed to prepare SQL statement: " . $con->error];
        }

        foreach ($rollNumbers as $rollNumber) {
            if (!empty($rollNumber)) {
                $stmt->bind_param("ii", $announcementID, $rollNumber);
                if (!$stmt->execute()) {
                    $stmt->close();
                    $con->close();
                    return ["success" => false, "message" => "Failed to insert recipient with RollNumber: $rollNumber"];
                }
            }
        }

        $stmt->close();
        $con->close();

        return ["success" => true, "message" => "Recipients added successfully"];
    }

    public function fetchUsersExcludingId($ID) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize the input
        $ID = $this->legal_input($ID);

        $query = "SELECT * FROM users WHERE id != ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        $stmt->bind_param("i", $ID);
        $stmt->execute();
        $result = $stmt->get_result();

        // Create an array to hold the results
        $data = array();

        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function fetchStudentNamesByCohorts($cohorts) {
        require_once('Admindp.php');
        $con = new db();
    
        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
    
        // Prepare SQL query
        if (in_array("All", $cohorts)) {
            $query = "SELECT Name FROM studentsinformation WHERE Status = 'Active'";
            $stmt = $con->prepare($query);
        } else {
            $cohortsPlaceholder = implode(',', array_fill(0, count($cohorts), '?'));
            $query = "SELECT Name FROM studentsinformation WHERE Cohort IN ($cohortsPlaceholder) AND Status = 'Active'";
            $stmt = $con->prepare($query);
            if ($stmt === false) {
                $this->debug_log("Failed to prepare statement: " . $con->error);
                return; // Exit if there is an error preparing the statement
            }
            $stmt->bind_param(str_repeat('s', count($cohorts)), ...$cohorts);
        }
    
        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Fetch data
        $students = [];
        while ($row = $result->fetch_assoc()) {
            $students[] = $row['Name'];
        }
    
        // Close statement and connection
        $stmt->close();
        $con->close();
    
        // Output data directly as JSON
        header('Content-Type: application/json');
        echo json_encode($students);
    }

    public function getRollNumbersByCohortsAndStudents($cohorts, $students) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
        
        // Sanitize inputs
        $cohorts = array_map([$this, 'legal_input'], explode(',', $cohorts));
        $students = array_map([$this, 'legal_input'], explode(',', $students));
        
        $rollNumbers = [];
        
        // Check if "All" is in cohorts list
        if (in_array('All', $cohorts)) {
            $query = "SELECT RollNumber FROM studentmap WHERE status = 'Active'";
            $stmt = $con->prepare($query);
            if ($stmt) {
                $stmt->execute();
                $result = $stmt->get_result();
                while ($row = $result->fetch_assoc()) {
                    $rollNumbers[] = $row['RollNumber'];
                }
                $stmt->close();
            } else {
                $this->debug_log("SQL prepare statement error: " . $con->error);
            }
        } else {
            // If "All" is not in cohorts list but "All" is in students list
            if (in_array('All', $students)) {
                $placeholders = implode(',', array_fill(0, count($cohorts), '?'));
                $query = "SELECT RollNumber FROM studentmap WHERE status = 'Active' AND cohort IN ($placeholders)";
                $stmt = $con->prepare($query);
                if ($stmt) {
                    $stmt->bind_param(str_repeat('s', count($cohorts)), ...$cohorts);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    while ($row = $result->fetch_assoc()) {
                        $rollNumbers[] = $row['RollNumber'];
                    }
                    $stmt->close();
                } else {
                    $this->debug_log("SQL prepare statement error: " . $con->error);
                }
            } else {
                // No "All" in either list, fetch specific students
                $placeholders = implode(',', array_fill(0, count($students), '?'));
                $query = "SELECT RollNumber FROM studentsinformation WHERE Name IN ($placeholders)";
                $stmt = $con->prepare($query);
                if ($stmt) {
                    $stmt->bind_param(str_repeat('s', count($students)), ...$students);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    while ($row = $result->fetch_assoc()) {
                        $rollNumbers[] = $row['RollNumber'];
                    }
                    $stmt->close();
                } else {
                    $this->debug_log("SQL prepare statement error: " . $con->error);
                }
            }
        }
        
        $con->close(); // Assuming you have a closeConnection method in your db class
        
        return ["success" => true, "rollNumbers" => $rollNumbers];
    }

    public function getRollNumbersByCohortsAndStudents2($cohorts, $students) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
        
        // Sanitize inputs
        $cohorts = array_map([$this, 'legal_input'], explode(',', $cohorts));
        $students = array_map([$this, 'legal_input'], explode(',', $students));
        
        $rollNumbers = [];
        
        // Check if "All" is in cohorts list
        if (in_array('All', $cohorts)) {
            $query = "SELECT id FROM studentmap WHERE status = 'Active'";
            $stmt = $con->prepare($query);
            if ($stmt) {
                $stmt->execute();
                $result = $stmt->get_result();
                while ($row = $result->fetch_assoc()) {
                    $rollNumbers[] = $row['id'];
                }
                $stmt->close();
            } else {
                $this->debug_log("SQL prepare statement error: " . $con->error);
            }
        } else {
            // If "All" is not in cohorts list but "All" is in students list
            if (in_array('All', $students)) {
                $placeholders = implode(',', array_fill(0, count($cohorts), '?'));
                $query = "SELECT id FROM studentsinformation WHERE status = 'Active' AND cohort IN ($placeholders)";
                $stmt = $con->prepare($query);
                if ($stmt) {
                    $stmt->bind_param(str_repeat('s', count($cohorts)), ...$cohorts);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    while ($row = $result->fetch_assoc()) {
                        $rollNumbers[] = $row['id'];
                    }
                    $stmt->close();
                } else {
                    $this->debug_log("SQL prepare statement error: " . $con->error);
                }
            } else {
                // No "All" in either list, fetch specific students
                $placeholders = implode(',', array_fill(0, count($students), '?'));
                $query = "SELECT id FROM studentsinformation WHERE Name IN ($placeholders)";
                $stmt = $con->prepare($query);
                if ($stmt) {
                    $stmt->bind_param(str_repeat('s', count($students)), ...$students);
                    $stmt->execute();
                    $result = $stmt->get_result();
                    while ($row = $result->fetch_assoc()) {
                        $rollNumbers[] = $row['id'];
                    }
                    $stmt->close();
                } else {
                    $this->debug_log("SQL prepare statement error: " . $con->error);
                }
            }
        }
        
        $con->close(); // Assuming you have a closeConnection method in your db class
        
        return ["success" => true, "rollNumbers" => $rollNumbers];
    }

    public function getDistinctCohorts() {
        require_once('Admindp.php');
        $con = new db();
    
        // SQL query to select distinct cohorts
        $query = "SELECT DISTINCT cohort FROM studentsinformation";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
    
        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Create an array to hold the results
        $data = array();
    
        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        return $data;
    }

    public function insertAnnouncement3($userID, $title, $content, $postedDateTime) {
        require_once('Admindp.php');
        $con = new db();
    
        // Sanitize inputs
        $userID = $this->legal_input($userID);
        $title = $this->legal_input($title);
        $content = $this->legal_input($content);
        $postedDateTime = $this->legal_input($postedDateTime);
    
        $query = "INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo) VALUES (?, ?, ?, ?, 'Students')";
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            $con->close();
            echo json_encode(["success" => false, "message" => "Statement preparation failed: " . $con->error]);
            return;
        }
        $stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);
        $result = $stmt->execute();
        if ($result === false) {
            $stmt->close();
            $con->close();
            echo json_encode(["success" => false, "message" => "Execution failed: " . $stmt->error]);
            return;
        }
    
        $announcementID = $con->lastInsertID(); // Get the ID of the last inserted row
    
        $stmt->close();
        $con->close();
        echo json_encode(["success" => true, "announcementID" => $announcementID, "message" => "Announcement inserted successfully"]);
    }

    public function insertAnnouncement5($userID, $title, $content, $postedDateTime, $sentTo, $receiverID) {
        require_once('Admindp.php');
        $con = new db();
    
        // Sanitize inputs
        $userID = $this->legal_input($userID);
        $title = $this->legal_input($title);
        $content = $this->legal_input($content);
        $postedDateTime = $this->legal_input($postedDateTime);
    
        $query = "INSERT INTO sendnotifications (userID, Title, Content, PostedDateTime, SentTo, ReceiverID) VALUES (?, ?, ?, ?, ?, ?)";
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            $con->close();
            echo json_encode(["success" => false, "message" => "Statement preparation failed: " . $con->error]);
            return;
        }
        $stmt->bind_param("ssssss", $userID, $title, $content, $postedDateTime, $sentTo, $receiverID);
        $result = $stmt->execute();
        if ($result === false) {
            $stmt->close();
            $con->close();
            echo json_encode(["success" => false, "message" => "Execution failed: " . $stmt->error]);
            return;
        }
    
        $announcementID = $con->lastInsertID(); // Get the ID of the last inserted row
    
        $stmt->close();
        $con->close();
        echo json_encode(["success" => true, "MsgID" => $announcementID, "message" => "Message inserted successfully"]);
    }

    public function insertAnnouncement4($userID, $title, $content, $postedDateTime) {
        require_once('Admindp.php');
        $con = new db();
    
        // Sanitize inputs
        $userID = $this->legal_input($userID);
        $title = $this->legal_input($title);
        $content = $this->legal_input($content);
        $postedDateTime = $this->legal_input($postedDateTime);
    
        $query = "INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo) VALUES (?, ?, ?, ?, 'Users')";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            echo json_encode(["success" => false, "message" => $con->error]);
            return;
        }
    
        // Bind parameters
        $stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);
    
        // Execute the query
        if ($stmt->execute()) {
            // Get the ID of the inserted announcement
            $announcementID = $stmt->insert_id;
            $stmt->close();
            $con->close();  
            echo json_encode(["success" => true, "announcementID" => $announcementID]);
        } else {
            $stmt->close();
            $con->close();
            echo json_encode(["success" => false, "message" => "Failed to submit announcement"]);
        }
    }

    public function insertAnnouncement2($userID, $title, $content, $postedDateTime) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize inputs
        $userID = $this->legal_input($userID);
        $title = $this->legal_input($title);
        $content = $this->legal_input($content);
        $postedDateTime = $this->legal_input($postedDateTime);

        $query = "INSERT INTO announcementbyro (userID, Title, Content, PostedDateTime, SentTo) VALUES (?, ?, ?, ?, 'faculty')";
        
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["success" => false, "message" => "Failed to prepare SQL statement"];
        }

        $stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);

        // Execute the statement
        if ($stmt->execute()) {
            $announcementID = $stmt->insert_id;
            $stmt->close();
            $con->close();
            return ["success" => true, "announcementID" => $announcementID];
        } else {
            $stmt->close();
            $con->close();
            return ["success" => false, "message" => "Failed to submit announcement"];
        }
    }

    public function getAnnouncementsForUser($userID) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
    
        // SQL query to fetch announcements based on the userID
        $query = "
        SELECT ar.Title, ar.Content, u.Name, ar.PostedDateTime
        FROM announcementbyro_userrecipients au
        JOIN announcementbyro ar ON au.AnnouncementID = ar.AnnouncementID
        JOIN users u ON u.id = ar.userID
        WHERE au.UserID = ?";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            $this->debug_log("Failed to prepare SQL statement: " . $con->error);
            return ["status" => "error", "message" => "Failed to prepare SQL statement"];
        }
    
        // Bind the parameter
        $stmt->bind_param("i", $userID);
    
        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();
    
        return ["success" => true, "data" => $data];
    }

    public function insertFacultyRecipients($announcementID, $facultyNames) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
        
        // Sanitize inputs
        $announcementID = $this->legal_input($announcementID);
        $facultyNames = array_map([$this, 'legal_input'], $facultyNames);

        // Function to get FacultyID by FacultyName
        function getFacultyID($facultyName, $con) {
            $sql = "SELECT FacultyID FROM faculty WHERE FacultyName = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("s", $facultyName);
            $stmt->execute();
            $stmt->bind_result($facultyID);
            $stmt->fetch();
            $stmt->close();
            return $facultyID;
        }

        // Prepare SQL statement for inserting recipients
        $recipientSql = "INSERT INTO announcementbyro_facultyrecipients (AnnouncementID, FacultyID) VALUES (?, ?)";
        $recipientStmt = $con->prepare($recipientSql);
        if ($recipientStmt === false) {
            $this->debug_log("Failed to prepare SQL statement: " . $con->error);
            return ["success" => false, "message" => "Failed to prepare SQL statement"];
        }

        $con->begin_transaction();
        foreach ($facultyNames as $facultyName) {
            if (!empty($facultyName)) {
                $facultyID = getFacultyID($facultyName, $con);
                if ($facultyID !== null) {
                    $recipientStmt->bind_param("ii", $announcementID, $facultyID);
                    if (!$recipientStmt->execute()) {
                        $this->debug_log("Error executing statement: " . $recipientStmt->error);
                        $con->rollback();
                        $recipientStmt->close();
                        $con->close();
                        return ["success" => false, "message" => "Error executing statement."];
                    }
                } else {
                    $con->rollback();
                    $recipientStmt->close();
                    $con->close();
                    return ["success" => false, "message" => "Faculty name not found: $facultyName"];
                }
            }
        }
        $con->commit();
        $recipientStmt->close();
        $con->close();
        return ["success" => true, "message" => "Recipients added successfully"];
    }

    public function addFacultyRecipients($announcementID) {
        require_once('Admindp.php');
        $con = new db();

        // Function to get all FacultyIDs
        $query = "SELECT FacultyID FROM faculty";
        $result = $con->SelectQueryExecutorFetchAssoc($query);

        if (empty($result)) {
            return json_encode(["success" => false, "message" => "No faculty members found"]);
        }

        // Insert each faculty member as a recipient
        $recipientSql = "INSERT INTO announcementbyro_facultyrecipients (AnnouncementID, FacultyID) VALUES (?, ?)";
        $recipientStmt = $con->prepare($recipientSql);

        foreach ($result as $row) {
            $facultyID = $row['FacultyID'];
            $recipientStmt->bind_param("ii", $announcementID, $facultyID);
            $recipientStmt->execute();
        }

        $recipientStmt->close();
        $con->close();

        return json_encode(["success" => true, "message" => "All faculty members added as recipients successfully"]);
    }

    public function insertAnnouncementRecipients($announcementID, $rollNumbers, $sectionID) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }
        // Sanitize inputs
        $announcementID = $this->legal_input($announcementID);
        $sectionID = $this->legal_input($sectionID);
        
        if (is_string($rollNumbers)) {
            $rollNumbers = explode(',', $rollNumbers);
        }
        
        if (!is_array($rollNumbers)) {
            $this->debug_log("Error: RollNumbers is not an array.");
            return ["success" => false, "message" => "Invalid RollNumbers format."];
        }
        
        $insertSql = "INSERT INTO announcementrecipients (AnnouncementID, RollNumber, sectionID) VALUES (?, ?, ?)";
        $insertStmt = $con->prepare($insertSql);
        
        if ($insertStmt === false) {
            $this->debug_log("Failed to prepare SQL statement: " . $con->error);
            return ["success" => false, "message" => "Failed to prepare SQL statement"];
        }
        
        $con->begin_transaction();
        foreach ($rollNumbers as $rollNumber) {
            $rollNumber = $this->legal_input($rollNumber);
            $insertStmt->bind_param("sss", $announcementID, $rollNumber, $sectionID);
            if (!$insertStmt->execute()) {
                $this->debug_log("Error executing statement: " . $insertStmt->error);
                $con->rollback();
                $insertStmt->close();
                $con->close();
                return ["success" => false, "message" => "Error executing statement."];
            }
        }
        $con->commit();
        $insertStmt->close();
        $con->close();
        return ["success" => true, "message" => "Records inserted successfully."];
    }

    public function addAnnouncementRecipients($announcementID, $rollNumbers) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }

        // Sanitize input
        $announcementID = intval($announcementID);
        $rollNumbersArray = explode(",", $this->legal_input($rollNumbers));

        // Check if there are roll numbers
        if (count($rollNumbersArray) > 0) {
            $insertSql = "INSERT INTO announcementrecipients (AnnouncementID, RollNumber) VALUES (?, ?)";
            $insertStmt = $con->prepare($insertSql);

            if ($insertStmt === false) {
                $this->debug_log("Failed to prepare SQL statement: " . $con->error);
                return ["success" => false, "message" => "Failed to prepare SQL statement"];
            }

            foreach ($rollNumbersArray as $rollNumber) {
                $rollNumber = intval($rollNumber); // Ensure it's an integer
                $insertStmt->bind_param("ii", $announcementID, $rollNumber);
                if (!$insertStmt->execute()) {
                    $this->debug_log("Failed to insert RollNumber: " . $rollNumber);
                }
            }

            $insertStmt->close();
            $con->close();

            return ["success" => true, "message" => "Recipients added successfully"];
        } else {
            return ["success" => false, "message" => "No roll numbers provided"];
        }
    }

    public function insertAnnouncement($userID, $title, $content, $postedDateTime) {
        require_once('Admindp.php');
        $con = new db();

        function debug_log($message) {
            file_put_contents('debug_log.txt', date('Y-m-d H:i:s') . " - $message\n", FILE_APPEND);
        }

        // SQL query to insert the announcement
        $query = "INSERT INTO announcements (facultyCoursesID, Title, Content, PostedDateTime) VALUES (?, ?, ?, ?)";

        // Prepare the statement
        $stmt = $con->prepare($query);

        // Check if the statement was prepared successfully
        if ($stmt === false) {
            $this->debug_log("Failed to prepare SQL statement: " . $con->error);
            return ["success" => false, "message" => "Failed to prepare SQL statement"];
        }

        // Bind parameters to the statement
        $stmt->bind_param("ssss", $userID, $title, $content, $postedDateTime);

        // Execute the statement
        if ($stmt->execute()) {
            // Get the ID of the inserted announcement
            $announcementID = $stmt->insert_id;

            // Log the successful insertion
            $this->debug_log("Announcement inserted with ID: $announcementID");

            // Return success and the announcement ID
            return ["success" => true, "announcementID" => $announcementID];
        } else {
            // Log the error if the statement execution fails
            $this->debug_log("Failed to execute statement: " . $stmt->error);
            return ["success" => false, "message" => "Failed to submit announcement"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();
    }

    public function getRollNumbersForCourseAndSession($courseName, $sessionDescription) {
        require_once('Admindp.php');
        $con = new db();
    
        $query = "
        SELECT DISTINCT sm.RollNumber 
        FROM studentmap sm 
        INNER JOIN offeredcourses oc ON sm.CourseID = oc.CourseID 
        INNER JOIN academicsession a ON sm.SessionID = a.SessionID 
        INNER JOIN courses c ON sm.CourseID = c.CourseID
        WHERE c.Name = ? AND a.Description = ?";
    
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["success" => false, "message" => $con->error];
        }
    
        $stmt->bind_param("ss", $courseName, $sessionDescription);
        $stmt->execute();
        $stmt->bind_result($rollNumber);
    
        $rollNumbers = [];
        while ($stmt->fetch()) {
            $rollNumbers[] = $rollNumber;
        }
    
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        if (count($rollNumbers) > 0) {
            return ["success" => true, "rollNumbers" => $rollNumbers];
        } else {
            return ["success" => false, "message" => "No students found for the given course and session"];
        }
    }

    public function fetchFacultyAnnouncements($facultyID) {
        require_once('Admindp.php');
        $con = new db();
    
        $query = "
        SELECT ar.Title, ar.Content, u.Name, f.FacultyName, ar.PostedDateTime
        FROM announcementbyro_facultyrecipients af 
        INNER JOIN announcementbyro ar ON af.AnnouncementID = ar.AnnouncementID
        INNER JOIN faculty f ON af.FacultyID = f.FacultyID
        INNER JOIN users u ON ar.userID = u.id
        WHERE f.FacultyID = ?";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
    
        // Bind parameters and execute
        $stmt->bind_param("s", $facultyID);
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();
    
        return $data;
    }

    public function getSectionsByFacultyCourseSession($facultyID, $courseName, $sessionDescription) {
        require_once('Admindp.php');
        $con = new db();
    
        $query = "
        SELECT s.SectionName
        FROM faculty f
        INNER JOIN facultycourses fc ON f.FacultyID = fc.FacultyID
        INNER JOIN courses c ON c.CourseID = fc.CourseID
        INNER JOIN sections s ON s.CourseID = fc.CourseID
        INNER JOIN academicsession a ON a.SessionID = fc.SessionID
        WHERE f.FacultyID = ? AND c.Name = ? AND a.Description = ?
        ";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
    
        // Bind the parameters
        $stmt->bind_param("iss", $facultyID, $courseName, $sessionDescription);
    
        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Fetch the section names
        $sections = array();
        while ($row = $result->fetch_assoc()) {
            $sections[] = $row['SectionName'];
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        return $sections;
    }

    public function getAnnouncementsForStudent($rollNumber) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT ar.Title, ar.Content, f.FacultyName, ar.PostedDateTime, c.Name as NAME
        FROM announcementrecipients af 
        INNER JOIN announcements ar ON af.AnnouncementID = ar.AnnouncementID
        INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
        INNER JOIN facultycourses fc ON ar.facultyCoursesID = fc.CourseID
        INNER JOIN faculty f ON fc.id = f.FacultyID
        INNER JOIN courses c ON c.CourseID = fc.CourseID
        WHERE si.RollNumber = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        $stmt->bind_param("s", $rollNumber);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function FetchAnnouncements($id) {
        require_once('Admindp.php');
        $con = new db();

        // First SQL query to get announcements from faculty 
        $sql1 = "SELECT DISTINCT ar.Title, ar.Content, f.FacultyName AS Name, ar.PostedDateTime, c.Name AS Course
                 FROM announcementrecipients af 
                 INNER JOIN announcements ar ON af.AnnouncementID = ar.AnnouncementID
                 INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
                 INNER JOIN facultycourses fc ON ar.facultyCoursesID = fc.CourseID
                 INNER JOIN faculty f ON f.FacultyID = fc.FacultyID
                 INNER JOIN courses c ON c.CourseID = fc.CourseID
                 WHERE si.RollNumber = ?";

        // Prepare and execute the first query
        $stmt1 = $con->prepare($sql1);
        $stmt1->bind_param("s", $id);
        $stmt1->execute();
        $result1 = $stmt1->get_result();

        // Fetch data from the first query
        $data1 = array();
        while ($row = $result1->fetch_assoc()) {
            $data1[] = $row;
        }

        // Second SQL query to get announcements from admin
        $sql2 = "SELECT DISTINCT ar.Title, ar.Content, u.Name AS Name, ar.PostedDateTime, u.department AS Course
                 FROM announcementbyro_studentrecipients af 
                 INNER JOIN announcementbyro ar ON af.AnnouncementID = ar.AnnouncementID
                 INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
                 INNER JOIN users u ON u.id = ar.userID
                 WHERE si.RollNumber = ?";

        // Prepare and execute the second query
        $stmt2 = $con->prepare($sql2);
        $stmt2->bind_param("s", $id);
        $stmt2->execute();
        $result2 = $stmt2->get_result();

        // Fetch data from the second query
        $data2 = array();
        while ($row = $result2->fetch_assoc()) {
            $data2[] = $row;
        }

        // Combine results from both queries
        $combinedData = array_merge($data1, $data2);

        // Close statements and connection
        $stmt1->close();
        $stmt2->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $combinedData;
    }

    public function fetchAnnouncementsForStudent($rollNumber) {
        require_once('Admindp.php');
        $con = new db();
    
        // The SQL query to fetch announcements for a student
        $query = "
        SELECT ar.Title, ar.Content, u.Name, ar.PostedDateTime
        FROM announcementbyro_studentrecipients af 
        INNER JOIN announcementbyro ar ON af.AnnouncementID = ar.AnnouncementID
        INNER JOIN studentsinformation si ON af.RollNumber = si.RollNumber
        INNER JOIN users u ON ar.userID = u.id
        WHERE si.RollNumber = ?";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
    
        // Bind the roll number parameter
        $stmt->bind_param("s", $rollNumber);
    
        // Execute the statement
        $stmt->execute();
    
        // Get the result
        $result = $stmt->get_result();
    
        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        return $data;
    }
    
    public function getUserAnnouncements($id) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT ar.Title, ar.Content, u.Name, ar.PostedDateTime
        FROM announcementbyro_userrecipients au
        JOIN announcementbyro ar ON au.AnnouncementID = ar.AnnouncementID
        JOIN users u ON u.id = ar.userID
        WHERE au.UserID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind parameters and execute
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
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function fetchAllStudents() {
        require_once('Admindp.php');
        $con = new db();
    
        $query = "SELECT * FROM studentsinformation";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
    
        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Create an array to hold the results
        $data = array();
    
        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        return $data;
    }

    public function getCoursesByStudentNameAndSemester($studentName, $semesterDescription) {
        require_once('Admindp.php');
        $con = new db();

        // Prepare the SQL statement
        $query = "
        SELECT c.CourseID, c.Name
        FROM courses c
        JOIN studentmap sm ON c.CourseID = sm.CourseID
        JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
        WHERE si.Name = ? AND sm.SessionID = (
            SELECT SessionID 
            FROM academicsession 
            WHERE Description = ?
        )";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind parameters
        $stmt->bind_param("ss", $studentName, $semesterDescription);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function FetchFaculty() {
        require_once('Admindp.php');
        $con = new db();
        $query = "SELECT * FROM faculty";
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }
        $stmt->execute();
        $result = $stmt->get_result();
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        $stmt->close();
        $con->close();
        return $data;
    }

    public function getCoursesByFacultyAndSemester($FacultyName, $semesterDescription) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT c.CourseID, c.Name
        FROM courses c
        JOIN facultycourses fc ON c.CourseID = fc.CourseID 
        JOIN faculty f ON fc.FacultyID = f.FacultyID
        WHERE f.FacultyName = ? AND fc.SessionID = (
            SELECT SessionID 
            FROM academicsession 
            WHERE Description = ?
        )";

        $stmt = $con->prepare($query);
        $stmt->bind_param("ss", $FacultyName, $semesterDescription);
        $stmt->execute();
        $result = $stmt->get_result();

        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        $stmt->close();
        $con->close();

        return $data;
    }

    public function updateAttendance($attendanceStatus, $name, $date, $sessionID, $courseID, $sectionID) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize input
        $attendanceStatus = $this->legal_input($attendanceStatus);
        $name = $this->legal_input($name);
        $date = $this->legal_input($date);
        $sessionID = (int) $sessionID;
        $courseID = (int) $courseID;
        $sectionID = (int) $sectionID;

        $query = "
        UPDATE attendance a
        INNER JOIN studentmap sm ON sm.id = a.StudentMapID
        INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
        SET a.AttendanceStatus = ?
        WHERE si.Name = ? AND a.Date = ? AND sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if (!$stmt) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind parameters
        $stmt->bind_param("sssiii", $attendanceStatus, $name, $date, $sessionID, $courseID, $sectionID);

        // Execute the statement
        $executeResult = $stmt->execute();
        if (!$executeResult) {
            return ["status" => "error", "message" => $stmt->error];
        }

        // Check if any rows were affected
        if ($stmt->affected_rows > 0) {
            $response = ["status" => "success"];
        } else {
            $response = ["status" => "failure", "message" => "No rows affected"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();

        return $response;
    }

    public function getStudentAttendanceByFaculty($facultyID, $sectionID, $date) {
        require_once('Admindp.php');
        $con = new db();

        $query = "
        SELECT * 
        FROM studentmap sm
        INNER JOIN facultycourses fc ON sm.SessionID = fc.SessionID 
            AND fc.CourseID = sm.CourseID 
            AND fc.SectionID = sm.SectionID
        INNER JOIN studentsinformation si ON si.RollNumber = sm.RollNumber
        WHERE fc.FacultyID = ? AND fc.SectionID = ? AND fc.SessionID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if (!$stmt) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind the parameters
        $stmt->bind_param("iii", $facultyID, $sectionID, $date);

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Create an array to hold the results
        $data = array();

        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function markAttendance($attendanceStatus, $name, $date, $sessionID, $courseID, $sectionID) {
        require_once('Admindp.php');
        $con = new db();
        
        // Step 1: Fetch the StudentMapID based on the provided criteria
        $queryFetch = "
        SELECT sm.id AS StudentMapID 
        FROM studentmap sm 
        INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber 
        WHERE si.Name = ? AND sm.SessionID = ? AND sm.CourseID = ? AND sm.SectionID = ?";

        // Prepare and execute the statement
        $stmtFetch = $con->prepare($queryFetch);
        if (!$stmtFetch) {
            return array("status" => "error", "message" => "Prepare failed for fetching StudentMapID: " . $con->error);
        }
        $stmtFetch->bind_param("ssss", $name, $sessionID, $courseID, $sectionID);
        $stmtFetch->execute();
        $stmtFetch->bind_result($studentMapID);
        $stmtFetch->fetch();
        $stmtFetch->close();
        
        if (!$studentMapID) {
            return array("status" => "error", "message" => "No StudentMapID found for the given criteria");
        }

        // Step 2: Insert the attendance status into the attendance table
        $queryInsert = "INSERT INTO attendance (StudentMapID, AttendanceStatus, Date, WarningsSent) VALUES (?, ?, ?, 0)";
        $stmtInsert = $con->prepare($queryInsert);
        if (!$stmtInsert) {
            return array("status" => "error", "message" => "Prepare failed for attendance insertion: " . $con->error);
        }
        $stmtInsert->bind_param("sss", $studentMapID, $attendanceStatus, $date);
        $executeResult = $stmtInsert->execute();
        
        // Close the statement and connection
        $stmtInsert->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        // Return the result
        if ($executeResult) {
            return array("status" => "success");
        } else {
            return array("status" => "failure", "message" => "Failed to insert attendance");
        }
    }

    public function getAttendanceByFacultySectionDate($facultyID, $sectionID, $date) {
        require_once('Admindp.php');
        $con = new db();

        // Query to get attendance data
        $query = "
        SELECT si.Name, ac.AttendanceStatus 
        FROM facultycourses fc
        INNER JOIN offeredcourses oc ON fc.CourseID = oc.CourseID AND fc.SessionID = oc.SessionID
        INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
        INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
        INNER JOIN courses c ON fc.CourseID = c.CourseID
        INNER JOIN sections s ON fc.SectionID = s.id
        INNER JOIN studentmap sm ON sm.SessionID = oc.SessionID AND sm.CourseID = oc.CourseID AND sm.SectionID = fc.SectionID
        INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
        INNER JOIN attendance ac ON ac.StudentMapID = sm.id
        WHERE a.Current = 1 AND f.FacultyID = ? AND s.id = ? AND ac.Date = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if (!$stmt) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind the parameters
        $stmt->bind_param("iis", $facultyID, $sectionID, $date);

        // Execute the statement
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getAttendanceDatesByFacultyAndSection($facultyID, $sectionID) {
        require_once('Admindp.php');
        $con = new db();

        // Sanitize input
        $facultyID = $this->legal_input($facultyID);
        $sectionID = $this->legal_input($sectionID);

        $query = "
        SELECT DISTINCT ac.Date 
        FROM facultycourses fc
        INNER JOIN offeredcourses oc ON fc.CourseID = oc.CourseID AND fc.SessionID = oc.SessionID
        INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
        INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
        INNER JOIN courses c ON fc.CourseID = c.CourseID
        INNER JOIN sections s ON fc.SectionID = s.id
        INNER JOIN studentmap sm ON sm.SessionID = oc.SessionID AND sm.CourseID = oc.CourseID AND sm.SectionID = fc.SectionID
        INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
        INNER JOIN attendance ac ON ac.StudentMapID = sm.id
        WHERE a.Current = 1 AND f.FacultyID = ? AND s.id = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if (!$stmt) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind parameters
        $stmt->bind_param("ii", $facultyID, $sectionID);
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getDistinctAttendanceDates($facultyID, $sessionID, $sectionID) {
        require_once('Admindp.php');
        $con = new db();

        // Define the SQL query
        $query = "
        SELECT DISTINCT a.Date 
        FROM attendance a
        INNER JOIN studentmap sm ON a.StudentMapID = sm.id
        INNER JOIN facultycourses fc ON fc.CourseID = sm.CourseID AND fc.SessionID = sm.SessionID AND sm.SectionID = fc.SectionID
        WHERE fc.FacultyID = ? AND fc.SessionID = ? AND fc.SectionID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind parameters
        $stmt->bind_param("sss", $facultyID, $sessionID, $sectionID);

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Fetch data as an associative array
        $data = array();
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getCurrentAcademicYearsByFaculty($facultyID) {
        require_once('Admindp.php');
        $con = new db();

        // Validate the FacultyID input
        $facultyID = $this->legal_input($facultyID);
        if (empty($facultyID)) {
            return ["status" => "error", "message" => "FacultyID is required"];
        }

        // SQL query to select distinct academic years for the given FacultyID
        $query = "
        SELECT DISTINCT oc.Year 
        FROM facultycourses fc
        INNER JOIN offeredcourses oc ON fc.CourseID = oc.CourseID AND fc.SessionID = oc.SessionID
        INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
        INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
        WHERE a.Current = 1 AND f.FacultyID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind the FacultyID parameter to the SQL query
        $stmt->bind_param("i", $facultyID);

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Create an array to hold the results
        $data = array();

        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getFacultyCoursesDetails2($facultyID) {
        require_once('Admindp.php');
        $con = new db();

        // SQL query to get faculty course details
        $query = "
        SELECT *, c.Name as CNAME, s.id as SectionID 
        FROM facultycourses fc
        INNER JOIN offeredcourses oc ON fc.CourseID = oc.CourseID AND fc.SessionID = oc.SessionID
        INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
        INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
        INNER JOIN courses c ON fc.CourseID = c.CourseID
        INNER JOIN sections s ON fc.SectionID = s.id
        WHERE a.Current = 1 AND f.FacultyID = ?";

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind the FacultyID parameter to the SQL query
        $stmt->bind_param("i", $facultyID);

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Create an array to hold the results
        $data = array();

        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

        return $data;
    }

    public function getFacultyCoursesDetails($facultyID) {
        require_once('Admindp.php');
        $con = new db();

        // SQL query to select all columns from the users table
        $query = "
        SELECT *, c.Name as CNAME, s.SectionName as SectionName 
        FROM facultycourses fc
        INNER JOIN offeredcourses oc ON fc.CourseID = oc.CourseID AND fc.SessionID = oc.SessionID
        INNER JOIN academicsession a ON a.SessionID = oc.SessionID 
        INNER JOIN faculty f ON fc.FacultyID = f.FacultyID
        INNER JOIN courses c ON fc.CourseID = c.CourseID
        INNER JOIN sections s ON s.id = fc.SectionID
        WHERE a.Current = 1 AND oc.Year = ?";
        

        // Prepare the statement
        $stmt = $con->prepare($query);
        if ($stmt === false) {
            return ["status" => "error", "message" => $con->error];
        }

        // Bind the FacultyID parameter to the SQL query
        $stmt->bind_param("i", $facultyID);

        // Execute the query
        $stmt->execute();
        $result = $stmt->get_result();

        // Create an array to hold the results
        $data = array();

        // Check if there are results
        if ($result->num_rows > 0) {
            // Fetch all rows as associative arrays
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        } else {
            $data = ["message" => "No results"];
        }

        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class

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
        $con->close();

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
        $con->close(); // Assuming you have a closeConnection method in your db class

        error_log("Update completed successfully");

        return ["status" => "success"];
    }

    public function updateStudentSection2($sessionID, $courseID, $studentNames) {
        require_once('Admindp.php');
        $con = new db();

        // Decode and sanitize student names
        $studentNames = urldecode($studentNames);
        $studentNamesArray = explode(",", $studentNames);
        $studentNamesArray = array_map([$this, 'legal_input'], $studentNamesArray);

        $query = "
            UPDATE studentmap sm
            INNER JOIN studentsinformation si ON sm.RollNumber = si.RollNumber
            SET sm.SectionID = NULL
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
            error_log("Executing query with parameters: SessionID=$sessionID, CourseID=$courseID, StudentName=$studentName");

            // Bind parameters
            $stmt->bind_param("iss", $sessionID, $courseID, $studentName);

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
        $con->close(); // Assuming you have a closeConnection method in your db class

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
        $con->close();

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
            $con->close();
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
        $con->close();

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
            $con->close();
            return null;
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
            $con->close();
    
            // Directly return the data array
            return $data;
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->close();
            return null;
        }
    }

    public function getCoursesStudents($rollNumber) {
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
            $con->close();
            return null;
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
            $con->close();
    
            // Directly return the data array
            return $data;
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->close();
            return null;
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
            $con->close();
            return null;
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
            $con->close();
    
            // Directly return the data array
            return $data;
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->close();
            return null;
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
        $con->close();

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
        $con->close();

        return $response;
    }

    public function getFacultyById($id) {
        require_once('Admindp.php');
        $con = new db();
    
        // Sanitize input
        $id = $this->legal_input($id);
    
        $query = "SELECT * FROM faculty WHERE id = ?";
    
        // Prepare the statement
        $stmt = $con->prepare($query);
    
        if (!$stmt) {
            error_log("Statement preparation failed: " . $con->error);
            $con->close();
            return; // Exit if there is an error preparing the statement
        }
    
        // Bind parameters
        $stmt->bind_param("s", $id);
    
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
            $con->close();
    
            // Return data directly as JSON
            header('Content-Type: application/json');
            echo json_encode($data);
        } else {
            error_log("Execution failed: " . $stmt->error);
            $stmt->close();
            $con->close();
        }
    }

    

    public function getUserData($id) {
        require_once('Admindp.php');
        $con = new db();  // Assuming this is your database connection class
    
        // Sanitize input
        $id = $this->legal_input($id);
    
        $query = "SELECT * FROM users WHERE id = ?";
        $stmt = $con->prepare($query);
        
        if (!$stmt) {
            return [
                "status" => "error",
                "message" => "Statement preparation failed: " . $con->error
            ];
        }
    
        $stmt->bind_param("s", $id);
        $stmt->execute();
        $result = $stmt->get_result();
    
        // Fetch data as an associative array
        $data = [];
        while ($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
    
        // Close the statement and connection
        $stmt->close();
        $con->close();  // Assuming you have a closeConnection method in your db class
    
        // Return data directly as JSON
        header('Content-Type: application/json');
        echo json_encode($data);
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