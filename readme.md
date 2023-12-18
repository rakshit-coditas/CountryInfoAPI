clone project go to folder where src is there
open command prompt 
run 
mvn clean install
: target folder created
: cd target
run 
java -jar countryinfoapi-0.0.1-SNAPSHOT.jar

open another cmd
run following commands for specific API

# Country Info API

Welcome to the Country Info API documentation. This API provides information about countries.

## Getting Started

Follow the instructions below to set up and run the application.

## Testing the APIs

### 1. Login API

Use the following curl command to authenticate and obtain a JWT token:
not using any DB : only 1 user for demo purpose : username : abcd & password : abcd

curl -X POST "http://localhost:8080/api/login" -H "Content-Type: application/json" -d "{\"username\": \"abcd\", \"password\": \"abcd\"}"
Sample Response:

json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkIiwiaWF0IjoxNzAyOTE3OTYwLCJleHAiOjE3MDI5MTk3NjB9.IY2cUMcwUN8Gn5khCxAIwq1nt52ZslTU0MPZ_BdPKPA",
  "expirationTime": "2023-12-18T16:36:09.000+00:00"
}

2. Filter Countries API
Use the JWT token obtained from the login API in the Authorization header for the filter countries API:
for pagination use these attributes : 
pageNo=1   current page to view by default set to 1
pageSize=25; no of records in each page by default set to DOUBLE.MAX_VALUE
sortDir  ( ASC, DESC)
sortCol (NAME, POPULATION, AREA)

for country filters use these attributes : 
languages pass list of languages example : ["English", "French"]
minArea      from area value   ex: 5000
maxArea      to area value     ex: 10000
minPopulation   from population value   ex: 500
maxPopulation   to population value     ex: 5000

curl -X POST "http://localhost:8080/api/countries/filter" -H "Content-Type: application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkIiwiaWF0IjoxNzAyOTE3OTYwLCJleHAiOjE3MDI5MTk3NjB9.IY2cUMcwUN8Gn5khCxAIwq1nt52ZslTU0MPZ_BdPKPA" -d "{\"countryFilters\": {\"languages\": [\"English\", \"French\"],\"minArea\": 1000.0,\"maxArea\": 5000.0,\"minPopulation\": 100000,\"maxPopulation\": 5000000},\"paginationInfo\": {\"pageNo\": 1,\"pageSize\": 10,\"sortDir\": \"ASC\",\"sortCol\": \"POPULATION\"}}"

Sample Response:

{
  "pageNo": 1,
  "pageSize": 10,        
  "totalRecords": 8,    // total no of records found by filter
  "content": [
    {
      "name": {"common": "French Polynesia", "official": "French Polynesia"},
      "languages": {"fra": "French"},
      "area": 4167.0,
      "population": 280904
    },
    // ... other countries
  ]
}

3. Get Country Info API
Retrieve information about a specific country using the following curl command:
here country name is usa (String)

curl -X GET "http://localhost:8080/api/countries/info/usa" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkIiwiaWF0IjoxNzAyOTE3OTYwLCJleHAiOjE3MDI5MTk3NjB9.IY2cUMcwUN8Gn5khCxAIwq1nt52ZslTU0MPZ_BdPKPA"

Sample Response:

[
  {
    "name": {"common": "United States", "official": "United States of America", "nativeName": {"eng": {"official": "United States of America", "common": "United States"}}},
    "tld": [".us"],
    // ... other country information
  }
]
