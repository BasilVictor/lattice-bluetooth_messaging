const router = require('express').Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const User = require('../model/User');

router.post('/signUp', async(req, res) => {
    //Checking existing user  
    const emailExist = await User.findOne({email: req.body.email});
    if(emailExist) {
        return res.status(405).send('Email already Signed Up');
    }

    //Hashing password
    const salt = await bcrypt.genSalt(10);
    const hashPassword = await bcrypt.hash(req.body.password, salt);

    //Creating new User
    const user = new User({
        name: req.body.name,
        address: req.body.address,
        phone: req.body.phone,
        email: req.body.email,
        password: hashPassword
    });

    try {
        const savedUser = await user.save();
        const token = jwt.sign({_id: savedUser._id}, process.env.TOKEN_SECRET);
        res.status(200).send({name: savedUser.name, token: token});
    } catch (err) {
        console.log(err);
        res.status(400).send(err);
    }
});

router.post('/signIn', async(req, res) => {
    const user = await User.findOne({email: req.body.email});
    if(!user) {
        return res.status(405).send('Email or password is wrong');
    }
    const validPassword = await bcrypt.compare(req.body.password, user.password);
    if(!validPassword) {
        return res.status(405).send('Email or password is wrong');
    }

    //Create JWT
    const token = jwt.sign({_id: user._id}, process.env.TOKEN_SECRET);
    res.header('auth-token', token);
    res.status(200).send({name: user.name, token: token});
})

module.exports = router;