**Google Cloud Functions Student Management Application**


**For local testing set up using Functions Framework and Gradle:**

gradlew runFunction -Prun.functionTarget=functions.CreateStudent

gradlew runFunction -Prun.functionTarget=functions.GetStudents

***Post curl request:***

curl -X POST http://localhost:8080 -H "Content-Type:application/json" -d {"firstName":"Lorem","lastName":"Ipsum","studentNumber":"250","dob":"2.09.1950","email":"test@admin.com","semester":"5","degree":"\"M.Sc. Yada yada\"","address":"\"Street Yada yada\""}

***Get curl request:***

curl -X GET http://localhost:8080 -H "Accept: application/json" (Without parameters will get all students)