
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