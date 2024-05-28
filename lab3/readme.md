## Description
### Node 1: 
1. Scheduled to check list files on Trash more than 1 day
2. Send list to second node
### Node 2:
1. Receive list from first node
2. Delete files
3. Send email to user to notify 
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
