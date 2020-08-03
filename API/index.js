const express = require('express');
const app = express();
const mongoose = require('mongoose');
const dotenv = require('dotenv');
//Importing Routes
const authRoute = require('./routes/auth');

dotenv.config();

mongoose.connect('mongodb://127.0.0.1/latticebluetoothmessaging', {useNewUrlParser: true, useUnifiedTopology: true});

//Middleware
app.use(express.json());

//Route Middleware
app.use('/api/user', authRoute);

app.listen(3000, () => {
    console.log("Starting Server on PORT 3000");
})