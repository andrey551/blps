## Description
### Node 1: 
1. Is scheduled to check list files on Trash more than 1 day each 24 hours ( Quartz)
2. Send list to second node( JMS api - RabbitMQ)
### Node 2:
1. Receive list from first node( JMS api - RabbitMQ)
2. Delete files
3. Send email to user to notify list file deleted (Users can receive only file deleted belong to them, multi user is supported) (Spring mail)
------------------------
## List API 
### /auth
* POST: /signup (AccountDTO request) 
* POST /signin (AccountDTO request) 

### /file
* GET /list(String token) : List<File>
* POST /add(FileDTO file) : ResponseEntity
* DELETE /{idStr}

### /message
* POST /send( Message msg)

### /payment
* POST /update(PaymentDTO paymentDTO)

### /user
* DELETE /del()
