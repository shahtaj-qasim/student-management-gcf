**Google Cloud Functions Student Management Application**

**Technologies**
1. Java 11
2. Gradle
3. IntelliJ IDEA
4. gcloud command line tool
5. Google Cloud Functions
6. Cloud Firestore
7. Cloud Logging
8. Functions Framework
9. Mockito

**Command for logging in**
    
    gcloud auth login
    
**For local testing set up using Functions Framework and Gradle:**

    gradlew runFunction -Prun.functionTarget=functions.CreateStudent
    
    gradlew runFunction -Prun.functionTarget=functions.GetStudents
    
    gradlew runFunction -Prun.functionTarget=functions.UpdateStudent
    
    gradlew runFunction -Prun.functionTarget=functions.DeleteStudent

***POST curl request:***

    curl -X POST http://localhost:8080 -H "Content-Type:application/json" -d {"firstName":"Lorem","lastName":"Ipsum","studentNumber":"250","dob":"2.09.1950","email":"test@admin.com","semester":"5","degree":"\"M.Sc. Yada yada\"","address":"\"Street Yada yada\""}

***GET curl request:***

    curl -X GET http://localhost:8080 -H "Accept: application/json" (Without parameters will get all students)
    
    curl -X GET http://localhost:8080/?studentNumber=2014848 -H "Accept: application/json" (Get student by student number)
***PUT curl request:***

    curl -X PUT http://localhost:8080/?studentNumber=2004849 -H "Content-Type:application/json" -d {"firstName":"Test"}
    
    curl -X PUT http://localhost:8080/?studentNumber=2004849 -H "Content-Type:application/json" -d {"firstName":"Abc","semester":"3"}

***DELETE curl request:***

    curl -X DEL http://localhost:8080/?studentNumber=2099088
    
**Unit tests execution**

_Execute all test cases_
    
    gradlew tests

_Execute single test cases_

    gradlew test --tests functions.unittests.GetStudentsTest.getAStudent_Test
    gradlew test --tests functions.unittests.UpdateStudentTest.updateStudent_Test
    gradlew test --tests functions.unittests.CreateStudentTest.createStudent_Test
    gradlew test --tests functions.unittests.DeleteStudentTest.deleteStudent_Test