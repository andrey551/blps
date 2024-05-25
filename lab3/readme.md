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
