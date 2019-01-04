# Quest1
/Quest 1 for intive Patronage 2018/2019

## 1. Building application
To build application use command:
>*"gradle build"*

## 2. Running application
To run application use command:
>*"java -jar Quest1-0.1.3.jar"*

## 3. Creating an example conference room
### 3.1 Create an organization
To create a conference room you must first create an organization. To do this, use command:

    curl -H "Content-Type: application/json" -d '{"name":"[ORGANIZATION_NAME]"}' localhost:8080/organizations
    
### 3.2 Create a conference room
To create full conference room, use command:

    curl -H "Content-Type: application/json" -d '{"name":"ConfRoomName","id":"1.33","floor":3,"availability":true,"numberOfSeatsAndStanding":15,"numberOfLyingPlaces":3,"numberOfHangingPlaces":1,"projectorName":"Solar","phone":true,"internalNumber":56,"externalNumber":"+12 123456789","phoneInterface":"USB"}' localhost:8080/organizations/[ORGANIZATION_NAME]
    
**Remember!**

You do not have to use all fields. Mandatory are only: name, floor, availability (default is true) and numberOfSeatsAndStanding. For example:

    curl -H "Content-Type: application/json" -d '{"name":"ConfRoomName","floor":3,"availability":true,"numberOfSeatsAndStanding":15}' localhost:8080/organizations/[ORGANIZATION_NAME]
