**Google Cloud Functions Student Management Application**


**For local testing set up using Functions Framework and Gradle:**

    gradlew runFunction -Prun.functionTarget=functions.CreateStudent
    
    gradlew runFunction -Prun.functionTarget=functions.GetStudents
    
    gradlew runFunction -Prun.functionTarget=functions.UpdateStudent

***POST curl request:***

    curl -X POST http://localhost:8080 -H "Content-Type:application/json" -d {"firstName":"Lorem","lastName":"Ipsum","studentNumber":"250","dob":"2.09.1950","email":"test@admin.com","semester":"5","degree":"\"M.Sc. Yada yada\"","address":"\"Street Yada yada\""}

***GET curl request:***

    curl -X GET http://localhost:8080 -H "Accept: application/json" (Without parameters will get all students)
    
    curl -X GET http://localhost:8080/?studentNumber=2014848 -H "Accept: application/json" (Get student by student number)
***PUT curl request:***

    curl -X PUT http://localhost:8080/?studentNumber=2004849 -H "Content-Type:application/json" -d {"firstName":"Test"}
    
    curl -X PUT http://localhost:8080/?studentNumber=2004849 -H "Content-Type:application/json" -d {"firstName":"Abc","semester":"3"}