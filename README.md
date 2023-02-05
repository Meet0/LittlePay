# LittlePay
LittlePay Code Challenge


## Matching & Pricing Taps
Due to time constraints, this program is an implementation of the core business logic - Matching and Pricing Taps and its tests. The reading and generation of CSV works as expected, but due to time constraints, that part has not been tested. It is assumed that the user will provide a valid data file in the correct format.

## Assumptions
1. It was originally assumed that tap/s would be received via API calls rather than a CSV in a real-world scenario (hence the use of Springboot as I planned to implement the api part of the application, but due to time contraints, that aspect was not completed).
2. As highlighted in the document, it is assumed that the tap data will be valid and in the correct format.
3. It is assumed that the data returned in CSV is of high priority and hence generic async DB persist is used. If DB is to be used to trigger event (add trips to queue to be processed by payments service), that logic should be removed.

## Further Improvements
1. First and foremost, The PAN data should be appropriately encrypted and the application should be secured if it's meant to be an API (which is preferred).
2. The Bus, Stop, and Company should be objects with their own attributes and have their own tables. A relational DB should be properly designed (possibly sharded for calculating fares).
3. A cache mechanism should be designed for storing Fares (memcached?).
4. The Fare table ideally should be sharded based on BusId as it is assumed that a Bus identified by BusID would follow a specific bus route, 
    and hence the use of `findTopByOrderByIdDesc` or any similar query would result in giving the last stop (given bus routes go from orgin/final stop).
5. Generic Async DB persist/save call is currently implemented as we are returning data in CSV currently, but that should be possibly updated to a better version of async persist
    and perhaps a MessageQueue should be used to add the fares which I assume would be processed by a payments service.
6. User built data access repositories should be used to interact with RDS (Oracle/MySQL) for further data cleanup (if required) and performance management.
7. Code coverage should be improved after above changes/regardless of above changes, as the CSV reader/writer service has not been tested.
8. Proper data input validation (input CSV data + format of that data) and relevant exceptions should be created/used along with improved logging using the appropriate logger to better track performance issues as well as bugs.

## Performance Considerations
A Hashmap is being used currently to store tap data which is also removed as a matching tap off is received, and that would seem to in the future cause a performance issue. However, based on VIC metro statistics, there are daily 1.5 million uses of VIC metro services. Considering that and an average user travel time (tap on + off) of over an hour, a Hashmap would be a better choice given the above context for now. As the system scales, a better solution should be designed/opted for. 

Due to limited time, the full potential of this program has yet to be realized. If more time was available, the above improvements and considerations could be made to ensure the program is as efficient and secure as possible.


Author: Meet Chaudhary
