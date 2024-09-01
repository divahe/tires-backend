### Tires-backend
This application is backend for tire change booking application, that mediates booking information from third party masteries.
Application is set to be configurable and new API configurations can be added to the application.

### Database
Application does not have its own database

### Configuration
- 'application.properties' file configures application
- 'configuration.json' configures third party APIs
- Both configuration files are located on the classpath

### Tests
- Tests are located in the 'src/test' folder
- Configuration file for tests is 'configuration.json'
- Configuration is located on the test classpath

### API
- Server is running controllers on 'http://localhost:8080/api/v1/'
- Cross-origin for frontend is set for http://localhost:5173"
- All endpoints use 'application/json' content type

There are two endpoints: 
##### 'bookings' for GET
- returns a list of available bookings from the current date approximately 1 month forward
- in case third party API is not responsive, returns a notice about it

##### 'booking/book' for PUT
 - requires booking details in the request body
 - returns success info when booking is confirmed from the third party
 - returns rejection reason if booking was not confirmed or third party API was not responsive



